package net.impactvector.mobvats.init;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.block.BlockBRGenericFluid;
import net.impactvector.mobvats.common.block.BlockBRMetal;
import net.impactvector.mobvats.common.block.BlockBROre;
import net.impactvector.mobvats.common.multiblock.PartType;
import net.impactvector.mobvats.common.multiblock.PowerSystem;
import net.impactvector.mobvats.common.multiblock.block.*;
import net.impactvector.mobvats.common.multiblock.tileentity.*;
import net.impactvector.mobvats.common.multiblock.tileentity.creative.TileEntityReactorCreativeCoolantPort;
import net.impactvector.mobvats.common.multiblock.tileentity.creative.TileEntityTurbineCreativeSteamGenerator;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;

public final class ModBlocks {

    // Ores
    public static final BlockBROre brOre;

    // Metal blocks
    public static final BlockBRMetal blockMetals;

    // Reactor parts
    public static final BlockMultiblockGlass reactorGlass;
    public static final BlockMultiblockCasing reactorCasing;
    public static final BlockMultiblockController reactorController;
    public static final BlockMultiblockPowerTap reactorPowerTapRF;
    public static final BlockMultiblockPowerTap reactorPowerTapTesla;
    public static final BlockMultiblockIOPort reactorAccessPort;
    public static final BlockMultiblockIOPort reactorCoolantPort;
    public static final BlockReactorControlRod reactorControlRod;
    public static final BlockReactorRedNetPort reactorRedNetPort;
    public static final BlockMultiblockComputerPort reactorComputerPort;
    public static final BlockReactorRedstonePort reactorRedstonePort;
    public static final BlockReactorFuelRod reactorFuelRod;
    public static final BlockMultiblockIOPort reactorCreativeCoolantPort;

    // Turbine parts
//    public static final BlockMultiblockGlass turbineGlass;
//    public static final BlockMultiblockCasing turbineHousing;
//    public static final BlockMultiblockController turbineController;
//    public static final BlockMultiblockPowerTap turbinePowerTapRF;
//    public static final BlockMultiblockPowerTap turbinePowerTapTesla;
//    public static final BlockMultiblockComputerPort turbineComputerPort;
//    public static final BlockMultiblockIOPort turbineFluidPort;
//    public static final BlockTurbineRotorBearing turbineBearing;
//    public static final BlockTurbineRotorShaft turbineRotorShaft;
//    public static final BlockTurbineRotorBlade turbineRotorBlade;
//    public static final BlockMultiblockIOPort turbineCreativeSteamGenerator;

    // Vat parts
    public static final BlockMultiblockHead vatHead;
    public static final BlockMultiblockFlesh vatFlesh;
    
    // Devices
    //public static final BlockBRDevice deviceCyaniteRep; // Bye Bye ...

    // Fluid blocks
//    public static final BlockBRGenericFluid yellorium;
//    public static final BlockBRGenericFluid cyanite;
    public static final BlockBRGenericFluid experience;
    public static final BlockBRGenericFluid meat;
    public static final BlockBRGenericFluid slurry;
    public static final BlockBRGenericFluid waste;

    public static void initialize() {
    }

    static {

        final InitHandler init = InitHandler.INSTANCE;
        final boolean regCreativeParts = MobVats.CONFIG.registerCreativeMultiblockParts;

        // register blocks

        // - ores
        brOre = (BlockBROre)init.register(new BlockBROre("brOre"));

        // - metal blocks
        blockMetals = (BlockBRMetal)init.register(new BlockBRMetal("blockMetals"));
        
        // - reactor parts
        reactorCasing = (BlockMultiblockCasing)init.register(new BlockMultiblockCasing(PartType.ReactorCasing, "reactorCasing"));
        reactorGlass = (BlockMultiblockGlass)init.register(new BlockMultiblockGlass(PartType.ReactorGlass, "reactorGlass"));
        reactorController = (BlockMultiblockController)init.register(new BlockMultiblockController(PartType.ReactorController, "reactorController"));
        reactorPowerTapRF = (BlockMultiblockPowerTap)init.register(new BlockMultiblockPowerTap(PartType.ReactorPowerTap, "reactorPowerTapRF", PowerSystem.RedstoneFlux));
        reactorPowerTapTesla = (BlockMultiblockPowerTap)init.register(new BlockMultiblockPowerTap(PartType.ReactorPowerTap, "reactorPowerTapTesla", PowerSystem.Tesla));
        reactorAccessPort = (BlockMultiblockIOPort)init.register(new BlockMultiblockIOPort(PartType.ReactorAccessPort, "reactorAccessPort"));
        reactorCoolantPort = (BlockMultiblockIOPort)init.register(new BlockMultiblockIOPort(PartType.ReactorCoolantPort, "reactorCoolantPort"));
        reactorControlRod = (BlockReactorControlRod)init.register(new BlockReactorControlRod("reactorControlRod"));
        reactorRedNetPort = (BlockReactorRedNetPort)init.register(new BlockReactorRedNetPort("reactorRedNetPort"));
        reactorComputerPort = (BlockMultiblockComputerPort)init.register(new BlockMultiblockComputerPort(PartType.ReactorComputerPort, "reactorComputerPort"));
        reactorRedstonePort = (BlockReactorRedstonePort)init.register(new BlockReactorRedstonePort("reactorRedstonePort"));
        reactorFuelRod = (BlockReactorFuelRod)init.register(new BlockReactorFuelRod("reactorFuelRod"));
        reactorCreativeCoolantPort = !regCreativeParts ? null : (BlockMultiblockIOPort)init.register(new BlockMultiblockIOPort(PartType.ReactorCreativeCoolantPort, "reactorCreativeCoolantPort"));

        // - turbine parts
//        turbineGlass = (BlockMultiblockGlass)init.register(new BlockMultiblockGlass(PartType.TurbineGlass, "turbineGlass"));
//        turbineHousing = (BlockMultiblockCasing)init.register(new BlockMultiblockCasing(PartType.TurbineHousing, "turbineHousing"));
//        turbineController = (BlockMultiblockController)init.register(new BlockMultiblockController(PartType.TurbineController, "turbineController"));
//        turbinePowerTapRF = (BlockMultiblockPowerTap)init.register(new BlockMultiblockPowerTap(PartType.TurbinePowerPort, "turbinePowerTapRF", PowerSystem.RedstoneFlux));
//        turbinePowerTapTesla = (BlockMultiblockPowerTap)init.register(new BlockMultiblockPowerTap(PartType.TurbinePowerPort, "turbinePowerTapTesla", PowerSystem.Tesla));
//        turbineComputerPort = (BlockMultiblockComputerPort)init.register(new BlockMultiblockComputerPort(PartType.TurbineComputerPort, "turbineComputerPort"));
//        turbineFluidPort = (BlockMultiblockIOPort)init.register(new BlockMultiblockIOPort(PartType.TurbineFluidPort, "turbineFluidPort"));
//        turbineBearing = (BlockTurbineRotorBearing)init.register(new BlockTurbineRotorBearing("turbineBearing"));
//        turbineRotorShaft = (BlockTurbineRotorShaft)init.register(new BlockTurbineRotorShaft("turbineRotorShaft"));
//        turbineRotorBlade = (BlockTurbineRotorBlade)init.register(new BlockTurbineRotorBlade("turbineRotorBlade"));
//        turbineCreativeSteamGenerator = !regCreativeParts ? null : (BlockMultiblockIOPort)init.register(new BlockMultiblockIOPort(PartType.TurbineCreativeSteamGenerator, "turbineCreativeSteamGenerator"));


        vatHead = (BlockMultiblockHead) init.register(new BlockMultiblockHead(PartType.ReactorHead, "vatHead"));
        vatFlesh = (BlockMultiblockFlesh) init.register(new BlockMultiblockFlesh(PartType.ReactorFlesh, "vatFlesh"));


        // - devices
        //deviceCyaniteRep = (BlockBRDevice)init.register(new BlockBRDevice(DeviceType.CyaniteReprocessor, "deviceCyaniteRep"));

        // - fluid blocks
//        yellorium = init.register(new BlockBRGenericFluid(ModFluids.fluidYellorium, "yellorium", new MaterialLiquid(MapColor.YELLOW)));
//        cyanite = init.register(new BlockBRGenericFluid(ModFluids.fluidCyanite, "cyanite", Material.LAVA));

        experience = init.register(new BlockBRGenericFluid(ModFluids.fluidExperience, "experience", new MaterialLiquid(MapColor.LIME)));
        meat = init.register(new BlockBRGenericFluid(ModFluids.fluidMeat, "meat", new MaterialLiquid(MapColor.PINK)));
        slurry = init.register(new BlockBRGenericFluid(ModFluids.fluidSlurry, "slurry", new MaterialLiquid(MapColor.GREEN)));
        waste = init.register(new BlockBRGenericFluid(ModFluids.fluidWaste, "waste", new MaterialLiquid(MapColor.BROWN)));

        // - register block tile entities

        //init.register(TileEntityCyaniteReprocessor.class);

        init.register(TileEntityReactorPart.class);
        init.register(TileEntityReactorGlass.class);
        init.register(TileEntityReactorController.class);
        init.register(TileEntityReactorPowerTapRedstoneFlux.class);
        init.register(TileEntityReactorPowerTapTesla.class);
        init.register(TileEntityReactorAccessPort.class);
        init.register(TileEntityReactorFuelRod.class);
        init.register(TileEntityReactorControlRod.class);
        init.register(TileEntityReactorRedstonePort.class);
        init.register(TileEntityReactorComputerPort.class);
        init.register(TileEntityReactorCoolantPort.class);
        init.register(TileEntityReactorCreativeCoolantPort.class);

//        init.register(TileEntityTurbinePart.class);
//        init.register(TileEntityTurbinePowerTapRedstoneFlux.class);
//        init.register(TileEntityTurbinePowerTapTesla.class);
//        init.register(TileEntityTurbineFluidPort.class);
//        init.register(TileEntityTurbinePartGlass.class);
//        init.register(TileEntityTurbineRotorBearing.class);
//        init.register(TileEntityTurbineRotorShaft.class);
//        init.register(TileEntityTurbineRotorBlade.class);
//        init.register(TileEntityTurbineCreativeSteamGenerator.class);
//        init.register(TileEntityTurbineComputerPort.class);
//        init.register(TileEntityTurbineController.class);

        init.register(TileEntityReactorFlesh.class);
        init.register(TileEntityReactorHead.class);

        //init.register(TileEntityReactorRedNetPort.class);
    }
}
