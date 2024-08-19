package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.icon.Icon;
import net.threetag.palladium.util.icon.ItemIcon;
import org.jetbrains.annotations.Nullable;

public class AbilityProperties {

    public static final AbilityProperties BASIC = new AbilityProperties();
    private static final Component EMPTY_TITLE = Component.empty();

    public static final Codec<AbilityProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.optionalFieldOf("title", EMPTY_TITLE).forGetter(AbilityProperties::getTitle),
                    Icon.CODEC.optionalFieldOf("icon", new ItemIcon(Items.BARRIER)).forGetter(AbilityProperties::getIcon),
                    AbilityDescription.CODEC.optionalFieldOf("description", AbilityDescription.EMPTY).forGetter(AbilityProperties::getDescription),
                    AbilityColor.CODEC.optionalFieldOf("color", AbilityColor.GRAY).forGetter(AbilityProperties::getColor),
                    Codec.BOOL.optionalFieldOf("hidden_in_gui", false).forGetter(AbilityProperties::isHiddenInGUI),
                    Codec.BOOL.optionalFieldOf("hidden_in_bar", false).forGetter(AbilityProperties::isHiddenInBar),
                    Codec.intRange(-1, Integer.MAX_VALUE).optionalFieldOf("list_index", -1).forGetter(AbilityProperties::getListIndex),
                    CodecUtils.VEC2_CODEC.optionalFieldOf("gui_position", Vec2.MIN).forGetter(AbilityProperties::getGuiPosition),
                    AnimationTimerSetting.CODEC.optionalFieldOf("animation_timer", null).forGetter(AbilityProperties::getAnimationTimerSetting)
            ).apply(instance, AbilityProperties::new));

    private Component title = Component.empty();
    private Icon icon = null;
    private AbilityDescription description = null;
    private AbilityColor color = AbilityColor.GRAY;
    private boolean hiddenInGUI = false;
    private boolean hiddenInBar = false;
    private int listIndex = -1;
    private Vec2 guiPosition = null;
    private AnimationTimerSetting animationTimerSetting = null;

    private AbilityProperties() {

    }

    private AbilityProperties(Component title, Icon icon, AbilityDescription description, AbilityColor color, boolean hiddenInGUI, boolean hiddenInBar, int listIndex, Vec2 guiPosition, AnimationTimerSetting animationTimerSetting) {
        this.title = title;
        this.icon = icon;
        this.description = description;
        this.color = color;
        this.hiddenInGUI = hiddenInGUI;
        this.hiddenInBar = hiddenInBar;
        this.listIndex = listIndex;
        this.guiPosition = guiPosition;
        this.animationTimerSetting = animationTimerSetting;
    }

    public AbilityProperties title(Component title) {
        this.title = title;
        return this;
    }

    public AbilityProperties icon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public AbilityProperties description(AbilityDescription description) {
        this.description = description;
        return this;
    }

    public AbilityProperties color(AbilityColor color) {
        this.color = color;
        return this;
    }

    public AbilityProperties hiddenInGUI(boolean hiddenInGUI) {
        this.hiddenInGUI = hiddenInGUI;
        return this;
    }

    public AbilityProperties hiddenInBar(boolean hiddenInBar) {
        this.hiddenInBar = hiddenInBar;
        return this;
    }

    public AbilityProperties listIndex(int listIndex) {
        this.listIndex = listIndex;
        return this;
    }

    public AbilityProperties guiPosition(Vec2 guiPosition) {
        this.guiPosition = guiPosition;
        return this;
    }

    public AbilityProperties animationTimer(AnimationTimerSetting setting) {
        this.animationTimerSetting = setting;
        return this;
    }

    @Nullable
    public Component getTitle() {
        return this.title == EMPTY_TITLE ? null : title;
    }

    public Icon getIcon() {
        return icon;
    }

    public AbilityDescription getDescription() {
        return description;
    }

    public AbilityColor getColor() {
        return color;
    }

    public boolean isHiddenInGUI() {
        return hiddenInGUI;
    }

    public boolean isHiddenInBar() {
        return hiddenInBar;
    }

    public int getListIndex() {
        return listIndex;
    }

    public Vec2 getGuiPosition() {
        return guiPosition;
    }

    public AnimationTimerSetting getAnimationTimerSetting() {
        return animationTimerSetting;
    }
}
