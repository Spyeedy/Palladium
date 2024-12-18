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
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class SuperpowerCommand {

    public static final String QUERY_SUCCESS = "commands.superpower.query.success";

    // TODO

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
            if (entity instanceof LivingEntity livingEntity) {
                SuperpowerUtil.getSuperpowers(livingEntity).stream().map(powerHolder -> powerHolder.unwrapKey().orElseThrow().location()).forEach(id -> {
                    var allId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "all");
                    if (!superpowers.contains(allId)) {
                        superpowers.add(allId);
                    }

                    if (!superpowers.contains(id)) {
                        superpowers.add(id);
                    }
                });
            }
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
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all", ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all", ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        }))))));
    }

    private static int querySuperpowers(CommandSourceStack source, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {

            MutableComponent powerList = null;
            AtomicInteger i = new AtomicInteger();

            for (Holder<Power> power : SuperpowerUtil.getSuperpowers(livingEntity)) {
                var powerName = power.value().getName().copy().setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(power.unwrapKey().orElseThrow().location().toString()))));

                if (powerList == null) {
                    powerList = powerName;
                } else {
                    powerList.append(", ").append(powerName);
                }

                i.getAndIncrement();
            }

            if (i.get() == 0) {
                source.sendFailure(Component.translatable("commands.superpower.error.noSuperpowers", livingEntity.getDisplayName()));
            } else {
                MutableComponent finalPowerList = powerList;
                source.sendSuccess(() -> Component.translatable(QUERY_SUCCESS, livingEntity.getDisplayName(), finalPowerList), true);
            }

            return i.get();
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
                SuperpowerUtil.setSuperpower(livingEntity, power);
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
                if (SuperpowerUtil.addSuperpower(livingEntity, superpower)) {
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
        try {
            int i = 0;
            boolean no = false;
            Predicate<ResourceLocation> predicate = filter.equalsIgnoreCase("all") ? id -> true : (filter.endsWith(":all") ? id -> id.getNamespace().equals(filter.split(":")[0]) : id -> id.equals(ResourceLocation.parse(filter)));

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    // TODO
                    SuperpowerUtil.setSuperpower(livingEntity, null);
                    i++;
//                if (SuperpowerUtil.removeSuperpower(livingEntity, predicate) > 0) {
//                    i++;
//                } else if (entities.size() == 1) {
//                    no = true;
//                    commandSource.sendFailure(Component.translatable("commands.superpower.error.doesntHaveSuperpower", entity.getDisplayName()));
//                }
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
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int replaceSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, String replacedFilter, Holder<Power> replacingPower) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;
        Predicate<ResourceLocation> predicate = replacedFilter.equalsIgnoreCase("all") ? id -> true : (replacedFilter.endsWith(":all") ? id -> id.getNamespace().equals(replacedFilter.split(":")[0]) : id -> id.equals(ResourceLocation.parse(replacedFilter)));

        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            if (entity instanceof LivingEntity livingEntity) {
                // TODO
//                if (SuperpowerUtil.removeSuperpowersByIds(livingEntity, predicate) > 0 && SuperpowerUtil.addSuperpower(livingEntity, replacingPower.value())) {
//                    i++;
//                } else if (entities.size() == 1) {
//                    no = true;
//                    commandSource.sendFailure(Component.translatable("commands.superpower.error.doesntHaveSuperpower", entity.getDisplayName()));
//                }
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