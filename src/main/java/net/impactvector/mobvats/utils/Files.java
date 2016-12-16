package net.impactvector.mobvats.utils;

import net.impactvector.mobvats.common.MobVats;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public class Files {

    public static File globalDataDirectory;
    public static File lootFile;

    private static final String LOOT_FILENAME = "loot.json";

    public static void init(FMLPreInitializationEvent event) {

        globalDataDirectory = new File(event.getModConfigurationDirectory().getParentFile(),
                File.separator + MobVats.MODID);

        lootFile = new File(globalDataDirectory, LOOT_FILENAME);
    }
}
