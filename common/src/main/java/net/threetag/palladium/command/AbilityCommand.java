package net.threetag.palladium.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AbilityCommand {

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_OWN_POWERS = (context, builder) -> {
        List<ResourceLocation> powers = Lists.newArrayList();
        Collection<? extends Entity> entities;
        try {
            context.getArgument("entities", EntitySelector.class);
            entities = EntityArgument.getEntities(context, "entities");
        } catch (Exception e) {
            entities = Collections.singleton(context.getSource().getPlayerOrException());
        }
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                var manager = PowerUtil.getPowerHandler(living).orElse(new EntityPowerHandler(living));

                for (PowerHolder holder : manager.getPowerHolders().values()) {
                    for (AbilityInstance<?> instance : holder.getAbilities().values()) {
                        if (instance.getAbility().getConditions().isBuyable()) {
                            if (!powers.contains(holder.getPowerId())) {
                                powers.add(holder.getPowerId());
                            }
                            break;
                        }
                    }
                }
            }
        }

        return SharedSuggestionProvider.suggestResource(powers, builder);
    };

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_ABILITIES = (context, builder) -> {
        List<String> abilities = Lists.newArrayList();
        Holder<Power> power = null;
        try {
            context.getArgument("power", ResourceLocation.class);
            power = ResourceArgument.getResource(context, "power", PalladiumRegistryKeys.POWER);
        } catch (Exception ignored) {
        }

        if (power != null) {
            for (Ability ability : power.value().getAbilities().values()) {
                if (ability.getConditions().isBuyable()) {
                    abilities.add(ability.getKey());
                }
            }
        }

        return SharedSuggestionProvider.suggest(abilities, builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("ability").requires((player) -> {
                    return player.hasPermission(2);
                })
                .then(Commands.literal("lock")
                        .then(Commands.argument("entities", EntityArgument.entities())
                                .then(Commands.argument("power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).suggests(SUGGEST_OWN_POWERS)
                                        .then(Commands.argument("ability", StringArgumentType.word()).suggests(SUGGEST_ABILITIES)
                                                .executes(c -> {
                                                    return lockAbility(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER), StringArgumentType.getString(c, "ability"), true);
                                                })))))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("entities", EntityArgument.entities())
                                .then(Commands.argument("power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).suggests(SUGGEST_OWN_POWERS)
                                        .then(Commands.argument("ability", StringArgumentType.word()).suggests(SUGGEST_ABILITIES)
                                                .executes(c -> {
                                                    return lockAbility(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER), StringArgumentType.getString(c, "ability"), false);
                                                }))))));
    }

    public static int lockAbility(CommandSourceStack source, Collection<? extends Entity> entities, Holder<Power> power, String abilityKey, boolean locking) {
        Ability configuration = power.value().getAbilities().values().stream().filter(c -> c.getKey().equals(abilityKey)).findFirst().orElse(null);
        var powerId = source.getServer().registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(power.value());

        if (configuration == null || !configuration.getConditions().isBuyable()) {
            source.sendFailure(Component.translatable("commands.ability.error.notUnlockable", abilityKey, powerId));
            return 0;
        }

        int i = 0;

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                var holder = PowerUtil.getPowerHandler(living).orElse(new EntityPowerHandler(living)).getPowerHolder(powerId);

                if (holder != null) {
                    var ability = holder.getAbilities().get(abilityKey);

                    if (ability != null) {
                        ability.set(PalladiumDataComponents.Abilities.BOUGHT.get(), !locking);
                        i++;
                    }
                } else {
                    source.sendFailure(Component.translatable("commands.ability.error.doesntHavePower"));
                }
            } else {
                source.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        int finalI = i;
        source.sendSuccess(() -> Component.translatable("commands.ability." + (locking ? "locking" : "unlocking") + ".success", abilityKey, powerId, finalI), true);

        return i;
    }

}
