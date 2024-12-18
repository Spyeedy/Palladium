package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.client.icon.IngredientIcon;
import net.threetag.palladium.power.ability.Ability;

public class ItemBuyableCondition extends BuyableCondition {

    public static final MapCodec<ItemBuyableCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(ItemBuyableCondition::getIngredient),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount", 1).forGetter(ItemBuyableCondition::getAmount)
            ).apply(instance, ItemBuyableCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemBuyableCondition> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, ItemBuyableCondition::getIngredient,
            ByteBufCodecs.VAR_INT, ItemBuyableCondition::getAmount,
            ItemBuyableCondition::new
    );

    public final Ingredient ingredient;
    private final int amount;

    public ItemBuyableCondition(Ingredient ingredient, int amount) {
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public Ability.UnlockData createData() {
        var items = this.ingredient.items().toList();
        var component = Component.empty();

        for (int i = 0; i < items.size(); i++) {
            var item = items.get(i).value();
            component.append(item.getName(item.getDefaultInstance()));

            if (i < items.size() - 1) {
                if (i == items.size() - 2) {
                    component.append(" ").append(Component.translatable("gui.palladium.powers.buy_ability.or")).append(" ");
                } else {
                    component.append(", ");
                }
            }
        }

        return new Ability.UnlockData(new IngredientIcon(this.ingredient), this.amount, component);
    }

    @Override
    public boolean isAvailable(LivingEntity entity) {
        int found = 0;

        if (entity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);

                if (this.ingredient.test(stack)) {
                    found += stack.getCount();
                }
            }
        }

        return found >= this.amount;
    }

    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        int remove = this.amount;

        if (entity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);

                if (this.ingredient.test(stack)) {
                    int willRemove = Math.min(remove, stack.getCount());
                    player.getInventory().removeItem(i, willRemove);
                    remove -= willRemove;

                    if (remove <= 0) {
                        break;
                    }
                }
            }
        }

        return remove <= 0;
    }

    @Override
    public ConditionSerializer<ItemBuyableCondition> getSerializer() {
        return ConditionSerializers.ITEM_BUYABLE.get();
    }

    public static class Serializer extends ConditionSerializer<ItemBuyableCondition> {

        @Override
        public MapCodec<ItemBuyableCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemBuyableCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "The player needs to spend a certain amount of items to unlock the ability.";
        }
    }
}
