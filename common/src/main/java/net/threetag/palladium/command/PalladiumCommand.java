package net.threetag.palladium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.function.BiConsumer;

public class PalladiumCommand {

    public static final Event<BiConsumer<LiteralArgumentBuilder<CommandSourceStack>, CommandBuildContext>> COMMAND_CALLBACK = EventFactory.createLoop();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("palladium");
        COMMAND_CALLBACK.invoker().accept(root, context);
        dispatcher.register(root);
    }

}
