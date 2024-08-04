package net.threetag.palladium.power;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.StringRepresentable;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.energybar.EnergyBarConfiguration;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.icon.Icon;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collections;
import java.util.Map;

public class Power {

    public static final Codec<Power> CODEC = RecordCodecBuilder.create((instance) -> instance
            .group(
                    ComponentSerialization.CODEC.fieldOf("name").forGetter(Power::getName),
                    Icon.CODEC.fieldOf("icon").forGetter(Power::getIcon),
                    TextureReference.CODEC.optionalFieldOf("background", null).forGetter(Power::getBackground),
                    TextureReference.CODEC.optionalFieldOf("ability_bar_texture", null).forGetter(Power::getBackground),
                    CodecUtils.COLOR_CODEC.optionalFieldOf("primary_color", new Color(210, 112, 49)).forGetter(Power::getPrimaryColor),
                    CodecUtils.COLOR_CODEC.optionalFieldOf("secondary_color", new Color(126, 97, 86)).forGetter(Power::getSecondaryColor),
                    Codec.BOOL.optionalFieldOf("persistent_data", false).forGetter(Power::hasPersistentData),
                    Codec.BOOL.optionalFieldOf("hidden", false).forGetter(Power::isHidden),
                    GuiDisplayType.CODEC.optionalFieldOf("gui_display_type", GuiDisplayType.AUTO).forGetter(Power::getGuiDisplayType),
                    Codec.unboundedMap(Codec.STRING, AbilityConfiguration.CODEC).optionalFieldOf("abilities", Collections.emptyMap()).forGetter(Power::getAbilities),
                    Codec.unboundedMap(Codec.STRING, EnergyBarConfiguration.CODEC).optionalFieldOf("energy_bars", Collections.emptyMap()).forGetter(Power::getEnergyBars)
            )
            .apply(instance, Power::new));


    private final Component name;
    private final Icon icon;
    private final Map<String, AbilityConfiguration> abilities;
    private final Map<String, EnergyBarConfiguration> energyBars;
    private final TextureReference background;
    private final TextureReference abilityBar;
    private final Color primaryColor, secondaryColor;
    private final boolean persistentData;
    private final boolean hidden;
    private final GuiDisplayType guiDisplayType;
    private boolean invalid = false;

    public Power(Component name, Icon icon, TextureReference background, TextureReference abilityBar, Color primaryColor, Color secondaryColor, boolean persistentData, boolean hidden, GuiDisplayType guiDisplayType, Map<String, AbilityConfiguration> abilities, Map<String, EnergyBarConfiguration> energyBars) {
        this.name = name;
        this.icon = icon;
        this.background = background;
        this.abilityBar = abilityBar;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.persistentData = persistentData;
        this.hidden = hidden;
        this.guiDisplayType = guiDisplayType;
        this.abilities = abilities;
        this.energyBars = energyBars;

        for (Map.Entry<String, AbilityConfiguration> e : this.abilities.entrySet()) {
            e.getValue().setKey(e.getKey());
        }

        for (Map.Entry<String, EnergyBarConfiguration> e : this.energyBars.entrySet()) {
            e.getValue().setKey(e.getKey());
        }
    }

    public void invalidate() {
        this.invalid = true;
    }

    public boolean isInvalid() {
        return this.invalid;
    }

    public Component getName() {
        return this.name;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public Map<String, AbilityConfiguration> getAbilities() {
        return this.abilities;
    }

    public Map<String, EnergyBarConfiguration> getEnergyBars() {
        return this.energyBars;
    }

    public TextureReference getBackground() {
        return this.background;
    }

    public TextureReference getAbilityBarTexture() {
        return this.abilityBar;
    }

    public Color getPrimaryColor() {
        return this.primaryColor;
    }

    public Color getSecondaryColor() {
        return this.secondaryColor;
    }

    public boolean hasPersistentData() {
        return this.persistentData;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public GuiDisplayType getGuiDisplayType() {
        return this.guiDisplayType;
    }

    public enum GuiDisplayType implements StringRepresentable {

        AUTO("auto"),
        TREE("tree"),
        LIST("list");

        public static final Codec<GuiDisplayType> CODEC = StringRepresentable.fromEnum(GuiDisplayType::values);

        private final String name;

        GuiDisplayType(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

}
