package net.threetag.palladium.client.gui.screen.abilitybar;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.gui.component.UiComponent;
import net.threetag.palladium.power.ability.AbilityInstance;

public class SimplifiedPowerComponent implements UiComponent {

    private final AbilityInstance<?> abilityInstance;

    public SimplifiedPowerComponent(AbilityInstance<?> abilityInstance) {
        this.abilityInstance = abilityInstance;
    }

    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 24;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, x, y, 0, 168, 24, 24, 256, 256);
        AbilityListComponent.renderAbility(minecraft, gui, x + 3, y + 3, alignment, this.abilityInstance, 0);
    }
}
