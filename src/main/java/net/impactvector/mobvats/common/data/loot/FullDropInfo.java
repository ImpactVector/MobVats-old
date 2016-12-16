package net.impactvector.mobvats.common.data.loot;

import net.minecraft.item.ItemStack;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public class FullDropInfo {

    ItemStack itemStack;
    float dropChance;

    public FullDropInfo(ItemStack itemStack, float dropChance) {

        this.itemStack = itemStack;
        this.dropChance = dropChance;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public float getDropChance() {
        return this.dropChance;
    }
}
