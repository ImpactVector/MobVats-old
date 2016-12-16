package net.impactvector.mobvats.common.data;

import net.impactvector.mobvats.api.IReactorFuel;
import net.minecraftforge.fluids.Fluid;

public class ReactorFuel implements IReactorFuel {
	protected Fluid referenceFluid;
	protected Fluid productFluid;
	protected int color;
	protected boolean isFuel;
	protected boolean isWaste;
	
	public ReactorFuel(Fluid fluid, int color, boolean isFuel, boolean isWaste) {
		this.referenceFluid = fluid;
		this.color = color;
		this.productFluid = null;
		this.isFuel = isFuel;
		this.isWaste = isWaste;
	}
	
	public ReactorFuel(Fluid fluid, int color, boolean isFuel, boolean isWaste, Fluid productFluid) {
		this(fluid, color, isFuel, isWaste);
		this.productFluid = productFluid;
	}

	@Override
	public boolean isFuelEqual(IReactorFuel otherFuel) {
		return referenceFluid == otherFuel.getReferenceFluid();
	}

	@Override
	public boolean isFuelEqual(Fluid fluid) {
		return referenceFluid == fluid;
	}

	@Override
	public Fluid getReferenceFluid() {
		return referenceFluid;
	}

	@Override
	public int getFuelColor() {
		return color;
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof IReactorFuel) {
			return isFuelEqual((IReactorFuel)arg0);
		}
		else if(arg0 instanceof Fluid) {
			return isFuelEqual((Fluid)arg0);
		}

		return false;
	}

	@Override
	public Fluid getProductFluid() {
		return productFluid;
	}

	@Override
	public boolean isFuel() {
		return this.isFuel;
	}

	@Override
	public boolean isWaste() {
		return this.isWaste;
	}
}
