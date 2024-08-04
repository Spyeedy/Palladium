package net.threetag.palladium.power.ability;

import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.threetag.palladium.condition.*;
import net.threetag.palladium.util.CodecUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public final class AbilityConditions {

    public static final AbilityConditions EMPTY = new AbilityConditions(Collections.emptyList(), Collections.emptyList());

    public static final Codec<AbilityConditions> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CodecUtils.listOrPrimitive(Condition.CODEC).optionalFieldOf("unlocking", Collections.emptyList()).forGetter(AbilityConditions::getUnlockingConditions),
                    CodecUtils.listOrPrimitive(Condition.CODEC).optionalFieldOf("enabling", Collections.emptyList()).forGetter(AbilityConditions::getEnablingConditions)
            ).apply(instance, AbilityConditions::new));

    public static final StreamCodec<FriendlyByteBuf, AbilityConditions> STREAM_CODEC = StreamCodec.of((buf, conditions) -> {
        buf.writeCollection(conditions.dependencies, FriendlyByteBuf::writeUtf);
        buf.writeBoolean(conditions.buyable);
        buf.writeBoolean(conditions.needsKey);
        buf.writeBoolean(conditions.needsEmptyHand);
        buf.writeBoolean(conditions.allowScrollWhenCrouching);
        buf.writeEnum(conditions.keyType);
        buf.writeEnum(conditions.keyPressType);
        buf.writeEnum(conditions.cooldownType);
    }, buf -> {
        var dependencies = buf.readCollection(value -> new ArrayList<>(), FriendlyByteBuf::readUtf);
        var buyable = buf.readBoolean();
        var needsKey = buf.readBoolean();
        var needsEmptyHand = buf.readBoolean();
        var allowScrollWhenCrouching = buf.readBoolean();
        var keyType = buf.readEnum(KeyType.class);
        var keyPressType = buf.readEnum(KeyPressType.class);
        var cooldownType = buf.readEnum(CooldownType.class);
        return new AbilityConditions(dependencies, buyable, needsKey, needsEmptyHand, allowScrollWhenCrouching, keyType, keyPressType, cooldownType);
    });

    private final List<Condition> unlocking;
    private final List<Condition> enabling;
    public final List<String> dependencies;
    private boolean buyable;
    private boolean needsKey = false;
    private boolean needsEmptyHand = false;
    private boolean allowScrollWhenCrouching = true;
    private KeyType keyType = KeyType.KEY_BIND;
    private KeyPressType keyPressType = KeyPressType.ACTION;
    private final CooldownType cooldownType;

    public AbilityConditions(List<String> dependencies, boolean buyable, boolean needsKey, boolean needsEmptyHand, boolean allowScrollWhenCrouching, KeyType keyType, KeyPressType keyPressType, CooldownType cooldownType) {
        this.unlocking = Collections.emptyList();
        this.enabling = Collections.emptyList();
        this.dependencies = new ArrayList<>();
        this.buyable = buyable;
        this.needsKey = needsKey;
        this.needsEmptyHand = needsEmptyHand;
        this.allowScrollWhenCrouching = allowScrollWhenCrouching;
        this.keyType = keyType;
        this.keyPressType = keyPressType;
        this.cooldownType = cooldownType;
    }

    public AbilityConditions(List<Condition> unlocking, List<Condition> enabling) {
        this.unlocking = unlocking;
        this.enabling = enabling;
        this.dependencies = new ArrayList<>();
        this.buyable = false;
        boolean withKeyOrChat = false;
        boolean withKey = false;
        CooldownType cooldownType = null;

        for (Condition condition : this.unlocking) {
            if (condition instanceof BuyableCondition) {
                if (this.buyable) {
                    throw new JsonParseException("Can't have more than one buyable unlock condition!");
                } else {
                    this.buyable = true;
                }
            }

            if (condition.needsKey() || condition instanceof ChatMessageCondition) {
                throw new JsonParseException("Can't have key binding or chat message conditions for unlocking!");
            }

            if (condition.handlesCooldown()) {
                if (cooldownType != null) {
                    throw new JsonParseException("Can't have two abilities handling the cooldown!");
                } else {
                    cooldownType = condition.getCooldownType();
                }
            }

            this.dependencies.addAll(condition.getDependentAbilities());
        }

        for (Condition condition : this.enabling) {
            if (condition instanceof BuyableCondition) {
                throw new JsonParseException("Can't have a buyable unlock condition for enabling!");
            }

            if (condition.needsKey() || condition instanceof ChatMessageCondition) {
                if (withKeyOrChat) {
                    throw new JsonParseException("Can't have two key binding or chat message conditions on one ability!");
                }
                withKeyOrChat = true;
                if (condition.needsKey()) {
                    withKey = true;
                    this.keyType = condition.getKeyType();
                    this.keyPressType = condition.getKeyPressType();

                    if (condition instanceof KeyCondition key) {
                        this.needsEmptyHand = key.needsEmptyHand();
                        this.allowScrollWhenCrouching = key.allowScrollingWhenCrouching();
                    }
                }
            }

            if (condition.handlesCooldown()) {
                if (cooldownType != null) {
                    throw new JsonParseException("Can't have two abilities handling the cooldown!");
                } else {
                    cooldownType = condition.getCooldownType();
                }
            }

            this.dependencies.addAll(condition.getDependentAbilities());
        }

        if (withKey) {
            this.needsKey = true;
        }

        this.cooldownType = cooldownType == null ? CooldownType.STATIC : cooldownType;
    }

    @Nullable
    public BuyableCondition findBuyCondition() {
        for (Condition condition : this.getUnlockingConditions()) {
            if (condition instanceof BuyableCondition buyableCondition) {
                return buyableCondition;
            }
        }
        return null;
    }

    public List<Condition> getUnlockingConditions() {
        return this.unlocking;
    }

    public List<Condition> getEnablingConditions() {
        return this.enabling;
    }

    public boolean isBuyable() {
        return this.buyable;
    }

    public boolean needsKey() {
        return this.needsKey;
    }

    public boolean needsEmptyHand() {
        return this.needsEmptyHand;
    }

    public boolean allowScrollWhenCrouching() {
        return this.allowScrollWhenCrouching;
    }

    public KeyType getKeyType() {
        return this.keyType;
    }

    public KeyPressType getKeyPressType() {
        return this.keyPressType;
    }

    public CooldownType getCooldownType() {
        return this.cooldownType;
    }

    public List<String> getDependencies() {
        return this.dependencies;
    }

    @Override
    public String toString() {
        return "AbilityConditions[" +
                "unlocking=" + unlocking + ", " +
                "enabling=" + enabling + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AbilityConditions) obj;
        return Objects.equals(this.unlocking, that.unlocking) &&
                Objects.equals(this.enabling, that.enabling);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.unlocking, this.enabling);
    }

    public enum KeyType implements StringRepresentable {

        KEY_BIND("key_bind"),
        LEFT_CLICK("left_click"),
        RIGHT_CLICK("right_click"),
        SPACE_BAR("space_bar"),
        SCROLL_UP("scroll_up"),
        SCROLL_DOWN("scroll_down"),
        SCROLL_EITHER("scroll_either");

        public static final Codec<KeyType> CODEC = StringRepresentable.fromEnum(KeyType::values);
        public static final Codec<KeyType> CODEC_WITHOUT_SCROLLING = StringRepresentable.fromEnum(() -> Arrays.stream(values()).filter(kt -> !kt.name.contains("scroll")).toArray(KeyType[]::new));
        public static final StreamCodec<ByteBuf, KeyType> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(i -> KeyType.values()[i], Enum::ordinal);

        private final String name;

        KeyType(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public enum KeyPressType {

        ACTION, ACTIVATION, TOGGLE, HOLD

    }

}
