package net.impactvector.mobvats.utils;

import net.minecraft.util.text.translation.I18n;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public class StringHelper {

    public static String localize(String s) {

        return I18n.translateToLocal(s);
    }
}
