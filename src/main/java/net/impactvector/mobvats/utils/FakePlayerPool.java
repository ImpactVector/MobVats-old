package net.impactvector.mobvats.utils;

import com.mojang.authlib.GameProfile;
import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.data.EnumEnchantKey;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.HashMap;
import java.util.UUID;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public class FakePlayerPool {

    static GameProfile MOBVATS_GAME_PROFILE = new GameProfile(UUID.nameUUIDFromBytes("[MobVats]".getBytes()), "[MobVats]");
    static GameProfile MOBVATS_GAME_PROFILE_I = new GameProfile(UUID.nameUUIDFromBytes("[MobVats_I]".getBytes()), "[MobVats I]");
    static GameProfile MOBVATS_GAME_PROFILE_II = new GameProfile(UUID.nameUUIDFromBytes("[MobVats_II]".getBytes()), "[MobVats II]");
    static GameProfile MOBVATS_GAME_PROFILE_III = new GameProfile(UUID.nameUUIDFromBytes("[MobVats_III]".getBytes()), "[MobVats III]");

    static HashMap<EnumEnchantKey, FakePlayer> fakePlayerHashMap;

    static void init(WorldServer world) {

        fakePlayerHashMap  = new HashMap<EnumEnchantKey, FakePlayer>();

        FakePlayer fakePlayer = FakePlayerFactory.get(world, MOBVATS_GAME_PROFILE);
        FakePlayer fakePlayerI = FakePlayerFactory.get(world, MOBVATS_GAME_PROFILE_I);
        FakePlayer fakePlayerII = FakePlayerFactory.get(world, MOBVATS_GAME_PROFILE_II);
        FakePlayer fakePlayerIII = FakePlayerFactory.get(world, MOBVATS_GAME_PROFILE_III);
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        ItemStack swordI = new ItemStack(Items.DIAMOND_SWORD);
        ItemStack swordII = new ItemStack(Items.DIAMOND_SWORD);
        ItemStack swordIII = new ItemStack(Items.DIAMOND_SWORD);

        swordI.addEnchantment(Enchantment.getEnchantmentByLocation("looting"), 1);//MobVats.CONFIG.lootingILevel);
        swordII.addEnchantment(Enchantment.getEnchantmentByLocation("looting"), 2);//MobVats.CONFIG.lootingIILevel);
        swordIII.addEnchantment(Enchantment.getEnchantmentByLocation("looting"), 3);//MobVats.CONFIG.lootingIIILevel);

        fakePlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, sword);
        fakePlayerI.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, swordI);
        fakePlayerII.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, swordII);
        fakePlayerIII.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, swordIII);

        fakePlayerHashMap.put(EnumEnchantKey.NO_ENCHANT, fakePlayer);
        fakePlayerHashMap.put(EnumEnchantKey.LOOTING_I, fakePlayerI);
        fakePlayerHashMap.put(EnumEnchantKey.LOOTING_II, fakePlayerII);
        fakePlayerHashMap.put(EnumEnchantKey.LOOTING_III, fakePlayerIII);
    }

    public static FakePlayer getFakePlayer(WorldServer world, EnumEnchantKey enumEnchantKey) {

        if (fakePlayerHashMap == null)
            init(world);

        return fakePlayerHashMap.get(enumEnchantKey);
    }

    public static boolean isOurFakePlayer(Entity entity) {

        if (!(entity instanceof FakePlayer))
            return false;

        FakePlayer fakePlayer = (FakePlayer)entity;
        UUID uuid = fakePlayer.getUniqueID();

        return (MOBVATS_GAME_PROFILE.getId().equals(uuid) ||
                MOBVATS_GAME_PROFILE_I.getId().equals(uuid) ||
                MOBVATS_GAME_PROFILE_II.getId().equals(uuid) ||
                MOBVATS_GAME_PROFILE_III.getId().equals(uuid));
    }
}
