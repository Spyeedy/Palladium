package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.condition.BuyableCondition;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;

public class BuyAbilityUnlockMessage extends MessageC2S {

    private final AbilityReference reference;

    public BuyAbilityUnlockMessage(AbilityReference reference) {
        this.reference = reference;
    }

    public BuyAbilityUnlockMessage(FriendlyByteBuf buf) {
        this.reference = AbilityReference.fromBuffer(buf);
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.BUY_ABILITY_UNLOCK;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        this.reference.toBuffer(buf);
    }

    @Override
    public void handle(MessageContext context) {
        AbilityInstance entry = this.reference.getInstance(context.getPlayer());

        if (entry != null) {
            var buyableCondition = entry.getConfiguration().findBuyCondition();

            if (buyableCondition != null && !entry.getProperty(BuyableCondition.BOUGHT) && buyableCondition.isAvailable(context.getPlayer())) {
                buyableCondition.buy(context.getPlayer(), entry);
            }
        }
    }
}
