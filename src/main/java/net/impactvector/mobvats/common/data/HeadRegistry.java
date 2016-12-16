package net.impactvector.mobvats.common.data;

import net.impactvector.mobvats.common.multiblock.helpers.UpgradeManager;
import net.impactvector.mobvats.common.MobVats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public class HeadRegistry {

    HashMap<String, HeadConfig> headConfigHashMap = new HashMap<String, HeadConfig>();

    public void init() {

        initVanilla();
        initModded();
    }

    void initVanilla() {

        /**
         * Vanilla
         * Skull meta is in ItemSkull.java
         */
        String key;

        // EntitySkeleton.java
        key = MobVats.mobRegistry.createMobVatsName("none:Skeleton");
        headConfigHashMap.put(key, new HeadConfig(key, new ItemStack(Items.SKULL, 1, 0)));
        key = MobVats.mobRegistry.createMobVatsName("wither:Skeleton");
        headConfigHashMap.put(key, new HeadConfig(key, new ItemStack(Items.SKULL, 1, 1)));
        // EntityZombie.java
        key = MobVats.mobRegistry.createMobVatsName("none:Zombie");
        headConfigHashMap.put(key, new HeadConfig(key, new ItemStack(Items.SKULL, 1, 2)));
        // EntityCreeper.java
        key = MobVats.mobRegistry.createMobVatsName("none:Creeper");
        headConfigHashMap.put(key, new HeadConfig(key, new ItemStack(Items.SKULL, 1, 4)));
    }

    void initModded() {

        String key;

        // EnderIO Enderman SkullsA
        Item i = Item.getByNameOrId("EnderIO:blockEndermanSkull");
        if (i != null) {
            key = MobVats.mobRegistry.createMobVatsName("none:Enderman");
            headConfigHashMap.put(key, new HeadConfig(key, new ItemStack(i)));
        }

    }

    public ItemStack handleDecap(String mobVatsMobName, EnumSpawnerUpgrade upgrade) {

        SpawnerUpgrade u = UpgradeManager.getSpawnerUpgrade(upgrade);
        if (!u.isDecapitate())
            return null;

        HeadConfig headConfig = headConfigHashMap.get(mobVatsMobName);
        if (headConfig == null)
            return null;

        float chance = (float)u.getDecapitateChance() / 100.0F;
        if (MobVats.RANDOM.nextFloat() <= chance)
            return ItemStack.copyItemStack(headConfig.headStack);

        return null;
    }

    public ItemStack getVanillaHead(EntityLiving entityLiving) {

        ItemStack itemStack = null;
        if (entityLiving instanceof EntityCreeper) {
            itemStack = new ItemStack(Items.SKULL, 1, 4);
        } else if (entityLiving instanceof EntityZombie) {
            itemStack = new ItemStack(Items.SKULL, 1, 2);
        } else if (entityLiving instanceof EntitySkeleton) {
            EntitySkeleton entitySkeleton = (EntitySkeleton)entityLiving;
            if (entitySkeleton.getSkeletonType() == SkeletonType.NORMAL) {
                itemStack = new ItemStack(Items.SKULL, 1, 0);
            } else if (entitySkeleton.getSkeletonType() == SkeletonType.WITHER) {
                itemStack = new ItemStack(Items.SKULL, 1, 1);
            }
        }

        return itemStack;
    }

    class HeadConfig {

        String mobVatsMobName;
        ItemStack headStack;

        public HeadConfig(String mobVatsMobName, ItemStack headStack) {

            this.mobVatsMobName = mobVatsMobName;
            this.headStack = headStack.copy();
        }
    }
}
