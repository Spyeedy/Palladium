package net.threetag.palladium.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.SuperpowerUtil;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class SuperpowerCommand {

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_OWN_POWERS_ALL = (context, builder) -> {
        List<ResourceLocation> superpowers = Lists.newArrayList();
        Collection<? extends Entity> entities;
        try {
            context.getArgument("entities", EntitySelector.class);
            entities = EntityArgument.getEntities(context, "entities");
        } catch (Exception e) {
            entities = Collections.singleton(context.getSource().getPlayerOrException());
        }
        for (Entity entity : entities) {
//            for (ResourceLocation id : PalladiumProperties.SUPERPOWER_IDS.get(entity)) {
//                var allId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "all");
//                if (!superpowers.contains(allId)) {
//                    superpowers.add(allId);
//                }
//
//                if (!superpowers.contains(id)) {
//                    superpowers.add(id);
//                }
//            }
        }

        return SharedSuggestionProvider.suggestResource(superpowers, builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("superpower").requires((player) -> {
                    return player.hasPermission(2);
                })

                .then(Commands.literal("query")
                        .then(Commands.argument("entity", EntityArgument.entity()).executes(c -> {
                            return querySuperpowers(c.getSource(), EntityArgument.getEntity(c, "entity"));
                        })))

                .then(Commands.literal("set")
                        .then(Commands.argument("power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                    return setSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER));
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return setSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER));
                                }))))

                .then(Commands.literal("add")
                        .then(Commands.argument("power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                    return addSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER));
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return addSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER));
                                }))))

                .then(Commands.literal("remove")
                        .then(Commands.argument("power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).suggests(SUGGEST_OWN_POWERS_ALL).executes(c -> {
                                    return removeSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceLocationArgument.getId(c, "power").toString());
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceLocationArgument.getId(c, "power").toString());
                                })))
                        .then(Commands.literal("*").executes(c -> {
                                    return removeSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all");
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all");
                                })))
                        .then(Commands.literal("all").executes(c -> {
                                    return removeSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all");
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all");
                                }))))

                .then(Commands.literal("replace")
                        .then(Commands.argument("replaced_power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).suggests(SUGGEST_OWN_POWERS_ALL)
                                .then(Commands.argument("replacing_power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceLocationArgument.getId(c, "replaced_power").toString(), ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceLocationArgument.getId(c, "replaced_power").toString(), ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        }))))
                        .then(Commands.literal("*")
                                .then(Commands.argument("replacing_power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all", ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all", ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        }))))
                        .then(Commands.literal("all")
                                .then(Commands.argument("replacing_power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all",ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all", ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        }))))));
    }

    private static int querySuperpowers(CommandSourceStack source, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            StringBuilder result = new StringBuilder();
            int i = 0;
            for (ResourceLocation id : SuperpowerUtil.getSuperpowerIds(livingEntity)) {
                result.append(id.toString()).append(", ");
                i++;
            }

            if (i == 0) {
                source.sendFailure(Component.translatable("commands.superpower.error.noSuperpowers", livingEntity.getDisplayName()));
            } else {
                source.sendSuccess(() -> Component.translatable("commands.superpower.query.success", livingEntity.getDisplayName(), result.substring(0, result.length() - 3)), true);
            }

            return i;
        } else {
            source.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            return 0;
        }
    }

    public static int setSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, Holder<Power> power) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity livingEntity) {
                SuperpowerUtil.setSuperpower(livingEntity, power.value());
                i++;
            } else {
                commandSource.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        if (i == 1) {
            commandSource.sendSuccess(() -> Component.translatable("commands.superpower.success.entity.single", (entities.iterator().next()).getDisplayName(), power.value().getName()), true);
        } else {
            int finalI = i;
            commandSource.sendSuccess(() -> Component.translatable("commands.superpower.success.entity.multiple", finalI, power.value().getName()), true);
        }

        return i;
    }

    public static int addSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, Holder<Power> superpower) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity livingEntity) {
                if (SuperpowerUtil.addSuperpower(livingEntity, superpower.value())) {
                    i++;
                } else if (entities.size() == 1) {
                    no = true;
                    commandSource.sendFailure(Component.translatable("commands.superpower.error.alreadyHasSuperpower", entity.getDisplayName()));
                }
            } else {
                commandSource.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        if (!no) {
            if (i == 1) {
                commandSource.sendSuccess(() -> Component.translatable("commands.superpower.success.entity.single", (entities.iterator().next()).getDisplayName(), superpower.value().getName()), true);
            } else {
                int finalI = i;
                commandSource.sendSuccess(() -> Component.translatable("commands.superpower.success.entity.multiple", finalI, superpower.value().getName()), true);
            }
        }

        return i;
    }

    public static int removeSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, String filter) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;
        Predicate<ResourceLocation> predicate = filter.equalsIgnoreCase("all") ? id -> true : (filter.endsWith(":all") ? id -> id.getNamespace().equals(filter.split(":")[0]) : id -> id.equals(ResourceLocation.parse(filter)));

        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            if (entity instanceof LivingEntity livingEntity) {
                if (SuperpowerUtil.removeSuperpowersByIds(livingEntity, predicate) > 0) {
                    i++;
                } else if (entities.size() == 1) {
                    no = true;
                    commandSource.sendFailure(Component.translatable("commands.superpower.error.doesntHaveSuperpower", entity.getDisplayName()));
                }
            } else {
                commandSource.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        if (!no) {
            if (i == 1) {
                commandSource.sendSuccess(() -> Component.translatable("commands.superpower.remove.success.entity.single", (entities.iterator().next()).getDisplayName()), true);
            } else {
                int finalI = i;
                commandSource.sendSuccess(() -> Component.translatable("commands.superpower.remove.success.entity.multiple", finalI), true);
            }
        }

        return i;
    }

    public static int replaceSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, String replacedFilter, Holder<Power> replacingPower) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;
        Predicate<ResourceLocation> predicate = replacedFilter.equalsIgnoreCase("all") ? id -> true : (replacedFilter.endsWith(":all") ? id -> id.getNamespace().equals(replacedFilter.split(":")[0]) : id -> id.equals(ResourceLocation.parse(replacedFilter)));

        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            if (entity instanceof LivingEntity livingEntity) {
                if (SuperpowerUtil.removeSuperpowersByIds(livingEntity, predicate) > 0 && SuperpowerUtil.addSuperpower(livingEntity, replacingPower.value())) {
                    i++;
                } else if (entities.size() == 1) {
                    no = true;
                    commandSource.sendFailure(Component.translatable("commands.superpower.error.doesntHaveSuperpower", entity.getDisplayName()));
                }
            } else {
                commandSource.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        if (!no) {
            if (i == 1) {
                commandSource.sendSuccess(() -> Component.translatable("commands.superpower.replace.success.entity.single", (entities.iterator().next()).getDisplayName()), true);
            } else {
                int finalI = i;
                commandSource.sendSuccess(() -> Component.translatable("commands.superpower.replace.success.entity.multiple", finalI), true);
            }
        }

        return i;
    }

}