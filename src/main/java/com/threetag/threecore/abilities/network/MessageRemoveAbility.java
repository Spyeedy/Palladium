package com.threetag.threecore.abilities.network;

import com.threetag.threecore.abilities.AbilityHelper;
import com.threetag.threecore.abilities.IAbilityContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRemoveAbility {

    public int entityID;
    public ResourceLocation containerId;
    public String abilityId;

    public MessageRemoveAbility(int entityID, ResourceLocation containerId, String abilityId) {
        this.entityID = entityID;
        this.containerId = containerId;
        this.abilityId = abilityId;
    }

    public MessageRemoveAbility(PacketBuffer buffer) {
        this.entityID = buffer.readInt();
        this.containerId = new ResourceLocation(buffer.readString(64));
        this.abilityId = buffer.readString(32);
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeString(this.containerId.toString());
        buffer.writeString(this.abilityId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().world.getEntityByID(this.entityID);
            if (entity != null && entity instanceof EntityLivingBase) {
                IAbilityContainer container = AbilityHelper.getAbilityContainerFromId((EntityLivingBase) entity, this.containerId);

                if (container != null) {
                    container.removeAbility((EntityLivingBase) entity, this.abilityId);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
