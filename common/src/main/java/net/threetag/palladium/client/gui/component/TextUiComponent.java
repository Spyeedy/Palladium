package net.threetag.palladium.client.gui.component;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TextUiComponent implements UiComponent {

    private final Component text;
    public boolean outline;
    private int width;

    public TextUiComponent(Component text) {
        this.text = text;
        this.width = Minecraft.getInstance().font.width(this.text) - 1;
    }

    public TextUiComponent(Component text, boolean outline) {
        this(text);
        this.outline = outline;

        if (outline) {
            this.width += 2;
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight - (this.outline ? 0 : 2);
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        x += this.outline ? 1 : 0;
        y += this.outline ? 1 : 0;

        if (this.outline) {
            gui.drawString(minecraft.font, this.text, x + 1, y, 0, false);
            gui.drawString(minecraft.font, this.text, x - 1, y, 0, false);
            gui.drawString(minecraft.font, this.text, x, y + 1, 0, false);
            gui.drawString(minecraft.font, this.text, x, y - 1, 0, false);

            gui.drawString(minecraft.font, this.text, x + 1, y + 1, 0, false);
            gui.drawString(minecraft.font, this.text, x + 1, y - 1, 0, false);
            gui.drawString(minecraft.font, this.text, x - 1, y + 1, 0, false);
            gui.drawString(minecraft.font, this.text, x - 1, y - 1, 0, false);
        }

        gui.drawString(minecraft.font, this.text, x, y, 0xffffff, false);
    }
}
