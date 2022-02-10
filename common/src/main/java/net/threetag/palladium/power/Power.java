package net.threetag.palladium.power;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.power.ability.AbilityConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Power {

    private final ResourceLocation id;
    private final Component name;
    private final List<AbilityConfiguration> abilities = new ArrayList<>();
    private boolean invalid = false;

    public Power(ResourceLocation id, Component name) {
        this.id = id;
        this.name = name;
    }

    public void invalidate() {
        this.invalid = true;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public Power addAbility(AbilityConfiguration configuration) {
        this.abilities.add(configuration);
        return this;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Component getName() {
        return name;
    }

    public List<AbilityConfiguration> getAbilities() {
        return abilities;
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeComponent(this.name);
        buf.writeInt(this.abilities.size());
        for(AbilityConfiguration configuration : this.abilities) {
            configuration.toBuffer(buf);
        }
    }

    public static Power fromBuffer(ResourceLocation id, FriendlyByteBuf buf) {
        Power power = new Power(id, buf.readComponent());
        int amount = buf.readInt();

        for(int i = 0; i < amount; i++) {
            power.addAbility(AbilityConfiguration.fromBuffer(buf));
        }

        return power;
    }

    public static Power fromJSON(ResourceLocation id, JsonObject json) {
        Component name = Component.Serializer.fromJson(json.get("name"));
        Power power = new Power(id, name);

        if (GsonHelper.isValidNode(json, "abilities")) {
            JsonObject abilities = GsonHelper.getAsJsonObject(json, "abilities");

            for (String key : abilities.keySet()) {
                power.addAbility(AbilityConfiguration.fromJSON(key, GsonHelper.getAsJsonObject(abilities, key)));
            }
        }

        return power;
    }

}
