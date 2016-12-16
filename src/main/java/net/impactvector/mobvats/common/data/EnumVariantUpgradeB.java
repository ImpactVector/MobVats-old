package net.impactvector.mobvats.common.data;

import net.minecraft.util.IStringSerializable;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public enum EnumVariantUpgradeB implements IStringSerializable {

    EFFICIENCY_I,
    EFFICIENCY_II,
    EFFICIENCY_III,
    BLOODMAGIC_I,
    BLOODMAGIC_II,
    BLOODMAGIC_III;

    public int getMetadata() {

        return this.ordinal();
    }

    public static EnumVariantUpgradeB getFromMetadata(int meta) {

        if (meta < 0 || meta > values().length)
            return values()[0];

        return values()[meta];
    }

    @Override
    public String getName() {

        return toString();
    }

    @Override
    public String toString() {

        return super.toString().toLowerCase();
    }
}
