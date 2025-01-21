package net.threetag.palladium.client.screen.abilitybar;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.client.screen.component.UiAlignment;
import net.threetag.palladium.client.screen.component.UiComponent;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;

public class AbilityListComponent implements UiComponent {

    private final AbilityBar.AbilityList abilityList;

    public AbilityListComponent(AbilityBar.AbilityList abilityList) {
        this.abilityList = abilityList;
    }

    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 112;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, x, y, 0, 56, this.getWidth(), this.getHeight(), 256, 256);

        for (int i = 0; i < AbilityBar.AbilityList.MAX_ABILITIES; i++) {
            var ability = this.abilityList.getAbility(i);
            int abilityX = x + 3;
            int abilityY = y + 3 + (i * 22);

            if (ability != null) {
                if (ability.isUnlocked()) {
                    if (ability.isEnabled()) {
                        gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, abilityX, abilityY, 42, 56, 18, 18, 256, 256);
                    } else {
                        gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, abilityX, abilityY, 24, 56, 18, 18, 256, 256);
                    }

                    // Ability Icon
                    ability.getAbility().getProperties().getIcon().draw(minecraft, gui, DataContext.forAbility(minecraft.player, ability), abilityX + 1, abilityY + 1);

                    // Cooldown
                    if (ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler
                            && handler.displayCooldown(ability)) {
                        float percentage = handler.getCooldownPercentage(ability);

                        gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, abilityX, abilityY, 60, 74, (int) (18 * percentage), 18, 256, 256);
                    }

                    // Key Bind (inside)
                    if (PalladiumConfig.ABILITY_BAR_KEY_BIND_DISPLAY == AbilityKeyBindDisplay.INSIDE &&
                            ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                        var key = handler.getKeyBindType().getDisplayedKey(ability, i);
                        gui.pose().pushPose();
                        gui.pose().translate(0, 0, 500);

                        // Outline
                        gui.drawString(minecraft.font, key, abilityX + 18 - minecraft.font.width(key), abilityY + 9, 0, false);
                        gui.drawString(minecraft.font, key, abilityX + 20 - minecraft.font.width(key), abilityY + 9, 0, false);
                        gui.drawString(minecraft.font, key, abilityX + 19 - minecraft.font.width(key), abilityY + 8, 0, false);
                        gui.drawString(minecraft.font, key, abilityX + 19 - minecraft.font.width(key), abilityY + 10, 0, false);
                        gui.drawString(minecraft.font, key, abilityX + 18 - minecraft.font.width(key), abilityY + 8, 0, false);
                        gui.drawString(minecraft.font, key, abilityX + 18 - minecraft.font.width(key), abilityY + 10, 0, false);
                        gui.drawString(minecraft.font, key, abilityX + 20 - minecraft.font.width(key), abilityY + 8, 0, false);
                        gui.drawString(minecraft.font, key, abilityX + 20 - minecraft.font.width(key), abilityY + 10, 0, false);

                        gui.drawString(minecraft.font, key, abilityX + 19 - minecraft.font.width(key), abilityY + 9, 0xffffff, false);
                        gui.pose().popPose();
                    }

                    // Name / Key Bind (outside)
                    boolean chatOpen = minecraft.screen instanceof ChatScreen;
                    Component displayedText = null;

                    if (PalladiumConfig.ABILITY_BAR_KEY_BIND_DISPLAY == AbilityKeyBindDisplay.OUTSIDE) {
                        if (chatOpen) {
                            displayedText = ability.getAbility().getDisplayName();
                        } else if (ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                            displayedText = handler.getKeyBindType().getDisplayedKey(ability, i);
                        }
                    } else if (chatOpen) {
                        displayedText = ability.getAbility().getDisplayName();
                    }

                    if (displayedText != null) {
                        int width = minecraft.font.width(displayedText);
                        gui.fill(
                                abilityX + (alignment.isLeft() ? 21 : -width - 10),
                                abilityY + 3,
                                abilityX + (alignment.isLeft() ? 28 : -width - 3) + width,
                                abilityY + 5 + 10,
                                0x80000000
                        );
                        gui.drawString(minecraft.font, displayedText, abilityX + (alignment.isLeft() ? 26 : -width - 8), abilityY + 5, 0xffffffff, false);
                    }
                } else {
                    gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, abilityX, abilityY, 24, 74, 18, 18, 256, 256);
                    gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, abilityX, abilityY, 42, 74, 18, 18, 256, 256);
                }

                // Ability Color
                var color = ability.getAbility().getProperties().getColor();
                gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, abilityX - 3, abilityY - 3, color.getU(), color.getV(), 24, 24, 256, 256);
            } else {
                gui.blit(RenderType::guiTextured, AbilityBar.TEXTURE, abilityX, abilityY, 60, 56, 18, 18, 256, 256);
            }
        }
    }
}
