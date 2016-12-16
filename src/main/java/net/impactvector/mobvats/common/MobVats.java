package net.impactvector.mobvats.common;

import net.impactvector.mobvats.api.IHeatEntity;
import net.impactvector.mobvats.api.registry.Reactants;
import net.impactvector.mobvats.api.registry.ReactorConversions;
import net.impactvector.mobvats.api.registry.ReactorInterior;
import net.impactvector.mobvats.api.registry.TurbineCoil;
import net.impactvector.mobvats.common.config.Config;
import net.impactvector.mobvats.common.data.HeadRegistry;
import net.impactvector.mobvats.common.data.loot.LootTableManager;
import net.impactvector.mobvats.common.data.MobRegistry;
import net.impactvector.mobvats.common.multiblock.helpers.SpawnerManager;
import net.impactvector.mobvats.common.data.StandardReactants;
import net.impactvector.mobvats.common.multiblock.helpers.RadiationHelper;
import net.impactvector.mobvats.init.ModBlocks;
import net.impactvector.mobvats.init.ModFluids;
import net.impactvector.mobvats.init.InitHandler;
import net.impactvector.mobvats.net.CommonPacketHandler;
import it.zerono.mods.zerocore.lib.IModInitializationHandler;
import it.zerono.mods.zerocore.lib.gui.ModGuiHandler;
import it.zerono.mods.zerocore.lib.world.IWorldGenWhiteList;
import it.zerono.mods.zerocore.lib.world.WorldGenMinableOres;
import it.zerono.mods.zerocore.lib.world.WorldGenWhiteList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Calendar;
import java.util.Random;

@Mod(modid = MobVats.MODID, name = MobVats.NAME, version = "0.0.0.0",
		acceptedMinecraftVersions = "", dependencies = "required-after:Forge;required-after:zerocore",
		guiFactory = "net.impactvector.mobvats.client.config.ConfigFactory")
public class MobVats implements IModInitializationHandler {

	public static final String NAME = "Mob Vats";
	public static final String MODID = "mobvats";
	public static final int WORLDGEN_VERSION = 1; // Bump this when changing world generation so the world regens
	public static final Config CONFIG;
	public static final CreativeTabs TAB;
	public static final IWorldGenWhiteList WHITELIST_WORLDGEN_ORES;
	public static final WorldGenMinableOres WORLDGEN_ORES;
	public static final WorldGenMinableOres NETHER_ORES;
	public static final WorldGenMinableOres END_ORES;
	public static final BigReactorsTickHandler TICK_HANDLER;
	public static final boolean VALENTINES_DAY; // Easter Egg :)

	public static SpawnerManager spawnerManager = new SpawnerManager();
	public static MobRegistry mobRegistry = new MobRegistry();
	public static HeadRegistry headRegistry = new HeadRegistry();
	public static Random RANDOM = new Random();
	//    public static TierMapper tierMapper = new TierMapper();
	public static LootTableManager LOOT_TABLE_MANAGER = new LootTableManager();

	public static final int defaultFluidColorFuel = 0xbcba50;
	public static final int defaultFluidColorWaste = 0x4d92b5;

	public static MobVats getInstance() {
		return MobVats.s_instance;
	}

	public static CommonProxy getProxy() {
		return MobVats.s_proxy;
	}

	@Mod.EventHandler
	@Override
	public void onPreInit(FMLPreInitializationEvent event) {

		CONFIG.onPreInit(event);
		InitHandler.INSTANCE.onPreInit(event);
		StandardReactants.register();
		MinecraftForge.EVENT_BUS.register(new BREventHandler());
		MinecraftForge.EVENT_BUS.register(MobVats.s_proxy);
		MobVats.s_proxy.onPreInit(event);
	}

	@Mod.EventHandler
	@Override
	public void onInit(FMLInitializationEvent event) {

		CONFIG.onInit(event);
		InitHandler.INSTANCE.onInit(event);

		// add world generator for our ores
		if (CONFIG.enableWorldGen) {

			GameRegistry.registerWorldGenerator(WORLDGEN_ORES, 0);
			GameRegistry.registerWorldGenerator(NETHER_ORES, 0);
			GameRegistry.registerWorldGenerator(END_ORES, 0);
			MinecraftForge.EVENT_BUS.register(TICK_HANDLER);
		}

		CommonPacketHandler.init();
		new ModGuiHandler(MobVats.s_instance);
		MobVats.s_proxy.onInit(event);
		this.registerGameBalanceData();
	}

	@Mod.EventHandler
	@Override
	public void onPostInit(FMLPostInitializationEvent event) {

		CONFIG.onPostInit(event);
		InitHandler.INSTANCE.onPostInit(event);
		MobVats.s_proxy.onPostInit(event);
	}

	// This must be done in init or later
	protected void registerGameBalanceData() {
		// Register ingot & block => reactant mappings
		StandardReactants.yelloriumMapping = Reactants.registerSolid("ingotYellorium", StandardReactants.yellorium);
		StandardReactants.cyaniteMapping = Reactants.registerSolid("ingotCyanite", StandardReactants.cyanite);

		Reactants.registerSolid("ingotBlutonium", StandardReactants.blutonium);

		ItemStack blockYellorium = ModBlocks.blockMetals.createItemStack(MetalType.Yellorium, 1);
		Reactants.registerSolid(blockYellorium, StandardReactants.yellorium, Reactants.standardSolidReactantAmount * 9);

		ItemStack blockBlutonium = ModBlocks.blockMetals.createItemStack(MetalType.Blutonium, 1);
		Reactants.registerSolid(blockBlutonium, StandardReactants.blutonium, Reactants.standardSolidReactantAmount * 9);

		// Register fluid => reactant mappings
//		Reactants.registerFluid(ModFluids.fluidYellorium, StandardReactants.yellorium);
//		Reactants.registerFluid(ModFluids.fluidCyanite, StandardReactants.cyanite);

		// Register reactant => reactant conversions for making cyanite
//		ReactorConversions.register(StandardReactants.yellorium, StandardReactants.cyanite);
//		ReactorConversions.register(StandardReactants.blutonium, StandardReactants.cyanite);


		boolean enableFantasyMetals = MobVats.CONFIG.enableMetallurgyFantasyMetalsInTurbines;
		boolean enableComedy = MobVats.CONFIG.enableComedy;


//		ReactorInterior.registerBlock("blockIron", 0.50f, 0.75f, 1.40f, IHeatEntity.conductivityIron);
//		ReactorInterior.registerBlock("blockGold", 0.52f, 0.80f, 1.45f, IHeatEntity.conductivityGold);
//		ReactorInterior.registerBlock("blockDiamond", 0.55f, 0.85f, 1.50f, IHeatEntity.conductivityDiamond);
//		ReactorInterior.registerBlock("blockEmerald", 0.55f, 0.85f, 1.50f, IHeatEntity.conductivityEmerald);
//		ReactorInterior.registerBlock("blockGraphite", 0.10f, 0.50f, 2.00f, IHeatEntity.conductivityGold); // Graphite: a great moderator!
//		ReactorInterior.registerBlock("blockGlassColorless", 0.20f, 0.25f, 1.10f, IHeatEntity.conductivityGlass);
//		ReactorInterior.registerBlock("blockIce", 0.33f, 0.33f, 1.15f, IHeatEntity.conductivityWater);
//		ReactorInterior.registerBlock("blockSnow", 0.15f, 0.33f, 1.05f, IHeatEntity.conductivityWater / 2f);

		ReactorInterior.registerBlock("blockGrass", 2f, 1.5f, 1f);
        ReactorInterior.registerBlock("blockDirt", .5f, 1.5f, 1f);

//		// Mod blocks
//		ReactorInterior.registerBlock("blockCopper", 0.50f, 0.75f, 1.40f, IHeatEntity.conductivityCopper);
//		ReactorInterior.registerBlock("blockOsmium", 0.51f, 0.77f, 1.41f, IHeatEntity.conductivityCopper);
//		ReactorInterior.registerBlock("blockBrass", 0.51f, 0.77f, 1.41f, IHeatEntity.conductivityCopper);
//		ReactorInterior.registerBlock("blockBronze", 0.51f, 0.77f, 1.41f, IHeatEntity.conductivityCopper);
//		ReactorInterior.registerBlock("blockZinc", 0.51f, 0.77f, 1.41f, IHeatEntity.conductivityCopper);
//		ReactorInterior.registerBlock("blockAluminum", 0.50f, 0.78f, 1.42f, IHeatEntity.conductivityIron);
//		ReactorInterior.registerBlock("blockSteel", 0.50f, 0.78f, 1.42f, IHeatEntity.conductivityIron);
//		ReactorInterior.registerBlock("blockInvar", 0.50f, 0.79f, 1.43f, IHeatEntity.conductivityIron);
//		ReactorInterior.registerBlock("blockSilver", 0.51f, 0.79f, 1.43f, IHeatEntity.conductivitySilver);
//		ReactorInterior.registerBlock("blockLead", 0.75f, 0.75f, 1.75f, IHeatEntity.conductivitySilver);
//		ReactorInterior.registerBlock("blockElectrum", 0.53f, 0.82f, 1.47f, 2.2f); // Between gold and emerald
//		ReactorInterior.registerBlock("blockElectrumFlux", 0.54f, 0.83f, 1.48f, 2.4f); // Between gold and emerald
//		ReactorInterior.registerBlock("blockPlatinum", 0.57f, 0.86f, 1.58f, IHeatEntity.conductivityEmerald);
//		ReactorInterior.registerBlock("blockShiny", 0.57f, 0.86f, 1.58f, IHeatEntity.conductivityEmerald);
//		ReactorInterior.registerBlock("blockTitanium", 0.58f, 0.87f, 1.59f, 2.7f); // Mariculture
//		ReactorInterior.registerBlock("blockEnderium", 0.60f, 0.88f, 1.60f, IHeatEntity.conductivityDiamond);
//
//		if (enableFantasyMetals) {
//			ReactorInterior.registerBlock("blockMithril", 0.53f, 0.81f, 1.45f, IHeatEntity.conductivitySilver);
//			ReactorInterior.registerBlock("blockOrichalcum", 0.52f, 0.83f, 1.46f, 1.7f);    // Between silver and gold
//			ReactorInterior.registerBlock("blockQuicksilver", 0.53f, 0.84f, 1.48f, IHeatEntity.conductivityGold);
//			ReactorInterior.registerBlock("blockHaderoth", 0.54f, 0.84f, 1.49f, IHeatEntity.conductivityEmerald);
//			ReactorInterior.registerBlock("blockCelenegil", 0.54f, 0.84f, 1.49f, IHeatEntity.conductivityDiamond);
//			ReactorInterior.registerBlock("blockTartarite", 0.65f, 0.90f, 1.62f, 4f); // Between diamond and graphene
//			ReactorInterior.registerBlock("blockManyullyn", 0.68f, 0.88f, 1.75f, 4.5f);
//		}
//
//		//Water: 0.33f, 0.5f, 1.33f
		ReactorInterior.registerFluid("water", RadiationHelper.waterData.absorption, RadiationHelper.waterData.burnRate, RadiationHelper.waterData.nutrition);
		ReactorInterior.registerFluid("fluidredstone", 3f, 3f, .5f);
		ReactorInterior.registerFluid("fluidglowstone", 7f, 2f, 1f);
		ReactorInterior.registerFluid("fluidender", .1f, 10f, 2f);
		ReactorInterior.registerFluid("lifeessence", 0.70f, 1f, 3f); // From Blood Magic
		ReactorInterior.registerFluid("meat", 3f, 2f, 2f);
		ReactorInterior.registerFluid("slurry", 5f, 3f, 3f);
		ReactorInterior.registerFluid("experience", 4f, 8f, 1f);
//
//		if (enableComedy) {
//			ReactorInterior.registerBlock("blockMeat", 0.50f, 0.33f, 1.33f, IHeatEntity.conductivityStone);
//			ReactorInterior.registerBlock("blockMeatRaw", 0.40f, 0.50f, 1.50f, IHeatEntity.conductivityStone);
//			ReactorInterior.registerFluid("meat", 0.40f, 0.60f, 1.33f, IHeatEntity.conductivityStone);
//			ReactorInterior.registerFluid("pinkSlime", 0.45f, 0.70f, 1.50f, IHeatEntity.conductivityIron);
//			ReactorInterior.registerFluid("sewage", 0.50f, 0.65f, 1.44f, IHeatEntity.conductivityIron);
//		}
	}

	public static ResourceLocation createResourceLocation(String path) {

		return new ResourceLocation(MobVats.MODID, path);
	}

	public static ResourceLocation createGuiResourceLocation(String path) {

		return MobVats.createResourceLocation("textures/gui/" + path);
	}

	public static ResourceLocation createBlockResourceLocation(String path) {

		return MobVats.createResourceLocation("blocks/" + path);
	}

	@Mod.Instance(MODID)
	private static MobVats s_instance;

	@SidedProxy(clientSide = "net.impactvector.mobvats.client.ClientProxy", serverSide = "net.impactvector.mobvats.common.CommonProxy")
	private static CommonProxy s_proxy;

	/*
	@Mod.Metadata(MODID)
	private static ModMetadata s_metadata;
	*/

	static {

		FluidRegistry.enableUniversalBucket();

		CONFIG = new Config();

		WHITELIST_WORLDGEN_ORES = new WorldGenWhiteList();
		WORLDGEN_ORES = new WorldGenMinableOres(WHITELIST_WORLDGEN_ORES);
		TICK_HANDLER = new BigReactorsTickHandler(WORLDGEN_ORES);

		WorldGenWhiteList whiteList;

		whiteList = new WorldGenWhiteList();
		whiteList.whiteListDimension(-1);
		NETHER_ORES = new WorldGenMinableOres(whiteList);

		whiteList = new WorldGenWhiteList();
		whiteList.whiteListDimension(1);
		END_ORES = new WorldGenMinableOres(whiteList);

		TAB = new CreativeTabBR(MODID);

		// Easter Egg - Check if today is valentine's day. If so, change all particles to hearts.
		Calendar calendar = Calendar.getInstance();
		VALENTINES_DAY = (calendar.get(Calendar.MONTH) == 1 && calendar.get(Calendar.DAY_OF_MONTH) == 14);
	}
}
