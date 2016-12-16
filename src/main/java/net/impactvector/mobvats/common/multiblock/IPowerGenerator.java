package net.impactvector.mobvats.common.multiblock;

public interface IPowerGenerator {

    long getEnergyCapacity();

    long getEnergyStored();

    long extractEnergy(long maxEnergy, boolean simulate);

    PowerSystem getPowerSystem();
}
