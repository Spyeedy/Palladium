package net.threetag.palladium.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum PlayerModelChangeType implements StringRepresentable {

    KEEP(0, "keep"),
    NORMAL(1, "normal"),
    SLIM(2, "slim");

    public static final IntFunction<PlayerModelChangeType> BY_ID = ByIdMap.continuous(PlayerModelChangeType::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<PlayerModelChangeType> CODEC = StringRepresentable.fromEnum(PlayerModelChangeType::values);
    public static final StreamCodec<ByteBuf, PlayerModelChangeType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, PlayerModelChangeType::id);

    private final int id;
    private final String name;

    PlayerModelChangeType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public int id() {
        return this.id;
    }

    public PlayerSkin.Model toPlayerSkinModelType(PlayerSkin.Model modelType) {
        if (this == KEEP) {
            return modelType;
        } else if (this == SLIM) {
            return PlayerSkin.Model.SLIM;
        } else {
            return PlayerSkin.Model.WIDE;
        }
    }
}
