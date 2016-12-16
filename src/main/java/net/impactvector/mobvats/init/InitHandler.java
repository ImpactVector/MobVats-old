package net.impactvector.mobvats.init;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.CommonProxy;
import net.impactvector.mobvats.common.block.BlockBR;
import net.impactvector.mobvats.common.block.BlockBRGenericFluid;
import net.impactvector.mobvats.common.item.ItemBase;
import it.zerono.mods.zerocore.lib.IGameObject;
import it.zerono.mods.zerocore.lib.IModInitializationHandler;
import it.zerono.mods.zerocore.util.OreDictionaryHelper;
import net.impactvector.mobvats.event.HandlerLivingDropsEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public final class InitHandler implements IModInitializationHandler {

    public static final InitHandler INSTANCE;

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

        ModFluids.initialize();
        ModItems.initialize();
        ModBlocks.initialize();

        MinecraftForge.EVENT_BUS.register(new HandlerLivingDropsEvent());
    }

    @Override
    public void onInit(FMLInitializationEvent event) {

        // Patch up vanilla being stupid - most mods already do this, so it's usually a no-op

        if (!OreDictionaryHelper.doesOreNameExist("blockSnow"))
            OreDictionary.registerOre("blockSnow", new ItemStack(Blocks.SNOW, 1));

        if (!OreDictionaryHelper.doesOreNameExist("blockIce"))
            OreDictionary.registerOre("blockIce", new ItemStack(Blocks.ICE, 1));

        // Register ore dict entries and recipes

        for (IGameObject obj: this._objects)
            obj.registerOreDictionaryEntries();

        for (IGameObject obj: this._objects)
            obj.registerRecipes();
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {

        this._objects.clear();
        this._objects = null;
        this._proxy = null;
    }

    private InitHandler() {

        this._objects = new ArrayList<>();
        this._proxy = MobVats.getProxy();
    }

    protected ItemBase register(ItemBase item) {

        this._objects.add(item);
        return this._proxy.register(item);
    }

    protected BlockBR register(BlockBR block) {

        this._objects.add(block);
        return this._proxy.register(block);
    }

    protected BlockBRGenericFluid register(BlockBRGenericFluid block) {

        this._objects.add(block);
        return this._proxy.register(block);
    }

    protected void register(Class<? extends TileEntity> tileEntityClass) {

        GameRegistry.registerTileEntity(tileEntityClass, MobVats.MODID + tileEntityClass.getSimpleName());
    }

    private List<IGameObject> _objects;
    private CommonProxy _proxy;

    static {
        INSTANCE = new InitHandler();
    }
}
