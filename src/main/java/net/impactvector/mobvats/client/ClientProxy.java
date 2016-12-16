package net.impactvector.mobvats.client;

import net.impactvector.mobvats.client.renderer.RendererReactorFuelRod;
import net.impactvector.mobvats.client.renderer.RotorSpecialRenderer;
import net.impactvector.mobvats.common.CommonProxy;
import net.impactvector.mobvats.common.block.BlockBR;
import net.impactvector.mobvats.common.block.BlockBRGenericFluid;
import net.impactvector.mobvats.common.item.ItemBase;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorFuelRod;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityTurbineRotorBearing;
import net.impactvector.mobvats.gui.BeefGuiIconManager;
import net.impactvector.mobvats.init.ModFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	public static BeefGuiIconManager GuiIcons;
	public static CommonBlockIconManager CommonBlockIcons;

	public static long lastRenderTime = Minecraft.getSystemTime();
	
	public ClientProxy() {
		GuiIcons = new BeefGuiIconManager();
		CommonBlockIcons = new CommonBlockIconManager();
	}

	@Override
	public BlockBR register(BlockBR block) {

		super.register(block);
		block.onPostClientRegister();
		return block;
	}

	@Override
	public BlockBRGenericFluid register(BlockBRGenericFluid block) {

		super.register(block);
		block.onPostClientRegister();
		return block;
	}

	@Override
	public ItemBase register(ItemBase item) {

		super.register(item);
		item.onPostClientRegister();
		return item;
	}

	@Override
	public void onInit(FMLInitializationEvent event) {

		super.onInit(event);

		MinecraftForge.EVENT_BUS.register(new BRRenderTickHandler());

		// register TESRs
		this.registerTESRs();
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerIcons(TextureStitchEvent.Pre event) {

		final TextureMap map = event.getMap();

//		this.registerFluidTextures(map, ModFluids.fluidSteam);
//		this.registerFluidTextures(map, ModFluids.fluidFuelColumn);

		GuiIcons.registerIcons(map);
		CommonBlockIcons.registerIcons(map);

		// Reset any controllers which have TESRs which cache displaylists with UV data in 'em
		// This is necessary in case a texture pack changes UV coordinates on us

        /* TODO track turbines locally
		Set<MultiblockControllerBase> controllers = MultiblockRegistry.getControllersFromWorld(FMLClientHandler.instance().getClient().theWorld);
		if(controllers != null) {
			for(MultiblockControllerBase controller: controllers) {
				if(controller instanceof MultiblockTurbine) {
					((MultiblockTurbine)controller).resetCachedRotors();
				}
			}
		}
		*/
	}

	private void registerTESRs() {

		// reactor fuel rods
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorFuelRod.class, new RendererReactorFuelRod());

		// turbine rotor
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbineRotorBearing.class, new RotorSpecialRenderer());
	}

	private void registerFluidTextures(final TextureMap map, final Fluid fluid) {

		map.registerSprite(fluid.getStill());
		map.registerSprite(fluid.getFlowing());
	}
}
