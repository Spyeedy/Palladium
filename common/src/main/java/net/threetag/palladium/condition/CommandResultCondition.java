package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

import java.util.Objects;

public record CommandResultCondition(String command, String comparison, int compareTo,
                                     boolean log) implements Condition, CommandSource {

    public static final MapCodec<CommandResultCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("command").forGetter(CommandResultCondition::command),
                    Codec.STRING.fieldOf("comparison").forGetter(CommandResultCondition::comparison),
                    Codec.INT.fieldOf("compare_to").forGetter(CommandResultCondition::compareTo),
                    Codec.BOOL.optionalFieldOf("log", false).forGetter(CommandResultCondition::log)
            ).apply(instance, CommandResultCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CommandResultCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, CommandResultCondition::command,
            ByteBufCodecs.STRING_UTF8, CommandResultCondition::comparison,
            ByteBufCodecs.VAR_INT, CommandResultCondition::compareTo,
            ByteBufCodecs.BOOL, CommandResultCondition::log,
            CommandResultCondition::new
    );

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        if (entity.level() instanceof ServerLevel serverLevel) {
            var stack = new CommandSourceStack(this, entity.position(), entity.getRotationVector(),
                    serverLevel, 2, entity.getName().getString(), Objects.requireNonNull(entity.getDisplayName()), serverLevel.getServer(),
                    entity)
                    .withSuppressedOutput();

            if (!this.log) {
                stack = stack.withSuppressedOutput();
            }

            int result = serverLevel.getServer().getCommands().performPrefixedCommand(stack, command);

            return switch (comparison) {
                case ">=" -> (result >= compareTo);
                case "<=" -> (result <= compareTo);
                case ">" -> (result > compareTo);
                case "<" -> (result < compareTo);
                case "!=" -> (result != compareTo);
                case "==" -> (result == compareTo);
                default -> false;
            };
        } else {
            return false;
        }
    }

    @Override
    public ConditionSerializer<CommandResultCondition> getSerializer() {
        return ConditionSerializers.COMMAND_RESULT.get();
    }

    @Override
    public void sendSystemMessage(Component component) {
        if (this.log) {
            AddonPackLog.info("Command Result Condition Log: " + component.getString());
        }
    }

    @Override
    public boolean acceptsSuccess() {
        return this.log;
    }

    @Override
    public boolean acceptsFailure() {
        return this.log;
    }

    @Override
    public boolean shouldInformAdmins() {
        return this.log;
    }

    public static class Serializer extends ConditionSerializer<CommandResultCondition> {

        @Override
        public MapCodec<CommandResultCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CommandResultCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Executes a command and compares the output to a number.";
        }
    }
}
