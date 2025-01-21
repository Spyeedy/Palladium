package net.threetag.palladium.client.screen.abilitybar;

import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.client.screen.component.CompoundUiComponent;
import net.threetag.palladium.client.screen.component.IconUiComponent;
import net.threetag.palladium.client.screen.component.UiAlignment;
import net.threetag.palladium.client.screen.component.UiComponent;
import net.threetag.palladium.util.Easing;

import java.util.ArrayList;
import java.util.List;

public class PowerIndicatorComponent implements UiComponent {

    private final CompoundUiComponent keyAndIcon;

    public PowerIndicatorComponent(AbilityBar.AbilityList abilityList, boolean showButton) {
        List<UiComponent> componentList = new ArrayList<>();

        if (showButton) {
            componentList.add(new SwitchKeyComponent(PalladiumKeyMappings.SWITCH_ABILITY_LIST.getTranslatedKeyMessage()));
        }

        componentList.add(new IconUiComponent(abilityList.getPowerHolder().getPower().value().getIcon()));
        this.keyAndIcon = new CompoundUiComponent(componentList, false);
        this.keyAndIcon.padding = 4;
        this.keyAndIcon.center = true;
    }

    @Override
    public int getWidth() {
        return 52;
    }

    @Override
    public int getHeight() {
        return 28;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, x, y, getU(alignment), getV(alignment), this.getWidth(), this.getHeight(), 256, 256);
        int width = this.keyAndIcon.getWidth();
        int height = this.keyAndIcon.getHeight();
        int offsetX = alignment.isLeft() ? 0 : 3;
        int offsetY = alignment.isTop() ? 0 : 3;

        this.keyAndIcon.render(
                minecraft,
                gui,
                deltaTracker,
                x + offsetX + ((this.getWidth() - 3 - width) / 2),
                y + offsetY + ((this.getHeight() - 3 - height) / 2),
                alignment
        );
    }

    private static int getU(UiAlignment alignment) {
        return switch (alignment) {
            case TOP_LEFT, BOTTOM_LEFT -> 52;
            case TOP_RIGHT, BOTTOM_RIGHT -> 0;
        };
    }

    private static int getV(UiAlignment alignment) {
        return switch (alignment) {
            case TOP_LEFT, TOP_RIGHT -> 28;
            case BOTTOM_LEFT, BOTTOM_RIGHT -> 0;
        };
    }

    public static class SwitchKeyComponent implements UiComponent {

        private final Component keyText;

        public SwitchKeyComponent(Component keyText) {
            this.keyText = keyText;
        }

        @Override
        public int getWidth() {
            return Minecraft.getInstance().font.width(this.keyText) + 3 + 8;
        }

        @Override
        public int getHeight() {
            return Minecraft.getInstance().font.lineHeight;
        }

        @Override
        public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
            gui.drawString(minecraft.font, this.keyText, x, y, 0xFFFFFFFF, false);

            gui.pose().pushPose();
            gui.pose().translate(x + minecraft.font.width(this.keyText) + 3 + 4, y + 4, 0);

            if (AbilityBar.KEY_ROTATION > 0) {
                gui.pose().mulPose(
                        Axis.ZP.rotationDegrees(
                                Easing.inOutCubic((AbilityBar.KEY_ROTATION - deltaTracker.getRealtimeDeltaTicks()) / 10F) * 360F
                                        * (AbilityBar.KEY_ROTATION_FORWARD ? -1 : 1)
                        )
                );
            }

            gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, -4, -4, 78, AbilityBar.KEY_ROTATION_FORWARD ? 56 : 64, 8, 8, 256, 256);
            gui.pose().popPose();
        }
    }
}
