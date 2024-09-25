package net.threetag.palladium.mixin;

import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(method = "updateEntireScoreboard", at = @At("RETURN"))
    protected void updateEntireScoreboard(ServerScoreboard scoreboard, ServerPlayer player, CallbackInfo ci) {
//        for (String tracked : TrackedScoresManager.INSTANCE.getTracked()) {
//            var objective = scoreboard.getObjective(tracked);
//
//            if (objective != null && scoreboard.getObjectiveDisplaySlotCount(objective) == 0) {
//                player.connection.send(new ClientboundSetObjectivePacket(objective, 0));
//
//                for (PlayerScoreEntry score : scoreboard.listPlayerScores(objective)) {
//                    player.connection.send(new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, score.value(), Optional.of(score.display()), Optional.of(score.numberFormatOverride())));
//                }
//            }
//
//            scoreboard.trackedObjectives.add(objective);
//        }
    }

}
