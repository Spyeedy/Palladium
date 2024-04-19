package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.condition.BuyableCondition;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

public class RequestAbilityBuyScreenMessage extends MessageC2S {

    private final AbilityReference reference;

    public RequestAbilityBuyScreenMessage(AbilityReference reference) {
        this.reference = reference;
    }

    public RequestAbilityBuyScreenMessage(FriendlyByteBuf buf) {
        this.reference = AbilityReference.fromBuffer(buf);
    }

    @NotNull
    @Override
    public MessageType getType() {
        return PalladiumNetwork.REQUEST_ABILITY_BUY_SCREEN;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        this.reference.toBuffer(buf);
    }

    @Override
    public void handle(MessageContext context) {
        AbilityInstance entry = this.reference.getEntry(context.getPlayer());

        if (entry != null) {
            var buyableCondition = entry.getConfiguration().findBuyCondition();

            if (buyableCondition != null && !entry.getProperty(BuyableCondition.BOUGHT)) {
                for (AbilityInstance parentEntry : Ability.findParentsWithinHolder(entry.getConfiguration(), entry.getHolder())) {
                    if (!parentEntry.isUnlocked()) {
                        return;
                    }
                }

                new OpenAbilityBuyScreenMessage(this.reference, buyableCondition.createData(), buyableCondition.isAvailable(context.getPlayer())).send(context.getPlayer());
            }
        }
    }
}
