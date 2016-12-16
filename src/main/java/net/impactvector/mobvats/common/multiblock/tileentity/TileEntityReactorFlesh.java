package net.impactvector.mobvats.common.multiblock.tileentity;

import net.impactvector.mobvats.common.multiblock.MultiblockReactor;
import net.impactvector.mobvats.common.multiblock.helpers.MobAssembly;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.util.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * Created by dgerold on 12/1/2016.
 */
public class TileEntityReactorFlesh extends TileEntityReactorPartBase {
    public TileEntityReactorFlesh() {
        super();
    }


    @Override
    public boolean isGoodForFrame(IMultiblockValidator validatorCallback) {

        validatorCallback.setLastError("multiblock.validation.reactor.invalid_fuelrod_position", this.getPos());
        return false;
    }

    @Override
    public boolean isGoodForSides(IMultiblockValidator validatorCallback) {

        validatorCallback.setLastError("multiblock.validation.reactor.invalid_fuelrod_position", this.getPos());
        return false;
    }

    @Override
    public boolean isGoodForTop(IMultiblockValidator validatorCallback) {

        validatorCallback.setLastError("multiblock.validation.reactor.invalid_fuelrod_position", this.getPos());
        return false;
    }

    @Override
    public boolean isGoodForBottom(IMultiblockValidator validatorCallback) {

        validatorCallback.setLastError("multiblock.validation.reactor.invalid_fuelrod_position", this.getPos());
        return false;
    }

    @Override
    public boolean isGoodForInterior(IMultiblockValidator validatorCallback) {
        if (validatorCallback instanceof MultiblockReactor) {
            MultiblockReactor vat = (MultiblockReactor) validatorCallback;
            if (!vat.attachedFlesh.contains(this))
                vat.attachBlock(this);
            return true;
        }
        else
            return false;
    }

    public void linkToAssembly(final MobAssembly assembly) {
        this._assembly = assembly;
    }

    public MobAssembly getMobAssembly() {
        return this._assembly;
    }

    private MobAssembly _assembly;
}
