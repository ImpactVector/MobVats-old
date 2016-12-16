package net.impactvector.mobvats.utils;

import net.impactvector.mobvats.common.ModEventLog;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.Field;

/**
 * Created by ImpactVector on 11/29/2016.
 * Extension class to expose a few necessary protected properties
 */
public class EntityHelper {

    public static int getExperienceValue(EntityLiving living)  {
        int xp = 0;
        try {
            Field field = ReflectionUtil.getField(((EntityLivingBase) living).getClass(), "experienceValue");
            ReflectionUtil.makeAccessible(field);
            xp = field.getInt(living);
        }
        catch (Exception e)
        {
            ModEventLog.error("Error accessing EntityLiving experienceValue: %d", e.getMessage() );
        }
        return xp;
    }

//    public void setAttackingPlayer(EntityPlayer player)
//    {
//        this.attackingPlayer = player;
//    }

    public static void setAttackingPlayer(EntityLivingBase living, EntityPlayer player) {
        try {
            Field field = ReflectionUtil.getField(((EntityLivingBase) living).getClass(), "attackingPlayer");
            ReflectionUtil.makeAccessible(field);
            field.set(living, player);
        }
        catch (Exception e)
        {
            ModEventLog.error("Error accessing EntityLivingBase attackingPlayer: %d", e.getMessage() );
        }
    }

//    public void setRecentlyHit(int val)
//    {
//        this.recentlyHit = val;
//    }

    public static void setRecentlyHit(EntityLivingBase living, int val) {
        try {
            Field field = ReflectionUtil.getField(((EntityLivingBase) living).getClass(), "recentlyHit");
            ReflectionUtil.makeAccessible(field);
            field.setInt(living, val);
        }
        catch (Exception e)
        {
            ModEventLog.error("Error accessing EntityLivingBase recentlyHit: %d", e.getMessage() );
        }
    }

}
