package net.impactvector.mobvats.common.multiblock.block;

import net.minecraft.util.IStringSerializable;

public enum PowerTapState implements IStringSerializable {

    Connected,
    Disconnected;

    PowerTapState() {

        this._name = this.name().toLowerCase();
    }

    @Override
    public String toString() {

        return this._name;
    }

    @Override
    public String getName() {

        return this._name;
    }

    private final String _name;
}
