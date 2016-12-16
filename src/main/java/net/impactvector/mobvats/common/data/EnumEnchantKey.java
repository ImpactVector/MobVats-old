package net.impactvector.mobvats.common.data;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public enum EnumEnchantKey {
    NO_ENCHANT("No Enchant"),
    LOOTING_I("Looting I"),
    LOOTING_II("Looting II"),
    LOOTING_III("Looting III");

    private String s;
    EnumEnchantKey(String s) {
        this.s = s;
    }

    public String getDisplayName() {
        return s;
    }

    public static EnumEnchantKey getEnchantKey(int v) {

        if (v < 0 || v > values().length)
            v = 0;

        return values()[v];
    }
}
