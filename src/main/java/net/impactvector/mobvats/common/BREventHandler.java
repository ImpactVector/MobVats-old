package net.impactvector.mobvats.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BREventHandler {

	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save saveEvent) {

		if (MobVats.CONFIG.enableWorldGen) {

			NBTTagCompound saveData = saveEvent.getData();
			
			saveData.setInteger("BigReactorsWorldGen", MobVats.WORLDGEN_VERSION);
			saveData.setInteger("BigReactorsUserWorldGen", MobVats.CONFIG.userWorldGenVersion);
		}
	}
	
	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load loadEvent) {

		NBTTagCompound loadData = loadEvent.getData();
		int dimensionId = loadEvent.getWorld().provider.getDimension();

		if (!MobVats.CONFIG.enableWorldRegeneration || !MobVats.CONFIG.enableWorldGen ||
			(loadData.getInteger("BigReactorsWorldGen") == MobVats.WORLDGEN_VERSION &&
			 loadData.getInteger("BigReactorsUserWorldGen") == MobVats.CONFIG.userWorldGenVersion) ||
			!MobVats.WHITELIST_WORLDGEN_ORES.shouldGenerateIn(dimensionId))
			return;

		MobVats.TICK_HANDLER.addRegenChunk(dimensionId, loadEvent.getChunk().getChunkCoordIntPair());
	}
}
