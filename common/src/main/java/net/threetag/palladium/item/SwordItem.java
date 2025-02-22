package net.threetag.palladium.item;

import net.minecraft.world.item.ToolMaterial;

public class SwordItem extends net.minecraft.world.item.SwordItem {

    public final ToolMaterial material;
    public final float attackDamage;
    public final float attackSpeed;

    public SwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties);
        this.material = material;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

}
