package net.impactvector.mobvats.init;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.MetalType;
import net.impactvector.mobvats.common.config.Config;
import net.impactvector.mobvats.common.item.*;
import net.impactvector.mobvats.common.multiblock.PartTier;
import it.zerono.mods.zerocore.lib.MetalSize;
import it.zerono.mods.zerocore.util.OreDictionaryHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class ModItems {

    // Ingots & dusts
    public static final ItemBRMetal ingotMetals;
    public static final ItemBRMetal dustMetals;
    public static final ItemMineral minerals;

    // Reactor components
    public static final ItemTieredComponent reactorCasingCores;

    // Turbine components
    public static final ItemTieredComponent turbineHousingCores;

    // Miscellanea
    public static final ItemWrench wrench;
    public static final ItemSyringe syringe;
    public static final ItemVialEssence vialEssence;

    public static void initialize() {
    }

    static {

        final InitHandler init = InitHandler.INSTANCE;

        // register items

        // - Ingots & dusts
        ingotMetals = (ItemBRMetal)init.register(new ItemBRMetal("ingotMetals", MetalSize.Ingot) {

             @Override
             public void registerRecipes() {

                 final Config configs = MobVats.CONFIG;
                 final ItemStack ingotGraphite = OreDictionaryHelper.getOre("ingotGraphite");
                 final ItemStack ingotCyanite = OreDictionaryHelper.getOre("ingotCyanite");

                 // Graphite & Cyanite

                 // -- Coal -> Graphite
                 if (configs.registerCoalForSmelting)
                     GameRegistry.addSmelting(Items.COAL, ingotGraphite, 1);

                 // -- Charcoal -> Graphite
                 if (configs.registerCharcoalForSmelting)
                     GameRegistry.addSmelting(new ItemStack(Items.COAL, 1, 1), ingotGraphite, 1);

                 // -- Gravel + Coal -> Graphite
                 if (configs.registerGraphiteCoalCraftingRecipes)
                     GameRegistry.addRecipe(new ShapedOreRecipe(ingotGraphite, "GCG", 'G', Blocks.GRAVEL, 'C',
                             new ItemStack(Items.COAL, 1, 0)));

                 // -- Gravel + Charcoal -> Graphite
                 if (configs.registerGraphiteCharcoalCraftingRecipes)
                     GameRegistry.addRecipe(new ShapedOreRecipe(ingotGraphite, "GCG", 'G', Blocks.GRAVEL, 'C',
                             new ItemStack(Items.COAL, 1, 1)));

                 // -- Yellorium ingot + Sand -> Cyanite
                 if (configs.enableCyaniteFromYelloriumRecipe)
                     GameRegistry.addRecipe(new ShapelessOreRecipe(ingotCyanite, configs.recipeYelloriumIngotName, Blocks.SAND));


                 // TEMPORARY recipe for the blutonium ingot

                 GameRegistry.addRecipe(ModItems.ingotMetals.createItemStack(MetalType.Blutonium, 1), "CCC", "C C", "CCC",
                         'C', ingotCyanite);
             }
        });

        dustMetals = (ItemBRMetal)init.register(new ItemBRMetal("dustMetals", MetalSize.Dust) {

            @Override
            public void registerRecipes() {

                for (MetalType metal : MetalType.VALUES) {

                    // smelt dust into ingot
                    GameRegistry.addSmelting(ModItems.dustMetals.createItemStack(metal, 1),
                            ModItems.ingotMetals.createItemStack(metal, 1), 0.0f);
                }
            }
        });

        minerals = (ItemMineral)init.register(new ItemMineral("minerals"));

        // Reactor components
        reactorCasingCores = (ItemTieredComponent)init.register(new ItemTieredComponent("reactorCasingCores") {

            @Override
            public void registerRecipes() {

                if (PartTier.REACTOR_TIERS.contains(PartTier.Legacy))
                    GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Legacy, 1), "IGI", "ARA", "IGI",
                            'I', "ingotIron", 'G', "ingotGraphite",
                            'A', "ingotGold", 'R', Items.REDSTONE));

                if (PartTier.REACTOR_TIERS.contains(PartTier.Basic))
                    GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Basic, 1), "IGI", "ARA", "IGI",
                            'I', "ingotSteel", 'G', "ingotGraphite",
                            'A', "ingotGold", 'R', Items.REDSTONE));
            }
        });

        // Turbine components
        turbineHousingCores = (ItemTieredComponent)init.register(new ItemTieredComponent("turbineHousingCores") {

            @Override
            public void registerRecipes() {

                if (PartTier.TURBINE_TIERS.contains(PartTier.Legacy))
                    GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Legacy, 1), "IGI", "ARA", "IGI",
                            'I', "ingotIron", 'G', "ingotGraphite",
                            'A', "ingotGold", 'R', Items.COMPARATOR));

                if (PartTier.TURBINE_TIERS.contains(PartTier.Basic))
                    GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Basic, 1), "IGI", "ARA", "IGI",
                            'I', "ingotSteel", 'G', "ingotGraphite",
                            'A', "ingotGold", 'R', Items.COMPARATOR));
            }
        });

        // Miscellanea
        wrench = (ItemWrench)init.register(new ItemWrench("wrench"));
        syringe = (ItemSyringe)init.register(new ItemSyringe("syringe"));
        vialEssence = (ItemVialEssence)init.register(new ItemVialEssence("vialEssence"));
    }
}
