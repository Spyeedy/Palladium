package net.threetag.palladium.power.ability;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.CommandFunctionProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.SyncOption;

import java.util.Collections;
import java.util.Objects;

public class CommandAbility extends Ability implements CommandSource {

    public static final PalladiumProperty<CommandFunctionProperty.CommandFunctionParsing> FIRST_TICK_COMMANDS = new CommandFunctionProperty("first_tick_commands").sync(SyncOption.NONE).disablePersistence().configurable("Sets the commands which get executed when gaining/activating the ability");
    public static final PalladiumProperty<CommandFunctionProperty.CommandFunctionParsing> LAST_TICK_COMMANDS = new CommandFunctionProperty("last_tick_commands").sync(SyncOption.NONE).disablePersistence().configurable("Sets the commands which get executed when losing/deactivating the ability");
    public static final PalladiumProperty<CommandFunctionProperty.CommandFunctionParsing> COMMANDS = new CommandFunctionProperty("commands").sync(SyncOption.NONE).disablePersistence().configurable("Sets the commands which get executed when using the ability");

    public CommandAbility() {
        this.withProperty(ICON, new ItemIcon(Blocks.COMMAND_BLOCK));
        this.withProperty(FIRST_TICK_COMMANDS, new CommandFunctionProperty.CommandFunctionParsing(Collections.emptyList()));
        this.withProperty(LAST_TICK_COMMANDS, new CommandFunctionProperty.CommandFunctionParsing(Collections.emptyList()));
        this.withProperty(COMMANDS, new CommandFunctionProperty.CommandFunctionParsing("say Hello World"));
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level().getServer() != null && entry.getProperty(FIRST_TICK_COMMANDS) != null && entity.level() instanceof ServerLevel serverLevel) {
            var source = this.createCommandSourceStack(entity, serverLevel);
            Objects.requireNonNull(entity.level().getServer()).getFunctions().execute(entry.getProperty(FIRST_TICK_COMMANDS).getCommandFunction(entity.level().getServer()), source.withSuppressedOutput().withMaximumPermission(2));
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level().getServer() != null && entry.getProperty(COMMANDS) != null && entity.level() instanceof ServerLevel serverLevel) {
            var source = this.createCommandSourceStack(entity, serverLevel);
            Objects.requireNonNull(entity.level().getServer()).getFunctions().execute(entry.getProperty(COMMANDS).getCommandFunction(entity.level().getServer()), source.withSuppressedOutput().withMaximumPermission(2));
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level().getServer() != null && entry.getProperty(LAST_TICK_COMMANDS) != null && entity.level() instanceof ServerLevel serverLevel) {
            var source = this.createCommandSourceStack(entity, serverLevel);
            Objects.requireNonNull(entity.level().getServer()).getFunctions().execute(entry.getProperty(LAST_TICK_COMMANDS).getCommandFunction(entity.level().getServer()), source.withSuppressedOutput().withMaximumPermission(2));
        }
    }

    public CommandSourceStack createCommandSourceStack(LivingEntity entity, ServerLevel serverLevel) {
        return new CommandSourceStack(this, entity.position(), entity.getRotationVector(),
                serverLevel, 2, entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity)
                .withSuppressedOutput();
    }

    @Override
    public void sendSystemMessage(Component component) {

    }

    @Override
    public boolean acceptsSuccess() {
        return false;
    }

    @Override
    public boolean acceptsFailure() {
        return false;
    }

    @Override
    public boolean shouldInformAdmins() {
        return false;
    }

    @Override
    public String getDocumentationDescription() {
        return "Executes commands.";
    }
}
