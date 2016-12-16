package net.impactvector.mobvats.common.multiblock.tileentity;

import net.impactvector.mobvats.common.multiblock.PartTier;
import net.impactvector.mobvats.init.ModBlocks;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorPart extends TileEntityReactorPartBase {

	public TileEntityReactorPart() {
		super();
	}

	@Override
	public boolean isGoodForFrame(IMultiblockValidator validatorCallback) {

		if (this.getBlockType() == ModBlocks.reactorCasing)
			return true;

		BlockPos position = this.getWorldPosition();

		validatorCallback.setLastError("multiblock.validation.reactor.invalid_frame_block", position.getX(), position.getY(), position.getZ());
		return false;
	}

	@Override
	public boolean isGoodForSides(IMultiblockValidator validatorCallback) {
		// All parts are valid for sides, by default
		return true;
	}

	@Override
	public boolean isGoodForTop(IMultiblockValidator validatorCallback) {
		// All parts are valid for the top, by default
		return true;
	}

	@Override
	public boolean isGoodForBottom(IMultiblockValidator validatorCallback) {
		// All parts are valid for the bottom, by default
		return true;
	}

	@Override
	public boolean isGoodForInterior(IMultiblockValidator validatorCallback) {

		validatorCallback.setLastError("multiblock.validation.reactor.invalid_part_for_interior", this.getWorldPosition());
		return false;
	}

	@Override
	public void onMachineActivated() {
	}

	@Override
	public void onMachineDeactivated() {
	}

	public PartTier getMachineTier() {
		return this.isConnected() ? this.getReactorController().getMachineTier() : PartTier.Legacy;
	}
}