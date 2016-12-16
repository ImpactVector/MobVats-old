package net.impactvector.mobvats.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;

/**
 * Created by dgerold on 11/30/2016.
 */
public class MobVatsItemGlassBottle extends ItemGlassBottle {
    public ItemStack turnBottleIntoItem(ItemStack stackIn, EntityPlayer player, ItemStack stackReplace)
    {
        return super.turnBottleIntoItem(stackIn, player, stackReplace);
    }
}
