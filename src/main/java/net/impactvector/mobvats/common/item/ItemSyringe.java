package net.impactvector.mobvats.common.item;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.init.ModItems;
//import net.impactvector.mobvats.manager.MobRegistry;
//import net.impactvector.mobvats.tileentity.TileEntityReactorHead;
//import net.impactvector.mobvats.tileentity.multiblock.EnumMobFactoryTier;
//import net.impactvector.mobvats.util.MobVatsEntityLiving;
import net.impactvector.mobvats.utils.StringHelper;
import net.impactvector.mobvats.utils.Lang;
import net.impactvector.mobvats.utils.EntityHelper;
import net.impactvector.mobvats.utils.MobVatsItemGlassBottle;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public class ItemSyringe extends ItemBase {

    public ItemSyringe(String itemName) {

        super(itemName);
        setMaxStackSize(1);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void onPostClientRegister() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    public void registerRecipes() {
        GameRegistry.addRecipe(
                new ShapedOreRecipe(
                        new ItemStack(ModItems.syringe),
                        "   ", "bti", "   ",
                        'b', Items.BONE,
                        'i', Items.IRON_INGOT,
                        't', Items.GLASS_BOTTLE));

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.syringe), new ItemStack(ModItems.syringe));
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

        if (attacker.worldObj.isRemote)
            return false;

        if (!(attacker instanceof EntityPlayer))
            return false;

        String mobVatsName = MobVats.mobRegistry.onEntityLiving((EntityLiving) target);
        if (!MobVats.mobRegistry.isValidMobName(mobVatsName))
            return false;
        String displayName = MobVats.mobRegistry.getDisplayName(mobVatsName);

        ItemStack vialStack = new ItemStack(ModItems.vialEssence, 1);

        if (!MobVats.mobRegistry.isPrismValid(mobVatsName)) {
            ((EntityPlayer) attacker).addChatComponentMessage(
                    new TextComponentString(String.format(
                            StringHelper.localize(Lang.CHAT_PRISM_INVALID), displayName, mobVatsName)));
            return false;
        }

        ItemVialEssence.setMobName(vialStack, mobVatsName, displayName, EntityHelper.getExperienceValue((EntityLiving)target));
        ((EntityPlayer) attacker).addChatComponentMessage(
                new TextComponentString(String.format(
                        StringHelper.localize(Lang.CHAT_PRISM_PROGRAM), displayName, mobVatsName)));

        EntityPlayer player = (EntityPlayer)attacker;
        if (!player.capabilities.isCreativeMode) {
            ItemStack bottleStack = new ItemStack(Items.GLASS_BOTTLE);
            if (player.inventory.hasItemStack(bottleStack)) {
                int slot = player.inventory.getSlotFor(bottleStack);
                ItemStack playerBottleStack = player.inventory.getStackInSlot(slot);
                new MobVatsItemGlassBottle().turnBottleIntoItem(playerBottleStack, player, vialStack);
            }
        } else {
            //if creative, just give them the blood
            if (!player.inventory.addItemStackToInventory(vialStack)) {
                player.dropItem(vialStack, false);
            }
        }


        return true;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote)
            return new ActionResult(EnumActionResult.PASS, itemStackIn);

        if (!playerIn.capabilities.isCreativeMode) {
            ItemStack bottleStack = new ItemStack(Items.GLASS_BOTTLE);
            if (playerIn.inventory.hasItemStack(bottleStack)) {
                int slot = playerIn.inventory.getSlotFor(bottleStack);
                ItemStack playerBottleStack = playerIn.inventory.getStackInSlot(slot);
                playerIn.attackEntityFrom(DamageSource.causePlayerDamage(playerIn), 2); //1 full heart of damage -- TODO: figure out if armor reduces it -- it should not
                new MobVatsItemGlassBottle().turnBottleIntoItem(playerBottleStack, playerIn, new ItemStack(ModItems.vialEssence, 1));
                return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
            } else {
                return new ActionResult(EnumActionResult.PASS, itemStackIn);
            }
        } else {
            //if creative, just give them the blood
            ItemStack vialStack = new ItemStack(ModItems.vialEssence);
            if (!playerIn.inventory.addItemStackToInventory(vialStack)) {
                playerIn.dropItem(vialStack, false);
            }
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }
    }





}
