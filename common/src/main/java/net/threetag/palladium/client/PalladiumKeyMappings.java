package net.threetag.palladium.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBar;
import net.threetag.palladium.client.gui.screen.power.PowersScreen;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;
import net.threetag.palladium.power.ability.keybind.AbilityKeyBind;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PalladiumKeyMappings implements ClientRawInputEvent.KeyPressed, ClientTickEvent.Client {

    public static final String CATEGORY = "key.palladium.categories.powers";
    public static final KeyMapping OPEN_EQUIPMENT = new KeyMapping("key.palladium.open_equipment", GLFW.GLFW_KEY_BACKSLASH, "key.categories.gameplay");
    public static final KeyMapping SHOW_POWERS = new KeyMapping("key.palladium.show_powers", InputConstants.UNKNOWN.getValue(), CATEGORY);
    public static final KeyMapping ROTATE_ABILITY_LIST = new KeyMapping("key.palladium.rotate_ability_list", GLFW.GLFW_KEY_X, CATEGORY);
    public static AbilityKeyMapping[] ABILITY_KEYS = new AbilityKeyMapping[5];

    public static void init() {
        KeyMappingRegistry.register(OPEN_EQUIPMENT);
        KeyMappingRegistry.register(SHOW_POWERS);
        KeyMappingRegistry.register(ROTATE_ABILITY_LIST);
        for (int i = 1; i <= ABILITY_KEYS.length; i++) {
            KeyMappingRegistry.register(ABILITY_KEYS[i - 1] = new AbilityKeyMapping("key.palladium.ability_" + i, getKeyForIndex(i), CATEGORY, i));
        }
        var instance = new PalladiumKeyMappings();

        ClientRawInputEvent.KEY_PRESSED.register(instance);
//        InputEvents.MOUSE_SCROLLING.register(instance);
        ClientTickEvent.CLIENT_POST.register(instance);
    }

    private static int getKeyForIndex(int index) {
        return switch (index) {
            case 1 -> GLFW.GLFW_KEY_V;
            case 2 -> GLFW.GLFW_KEY_B;
            case 3 -> GLFW.GLFW_KEY_N;
            case 4 -> GLFW.GLFW_KEY_M;
            case 5 -> GLFW.GLFW_KEY_K;
            default -> -1;
        };
    }

    @Override
    public EventResult keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers) {
        var abilityBar = AbilityBar.INSTANCE.getCurrentList();

        if (abilityBar != null && client.player != null) {
            for (AbilityKeyMapping key : ABILITY_KEYS) {
                if (key.matches(keyCode, 0)) {
                    AbilityInstance<?> entry = abilityBar.getAbility(key.index - 1);

                    if (entry != null && entry.isUnlocked() && entry.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                        if (handler.getKeyBindType() instanceof AbilityKeyBind) {
                            if (action == GLFW.GLFW_PRESS) {
                                if (client.screen == null) {
                                    handler.onKeyPressed(client.player, entry);
                                }
                            } else if (action == GLFW.GLFW_RELEASE) {
                                handler.onKeyReleased(client.player, entry);
                            }
                        }
                    }
                }
            }
        }

        return EventResult.pass();
    }

    @Override
    public void tick(Minecraft client) {
        if (client.player != null) {
            if (client.screen == null) {
                var abilityBar = AbilityBar.INSTANCE.getCurrentList();

                if (abilityBar != null) {
                    while (ROTATE_ABILITY_LIST.consumeClick()) {
                        AbilityBar.INSTANCE.rotateList(!client.player.isCrouching());
                    }
                }

                while (SHOW_POWERS.consumeClick()) {
                    client.setScreen(new PowersScreen());
                }
            }
        }
    }

//    @Override
//    public void keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers) {
//        if (client.player != null && client.screen == null && !client.player.isSpectator()) {
//
//            // TODO
//            // Open Equipment
//            if (OPEN_EQUIPMENT.isDown()) {
//                NetworkManager.get().sendToServer(new ToggleOpenableEquipmentPacket());
//                return;
//            }
//
//            // Scroll ability list
//            if (SWITCH_ABILITY_LIST.isDown()) {
//                AbilityBarRenderer.scroll(!client.player.isCrouching());
//                return;
//            }
//
//            // TODO
//            // Ability keys
//            AbilityBarRenderer.AbilityList list = AbilityBarRenderer.getSelectedList();
//            if (list != null && action != GLFW.GLFW_REPEAT) {
//                for (AbilityKeyMapping key : ABILITY_KEYS) {
//                    AbilityInstance<?> entry = list.getDisplayedAbilities()[key.index - 1];
//
//                    if (entry != null && (action != GLFW.GLFW_PRESS || (!entry.getAbility().getConditions().needsEmptyHand() || client.player.getMainHandItem().isEmpty()))) {
//                        if (key.matches(keyCode, scanCode) && entry.getAbility().getConditions().getKeyType() == AbilityConditions.KeyType.KEY_BIND) {
//                            NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), action == GLFW.GLFW_PRESS));
//                        } else if (entry.getAbility().getConditions().getKeyType() == AbilityConditions.KeyType.SPACE_BAR && client.options.keyJump.matches(keyCode, scanCode)) {
//                            NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), action == GLFW.GLFW_PRESS));
//                            return;
//                        }
//                    }
//                }
//            }
//        }
//    }

//    @Override
//    public EventResult mouseScrolling(Minecraft client, double scrollDeltaX, double scrollDeltaY, boolean leftDown, boolean middleDown, boolean rightDown, double mouseX, double mouseY) {
//        var player = Objects.requireNonNull(client.player);
//
//        // Disable active toggle abilities
//        List<AbilityInstance<Ability>> activeToggles = AbilityUtil.getInstances(player).stream()
//                .filter(e -> e.getAbility().getConditions().getKeyPressType() == AbilityConditions.KeyPressType.TOGGLE
//                        && e.getAbility().getConditions().getKeyType().toString().toLowerCase(Locale.ROOT).startsWith("scroll")
//                        && e.isEnabled())
//                .toList();
//
//        // TODO
//        for (AbilityInstance<Ability> active : activeToggles) {
//            if ((active.getAbility().getConditions().getKeyType() == AbilityConditions.KeyType.SCROLL_UP && scrollDeltaY < 0D)
//                    || (active.getAbility().getConditions().getKeyType() == AbilityConditions.KeyType.SCROLL_DOWN && scrollDeltaY > 0D)
//                    || (active.getAbility().getConditions().getKeyType() == AbilityConditions.KeyType.SCROLL_EITHER && scrollDeltaY != 0D)) {
//                NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(active.getReference(), true));
//                return EventResult.cancel();
//            }
//        }
//
//        AbilityInstance<?> entry = null;
//
//        if (scrollDeltaY > 0D) {
//            entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConditions.KeyType.SCROLL_UP);
//        } else if (scrollDeltaY < 0D) {
//            entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConditions.KeyType.SCROLL_DOWN);
//        }
//
//        if (entry == null) {
//            entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConditions.KeyType.SCROLL_EITHER);
//        }
//
//        // TODO
//        if (entry != null && entry.isUnlocked() && (!entry.getAbility().getConditions().needsEmptyHand() || player.getMainHandItem().isEmpty())) {
//            var pressType = entry.getAbility().getConditions().getKeyPressType();
//
//            if (pressType == AbilityConditions.KeyPressType.ACTION) {
//                if (!entry.isOnCooldown()) {
//                    NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), true));
//                    return EventResult.cancel();
//                }
//            } else if (pressType == AbilityConditions.KeyPressType.ACTIVATION) {
//                if (!entry.isOnCooldown() && !entry.isEnabled()) {
//                    NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), true));
//                    return EventResult.cancel();
//                }
//            } else if (pressType == AbilityConditions.KeyPressType.TOGGLE) {
//                NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), true));
//                return EventResult.cancel();
//            }
//        }
//
//        return EventResult.pass();
//    }

//    @Override
//    public void clientTick(Minecraft minecraft) {
//        if (minecraft.player != null && minecraft.screen == null && !minecraft.player.isSpectator()) {
    // Stop left-clicked ability
    // TODO
//            if (LEFT_CLICKED_ABILITY != null && !minecraft.options.keyAttack.isDown()) {
//                NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(LEFT_CLICKED_ABILITY.getReference(), false));
//                LEFT_CLICKED_ABILITY = null;
//            }
//
//            // Stop right-clicked ability
//            if (RIGHT_CLICKED_ABILITY != null && !minecraft.options.keyUse.isDown()) {
//                NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(RIGHT_CLICKED_ABILITY.getReference(), false));
//                RIGHT_CLICKED_ABILITY = null;
//            }
//        }
//    }

    public static class AbilityKeyMapping extends KeyMapping {

        public final int index;

        public AbilityKeyMapping(String description, int keyCode, String category, int index) {
            super(description, InputConstants.Type.KEYSYM, keyCode, category);
            this.index = index;
        }
    }

}