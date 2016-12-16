package net.impactvector.mobvats.client.config;

import net.impactvector.mobvats.common.MobVats;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigGui extends GuiConfig {

    public ConfigGui(GuiScreen parent) {
        super(parent, MobVats.CONFIG.getConfigElements(), MobVats.MODID, false, false,
                GuiConfig.getAbridgedConfigPath(MobVats.CONFIG.toString()));
    }
}
