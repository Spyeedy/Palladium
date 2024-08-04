package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlayerModelChangeType implements StringRepresentable {

    KEEP("keep"),
    NORMAL("normal"),
    SLIM("slim");

    public static final Codec<PlayerModelChangeType> CODEC = StringRepresentable.fromEnum(PlayerModelChangeType::values);
    public static final StreamCodec<ByteBuf, PlayerModelChangeType> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(i -> PlayerModelChangeType.values()[i], Enum::ordinal);

    private final String name;

    PlayerModelChangeType(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
