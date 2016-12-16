package net.impactvector.mobvats.common.item;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.init.ModItems;
//import net.impactvector.mobvats.reference.Lang;
//import net.impactvector.mobvats.reference.Reference;
//import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorHead;
import net.impactvector.mobvats.utils.StringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by impactvector on 11/30/2016.
 */
public class ItemVialEssence extends ItemBase {

    public ItemVialEssence(String itemName) {
        super(itemName);
        this.setMaxStackSize(16);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onPostClientRegister() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    public void registerRecipes() {
        //none -- you use the syringe to create this
    }

//    @Override
//    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//
//        if (worldIn.isRemote)
//            return EnumActionResult.PASS;
//
//        if (!hasMobName(stack))
//            return EnumActionResult.FAIL;
//
//        TileEntity te = worldIn.getTileEntity(pos);
//        if (te instanceof TileEntityReactorHead) {
//            if (!MobVats.mobRegistry.isValidMobName(((TileEntityReactorHead) te).getMobName())) {
//                ((TileEntityReactorHead) te).setMobName(getMobName(stack), getDisplayName(stack), getXp(stack));
//                if (!playerIn.capabilities.isCreativeMode)
//                    stack.stackSize--;
//                return EnumActionResult.SUCCESS;
//            }
//        }
//
//        return EnumActionResult.FAIL;
//    }


    /**
     * All we store in the prism is the name of the mob
     */
    static final String NBT_MOBNAME = "mobName";
    static final String NBT_DISPLAYNAME = "displayName";
    static final String NBT_XP_VALUE = "mobXpCost";
    public static void setMobName(ItemStack itemStack, String mobName, String displayName, int xp) {

//        if (xp <= 0)
//            xp = MobRegistry.MobInfo.MIN_XP_VALUE;

        if (itemStack.getTagCompound() == null)
            itemStack.setTagCompound(new NBTTagCompound());

        itemStack.getTagCompound().setString(NBT_MOBNAME, mobName);
        itemStack.getTagCompound().setString(NBT_DISPLAYNAME, displayName);
        itemStack.getTagCompound().setInteger(NBT_XP_VALUE, xp);
    }

    public static String getMobName(ItemStack itemStack) {

        if (itemStack.getTagCompound() == null)
            return "";

        return itemStack.getTagCompound().getString(NBT_MOBNAME);
    }


    public static String getDisplayName(ItemStack itemStack) {

        if (itemStack.getTagCompound() == null)
            return "";

        return itemStack.getTagCompound().getString(NBT_DISPLAYNAME);
    }

    public static int getXp(ItemStack itemStack) {

        if (itemStack.getTagCompound() == null)
            return 1;

        return itemStack.getTagCompound().getInteger(NBT_XP_VALUE);
    }

    public static boolean hasMobName(ItemStack itemStack) {

        if (itemStack.getItem() != ModItems.vialEssence)
            return false;

        return MobVats.mobRegistry.isValidMobName(getMobName(itemStack));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {

//        tooltip.add(StringHelper.localize(Lang.TAG_TOOLTIP + BASENAME + ".0"));
//        tooltip.add(StringHelper.localize(Lang.TAG_TOOLTIP + BASENAME + ".1"));
        if (stack != null && hasMobName(stack) && MobVats.mobRegistry.isValidMobName(getMobName(stack))) {
            String displayName = getDisplayName(stack);
            if (!displayName.equals(""))
                tooltip.add(TextFormatting.GREEN + String.format("Mob: %s", StringHelper.localize(displayName)));

//            int xp = MobVats.mobRegistry.getSpawnXp(getMobName(stack));
//            EnumMobFactoryTier t = MobVats.tierMapper.getTierForEntity(getMobName(stack), xp);
//            tooltip.add(TextFormatting.BLUE + t.getTranslated(Lang.WAILA_FACTORY_TIER));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {

        return hasMobName(stack);
    }

    public static ItemStack getItemStack(String mobVatsName, int xp) {
        //return null;
        if (!MobVats.mobRegistry.isValidMobName(mobVatsName))
            return null;

        ItemStack itemStack = new ItemStack(ModItems.vialEssence);
        setMobName(itemStack, mobVatsName, mobVatsName, xp);
        return itemStack;
    }
}
