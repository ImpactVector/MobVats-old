package net.impactvector.mobvats.common;

import net.impactvector.mobvats.init.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabBR extends CreativeTabs {

	public CreativeTabBR(String label)
	{
		super(label);
	}

	public Item getTabIconItem()
	{
		return Item.getItemFromBlock(ModBlocks.brOre);
	}
}
