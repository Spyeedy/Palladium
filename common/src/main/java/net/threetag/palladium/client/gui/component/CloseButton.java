package net.threetag.palladium.client.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class CloseButton extends Button {

    public CloseButton(int x, int y, OnPress onPress) {
        super(x, y, 7, 7, Component.literal("x"), onPress, Button.DEFAULT_NARRATION);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.isHoveredOrFocused()) {
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 2, this.getY() - 1, 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX(), this.getY() - 1, 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 1, this.getY(), 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 1, this.getY() - 2, 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX(), this.getY() - 2, 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX(), this.getY(), 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 2, this.getY(), 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 2, this.getY() - 2, 0, false);

            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 1, this.getY() - 1, 0xffffff, false);
        } else {
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 1, this.getY() - 1, 4210752, false);
        }
    }
}
