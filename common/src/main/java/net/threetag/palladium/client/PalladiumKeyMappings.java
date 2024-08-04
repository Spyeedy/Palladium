package net.threetag.palladium.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.network.AbilityKeyPressedPacket;
import net.threetag.palladium.network.NotifyMovementKeyListenerPacket;
import net.threetag.palladium.network.ToggleOpenableEquipmentPacket;
import net.threetag.palladium.power.ability.AbilityConditions;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladiumcore.event.ClientTickEvents;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.InputEvents;
import net.threetag.palladiumcore.network.NetworkManager;
import net.threetag.palladiumcore.registry.client.KeyMappingRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class PalladiumKeyMappings implements InputEvents.KeyPressed, ClientTickEvents.ClientTick, InputEvents.MouseScrolling {

    public static final String CATEGORY = "key.palladium.categories.abilities";
    public static final KeyMapping OPEN_EQUIPMENT = new KeyMapping("key.palladium.open_equipment", GLFW.GLFW_KEY_SLASH, "key.categories.gameplay");
    public static final KeyMapping SWITCH_ABILITY_LIST = new KeyMapping("key.palladium.switch_ability_list", 88, CATEGORY);
    public static AbilityKeyMapping[] ABILITY_KEYS = new AbilityKeyMapping[5];
    public static AbilityInstance LEFT_CLICKED_ABILITY = null;
    public static AbilityInstance RIGHT_CLICKED_ABILITY = null;

    public static void init() {
        KeyMappingRegistry.register(OPEN_EQUIPMENT);
        KeyMappingRegistry.register(SWITCH_ABILITY_LIST);
        for (int i = 1; i <= ABILITY_KEYS.length; i++) {
            KeyMappingRegistry.register(ABILITY_KEYS[i - 1] = new AbilityKeyMapping("key.palladium.ability_" + i, i == 1 ? GLFW.GLFW_KEY_V : i == 2 ? GLFW.GLFW_KEY_B : i == 3 ? GLFW.GLFW_KEY_N : i == 4 ? GLFW.GLFW_KEY_M : i == 5 ? GLFW.GLFW_KEY_COMMA : -1, CATEGORY, i));
        }
        var instance = new PalladiumKeyMappings();

        InputEvents.KEY_PRESSED.register(instance);
        InputEvents.MOUSE_SCROLLING.register(instance);
        ClientTickEvents.CLIENT_POST.register(instance);
    }

    @Override
    public void keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers) {
        if (client.player != null && client.screen == null && !client.player.isSpectator()) {

            // Open Equipment
            if (OPEN_EQUIPMENT.isDown()) {
                NetworkManager.get().sendToServer(new ToggleOpenableEquipmentPacket());
                return;
            }

            // Scroll ability list
            if (SWITCH_ABILITY_LIST.isDown()) {
                AbilityBarRenderer.scroll(!client.player.isCrouching());
                return;
            }

            // Ability keys
            AbilityBarRenderer.AbilityList list = AbilityBarRenderer.getSelectedList();
            if (list != null && action != GLFW.GLFW_REPEAT) {
                for (AbilityKeyMapping key : ABILITY_KEYS) {
                    AbilityInstance entry = list.getDisplayedAbilities()[key.index - 1];

                    if (entry != null && (action != GLFW.GLFW_PRESS || (!entry.getConfiguration().getConditions().needsEmptyHand() || client.player.getMainHandItem().isEmpty()))) {
                        if (key.matches(keyCode, scanCode) && entry.getConfiguration().getConditions().getKeyType() == AbilityConditions.KeyType.KEY_BIND) {
                            NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), action == GLFW.GLFW_PRESS));
                        } else if (entry.getConfiguration().getConditions().getKeyType() == AbilityConditions.KeyType.SPACE_BAR && client.options.keyJump.matches(keyCode, scanCode)) {
                            NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), action == GLFW.GLFW_PRESS));
                            return;
                        }
                    }
                }
            }

            // Sync jump key
            if (PalladiumProperties.JUMP_KEY_DOWN.isRegistered(client.player) && client.options.keyJump.isDown() != PalladiumProperties.JUMP_KEY_DOWN.get(client.player)) {
                NetworkManager.get().sendToServer(new NotifyMovementKeyListenerPacket(0, client.options.keyJump.isDown()));
            }

            if (PalladiumProperties.LEFT_KEY_DOWN.isRegistered(client.player) && client.options.keyLeft.isDown() != PalladiumProperties.LEFT_KEY_DOWN.get(client.player)) {
                NetworkManager.get().sendToServer(new NotifyMovementKeyListenerPacket(1, client.options.keyLeft.isDown()));
            }

            if (PalladiumProperties.RIGHT_KEY_DOWN.isRegistered(client.player) && client.options.keyRight.isDown() != PalladiumProperties.RIGHT_KEY_DOWN.get(client.player)) {
                NetworkManager.get().sendToServer(new NotifyMovementKeyListenerPacket(2, client.options.keyRight.isDown()));
            }

            if (PalladiumProperties.FORWARD_KEY_DOWN.isRegistered(client.player) && client.options.keyUp.isDown() != PalladiumProperties.FORWARD_KEY_DOWN.get(client.player)) {
                NetworkManager.get().sendToServer(new NotifyMovementKeyListenerPacket(3, client.options.keyUp.isDown()));
            }

            if (PalladiumProperties.BACKWARDS_KEY_DOWN.isRegistered(client.player) && client.options.keyDown.isDown() != PalladiumProperties.BACKWARDS_KEY_DOWN.get(client.player)) {
                NetworkManager.get().sendToServer(new NotifyMovementKeyListenerPacket(4, client.options.keyDown.isDown()));
            }
        }
    }

    @Override
    public EventResult mouseScrolling(Minecraft client, double scrollDeltaX, double scrollDeltaY, boolean leftDown, boolean middleDown, boolean rightDown, double mouseX, double mouseY) {
        var player = Objects.requireNonNull(client.player);

        // Disable active toggle abilities
        List<AbilityInstance> activeToggles = AbilityUtil.getInstances(player).stream()
                .filter(e -> e.getConfiguration().getConditions().getKeyPressType() == AbilityConditions.KeyPressType.TOGGLE
                        && e.getConfiguration().getConditions().getKeyType().toString().toLowerCase(Locale.ROOT).startsWith("scroll")
                        && e.isEnabled())
                .toList();

        for (AbilityInstance active : activeToggles) {
            if ((active.getConfiguration().getConditions().getKeyType() == AbilityConditions.KeyType.SCROLL_UP && scrollDeltaY < 0D)
                    || (active.getConfiguration().getConditions().getKeyType() == AbilityConditions.KeyType.SCROLL_DOWN && scrollDeltaY > 0D)
                    || (active.getConfiguration().getConditions().getKeyType() == AbilityConditions.KeyType.SCROLL_EITHER && scrollDeltaY != 0D)) {
                NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(active.getReference(), true));
                return EventResult.cancel();
            }
        }

        AbilityInstance entry = null;

        if (scrollDeltaY > 0D) {
            entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConditions.KeyType.SCROLL_UP);
        } else if (scrollDeltaY < 0D) {
            entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConditions.KeyType.SCROLL_DOWN);
        }

        if (entry == null) {
            entry = PalladiumKeyMappings.getPrioritisedKeyedAbility(AbilityConditions.KeyType.SCROLL_EITHER);
        }

        if (entry != null && entry.isUnlocked() && (!entry.getConfiguration().getConditions().needsEmptyHand() || player.getMainHandItem().isEmpty())) {
            var pressType = entry.getConfiguration().getConditions().getKeyPressType();

            if (pressType == AbilityConditions.KeyPressType.ACTION) {
                if (!entry.isOnCooldown()) {
                    NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), true));
                    return EventResult.cancel();
                }
            } else if (pressType == AbilityConditions.KeyPressType.ACTIVATION) {
                if (!entry.isOnCooldown() && !entry.isEnabled()) {
                    NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), true));
                    return EventResult.cancel();
                }
            } else if (pressType == AbilityConditions.KeyPressType.TOGGLE) {
                NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(entry.getReference(), true));
                return EventResult.cancel();
            }
        }

        return EventResult.pass();
    }

    @Override
    public void clientTick(Minecraft minecraft) {
        if (minecraft.player != null && minecraft.screen == null && !minecraft.player.isSpectator()) {
            // Stop left-clicked ability
            if (LEFT_CLICKED_ABILITY != null && !minecraft.options.keyAttack.isDown()) {
                NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(LEFT_CLICKED_ABILITY.getReference(), false));
                LEFT_CLICKED_ABILITY = null;
            }

            // Stop right-clicked ability
            if (RIGHT_CLICKED_ABILITY != null && !minecraft.options.keyUse.isDown()) {
                NetworkManager.get().sendToServer(new AbilityKeyPressedPacket(RIGHT_CLICKED_ABILITY.getReference(), false));
                RIGHT_CLICKED_ABILITY = null;
            }
        }
    }

    public static AbilityInstance getPrioritisedKeyedAbility(AbilityConditions.KeyType keyType) {
        var list = AbilityBarRenderer.getSelectedList();

        if (list != null) {
            for (AbilityInstance ability : list.getDisplayedAbilities()) {
                if (ability != null && ability.isUnlocked()) {
                    if (ability.getConfiguration().getConditions().getKeyType() == keyType) {
                        if (!ability.getConfiguration().getConditions().getKeyType().toString().toLowerCase(Locale.ROOT).startsWith("scroll") || (!ability.getConfiguration().getConditions().allowScrollWhenCrouching() || !Objects.requireNonNull(Minecraft.getInstance().player).isCrouching())) {
                            return ability;
                        }
                    }
                }
            }
        }

        for (AbilityInstance entry : AbilityUtil.getInstances(Minecraft.getInstance().player)) {
            if (entry != null && entry.isUnlocked()) {
                if (entry.getConfiguration().getConditions().getKeyType() == keyType) {
                    if (!entry.getConfiguration().getConditions().getKeyType().toString().toLowerCase(Locale.ROOT).startsWith("scroll") || (!entry.getConfiguration().getConditions().allowScrollWhenCrouching() || !Objects.requireNonNull(Minecraft.getInstance().player).isCrouching())) {
                        return entry;
                    }
                }
            }
        }

        return null;
    }

    public static class AbilityKeyMapping extends KeyMapping {

        public final int index;

        public AbilityKeyMapping(String description, int keyCode, String category, int index) {
            super(description, keyCode, category);
            this.index = index;
        }
    }

}