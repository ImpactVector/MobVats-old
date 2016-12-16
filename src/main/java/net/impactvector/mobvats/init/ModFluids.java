package net.impactvector.mobvats.init;

import net.impactvector.mobvats.common.MobVats;
import it.zerono.mods.zerocore.lib.fluid.ModFluid;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

    // Fluids
//    public static final Fluid fluidYellorium;
//    public static final Fluid fluidCyanite;
//    public static final Fluid fluidSteam;
//    public static final Fluid fluidFuelColumn;
    public static final Fluid fluidExperience;
    public static final Fluid fluidMeat;
    public static final Fluid fluidSlurry;
    public static final Fluid fluidWaste;

    public static void initialize() {
    }

    static {

        Fluid fluid;

        // register fluids


        // - experience
        if (null == (fluid = FluidRegistry.getFluid("experience"))) {

            FluidRegistry.registerFluid(fluid = new ModFluid("experience",
                    MobVats.createBlockResourceLocation("experienceStill"),
                    MobVats.createBlockResourceLocation("experienceFlowing")) {

                @Override
                protected void initialize() {

                    this.setDensity(100);
                    this.setGaseous(false);
                    this.setLuminosity(10);
                    this.setRarity(EnumRarity.UNCOMMON);
                    this.setViscosity(100);
                }
            });
        }
        FluidRegistry.addBucketForFluid(fluidExperience = fluid);

        // - meat
        if (null == (fluid = FluidRegistry.getFluid("meat"))) {

            FluidRegistry.registerFluid(fluid = new ModFluid("meat",
                    MobVats.createBlockResourceLocation("meatStill"),
                    MobVats.createBlockResourceLocation("meatFlowing")) {

                @Override
                protected void initialize() {

                    this.setDensity(100);
                    this.setGaseous(false);
                    this.setLuminosity(6);
                    this.setRarity(EnumRarity.UNCOMMON);
                    this.setViscosity(100);
                }
            });
        }
        FluidRegistry.addBucketForFluid(fluidMeat = fluid);


        // - slurry
        if (null == (fluid = FluidRegistry.getFluid("slurry"))) {

            FluidRegistry.registerFluid(fluid = new ModFluid("slurry",
                    MobVats.createBlockResourceLocation("slurryStill"),
                    MobVats.createBlockResourceLocation("slurryFlowing")) {

                @Override
                protected void initialize() {

                    this.setDensity(100);
                    this.setGaseous(false);
                    this.setLuminosity(8);
                    this.setRarity(EnumRarity.UNCOMMON);
                    this.setViscosity(100);
                }
            });
        }
        FluidRegistry.addBucketForFluid(fluidSlurry = fluid);

        // - slurry
        if (null == (fluid = FluidRegistry.getFluid("waste"))) {

            FluidRegistry.registerFluid(fluid = new ModFluid("waste",
                    MobVats.createBlockResourceLocation("wasteStill"),
                    MobVats.createBlockResourceLocation("wasteFlowing")) {

                @Override
                protected void initialize() {

                    this.setDensity(100);
                    this.setGaseous(false);
                    this.setLuminosity(4);
                    this.setRarity(EnumRarity.UNCOMMON);
                    this.setViscosity(100);
                }
            });
        }
        FluidRegistry.addBucketForFluid(fluidWaste = fluid);
//
//        // - yellorium
//        if (null == (fluid = FluidRegistry.getFluid("yellorium"))) {
//
//            FluidRegistry.registerFluid(fluid = new ModFluid("yellorium",
//                    MobVats.createBlockResourceLocation("yelloriumStill"),
//                    MobVats.createBlockResourceLocation("yelloriumFlowing")) {
//
//                @Override
//                protected void initialize() {
//
//                    this.setDensity(100);
//                    this.setGaseous(false);
//                    this.setLuminosity(10);
//                    this.setRarity(EnumRarity.UNCOMMON);
//                    this.setTemperature(295);
//                    this.setViscosity(100);
//                }
//            });
//        }
//
//        FluidRegistry.addBucketForFluid(fluidYellorium = fluid);
//
//        // - cyanite
//        if (null == (fluid = FluidRegistry.getFluid("cyanite"))) {
//
//            FluidRegistry.registerFluid(fluid = new ModFluid("cyanite",
//                    MobVats.createBlockResourceLocation("cyaniteStill"),
//                    MobVats.createBlockResourceLocation("cyaniteFlowing")) {
//
//                @Override
//                protected void initialize() {
//
//                    this.setDensity(100);
//                    this.setGaseous(false);
//                    this.setLuminosity(6);
//                    this.setRarity(EnumRarity.UNCOMMON);
//                    this.setTemperature(295);
//                    this.setViscosity(100);
//                }
//            });
//        }
//
//        FluidRegistry.addBucketForFluid(fluidCyanite = fluid);
//
//        // - steam
//        if (null == (fluid = FluidRegistry.getFluid("steam"))) {
//
//            FluidRegistry.registerFluid(fluid = new ModFluid("steam",
//                    MobVats.createBlockResourceLocation("steamStill"),
//                    MobVats.createBlockResourceLocation("steamFlowing")) {
//
//                @Override
//                protected void initialize() {
//
//                    this.setTemperature(1000); // For consistency with TE
//                    this.setGaseous(true);
//                    this.setLuminosity(0);
//                    this.setRarity(EnumRarity.COMMON);
//                    this.setDensity(6);
//                }
//            });
//        }
//
//        FluidRegistry.addBucketForFluid(fluidSteam = fluid);
//
//        // - fuel column for rendering
//        if (null == (fluid = FluidRegistry.getFluid("fuelcolumn"))) {
//
//            FluidRegistry.registerFluid(fluid = new ModFluid("fuelcolumn",
//                    MobVats.createBlockResourceLocation("fuelColumnStill"),
//                    MobVats.createBlockResourceLocation("fuelColumnFlowing")) {
//
//                @Override
//                protected void initialize() {
//                }
//            });
//        }
//
//        fluidFuelColumn = fluid;
    }
}
