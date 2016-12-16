package net.impactvector.mobvats.plugins.top;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.ModEventLog;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import javax.annotation.Nullable;

/**
 * Straight from McJJty's tutorial
 */
public class TOPCompat {

    private static boolean registered;

    public static void register() {

        if (!registered) {
            registered = true;
            FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe",
                    "net.impactvector.mobvats.plugins.top.TOPCompat$GetTheOneProbe");
        }
    }

    public static class GetTheOneProbe implements com.google.common.base.Function<ITheOneProbe, Void> {

        public static ITheOneProbe probe;

        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {

            probe = theOneProbe;
            ModEventLog.info("Enabled The One Probe support");

            probe.registerProvider(new IProbeInfoProvider() {
                @Override
                public String getID() {
                    return MobVats.MODID;
                }

                @Override
                public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

                    if (blockState.getBlock() instanceof ITOPInfoProvider) {
                        ITOPInfoProvider provider = (ITOPInfoProvider)blockState.getBlock();
                        provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
                    }
                }
            });
            return null;
        }
    }
}
