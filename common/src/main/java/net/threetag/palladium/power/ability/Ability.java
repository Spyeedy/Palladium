package net.threetag.palladium.power.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.DocumentedPropertyManager;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.icon.Icon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.*;

import java.util.Comparator;
import java.util.stream.Collectors;

public class Ability implements DocumentedPropertyManager {

    public static final PalladiumProperty<Component> TITLE = PalladiumPropertyBuilder.create("title", PalladiumPropertyType.COMPONENT).configurable("Allows you to set a custom title for this ability", false).build();
    public static final PalladiumProperty<Icon> ICON = PalladiumPropertyBuilder.create("icon", PalladiumPropertyType.ICON).configurable("Icon for the ability", false, new ItemIcon(Items.BLAZE_ROD)).build();
    public static final PalladiumProperty<AbilityDescription> DESCRIPTION = PalladiumPropertyBuilder.create("description", PalladiumPropertyType.ABILITY_DESCRIPTION).configurable("Description of the ability. Visible in ability menu", false).build();
    public static final PalladiumProperty<AbilityColor> COLOR = PalladiumPropertyBuilder.create("bar_color", PalladiumPropertyType.ABILITY_COLOR).configurable("Changes the color of the ability in the ability bar", false, AbilityColor.LIGHT_GRAY).build();
    public static final PalladiumProperty<Boolean> HIDDEN_IN_GUI = PalladiumPropertyBuilder.create("hidden", PalladiumPropertyType.BOOLEAN).sync(SyncOption.SELF).configurable("Determines if the ability is visible in the powers screen", false, false).build();
    public static final PalladiumProperty<Boolean> HIDDEN_IN_BAR = PalladiumPropertyBuilder.create("hidden_in_bar", PalladiumPropertyType.BOOLEAN).sync(SyncOption.SELF).configurable("Determines if the ability is visible in the ability bar on your screen", false, false).build();
    public static final PalladiumProperty<Integer> LIST_INDEX = PalladiumPropertyBuilder.create("list_index", PalladiumPropertyType.INTEGER).configurable("Determines the list index for custom ability lists. Starts at 0. Going beyond 4 (which is the 5th place in the ability) will start a new list. Keeping it at -1 will automatically arrange the abilities.", false, -1).build();
    public static final PalladiumProperty<Vec2> GUI_POSITION = PalladiumPropertyBuilder.create("gui_position", PalladiumPropertyType.VEC2).configurable("Position of the ability in the ability menu. Leave null for automatic positioning. 0/0 is center", false).build();

    final PropertyManager propertyManager = new PropertyManager();
    private String documentationDescription;

    public Ability() {
        this.withProperty(TITLE, ICON, DESCRIPTION, COLOR, HIDDEN_IN_GUI, HIDDEN_IN_BAR, LIST_INDEX, GUI_POSITION);
    }

    public void registerUniqueProperties(PropertyManager manager) {

    }

    public boolean isEffect() {
        return false;
    }

    public boolean isExperimental() {
        return false;
    }

    public void tick(LivingEntity entity, AbilityInstance entry, PowerHolder holder, boolean enabled) {

    }

    public void firstTick(LivingEntity entity, AbilityInstance entry, PowerHolder holder, boolean enabled) {

    }

    public void lastTick(LivingEntity entity, AbilityInstance entry, PowerHolder holder, boolean enabled) {

    }

    public final Ability withProperty(PalladiumProperty<?>... properties) {
        for (PalladiumProperty<?> property : properties) {
            this.propertyManager.register(property);
        }
        return this;
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(Palladium.id("abilities"), "Abilities")
                .add(HTMLBuilder.heading("Abilities"))
                .addDocumentationSettings(PalladiumRegistries.ABILITY.stream().filter(ab -> !ab.isExperimental()).sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }

    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    @Override
    public ResourceLocation getId() {
        return PalladiumRegistries.ABILITY.getKey(this);
    }

    public Ability setDocumentationDescription(String documentationDescription) {
        this.documentationDescription = documentationDescription;
        return this;
    }

    public String getDocumentationDescription() {
        return this.documentationDescription;
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        DocumentedPropertyManager.super.generateDocumentation(builder);
        builder.setTitle(this.getId().getPath());

        var desc = this.getDocumentationDescription();
        if (desc != null && !desc.isEmpty()) {
            builder.setDescription(desc);
        }
    }

    public void postParsing(AbilityConfiguration configuration) {
    }
}
