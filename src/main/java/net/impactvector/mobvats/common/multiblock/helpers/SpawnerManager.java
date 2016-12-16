package net.impactvector.mobvats.common.multiblock.helpers;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorController;
import net.impactvector.mobvats.common.multiblock.PartTier;
import net.impactvector.mobvats.common.data.*;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorHead;
import net.impactvector.mobvats.utils.EntityHelper;
import net.impactvector.mobvats.utils.FakePlayerPool;
import net.impactvector.mobvats.utils.StringHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.*;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public class SpawnerManager {

    public static class SpawnReq {

        int totalRf;
        int spawnTime;
        int rfPerTick;
        BlockPos size;

        public SpawnReq(int totalRf, int spawnTime) {
            this.totalRf = totalRf;
            this.spawnTime = spawnTime;

            /**
             * Depending on the values, the resultant value can exceed the totalRf.
             * We therefore update the totalRf.
             * Blame rounding. It's a bitch :)
             */
            this.rfPerTick = calcRfPerTick();
            this.totalRf = this.rfPerTick * this.spawnTime;
        }

        int calcRfPerTick() {

            if (spawnTime > 0) {
                float rfpertick = (float)totalRf / spawnTime;
                return MathHelper.ceiling_float_int(rfpertick);
            }
            return 1;
        }

        public int getTotalRf() {
            return totalRf;
        }

        public int getSpawnTime() { return spawnTime; }

        public int getRfPerTick() { return rfPerTick; }

        @Override
        public String toString() {
            return totalRf + "RF " + spawnTime + " ticks @ " + rfPerTick + "RF/tick";
        }
    }

    public SpawnReq getSpawnReq(String mobName, TileEntityReactorHead te) {

        if (!MobVats.mobRegistry.isValidMobName(mobName))
            return null;

        int xpLevel = getSpawnXp(mobName, te);

        int baseRF = 60;
//        if (tier == EnumMobFactoryTier.TIER_ONE)
//            baseRF = Settings.tierIRFtick;
//        else if (tier == EnumMobFactoryTier.TIER_TWO)
//            baseRF = Settings.tierIIRFtick;
//        else if (tier == EnumMobFactoryTier.TIER_THREE)
//            baseRF = Settings.tierIIIRFtick;
//        else
//            baseRF = Settings.tierIVRFtick;

        //int xpPerTick = MobVats.CONFIG.DEF_XP_RF_TICK;
        int xpPerTick = 16;

        int mobCount = 1; //upgradeSetup.hasMassUpgrade() ? UpgradeManager.getSpawnerUpgrade(upgradeSetup.getMassUpgrade()).getMass() : 1;
        int spawnTime = 320; //upgradeSetup.hasRateUpgrade() ? UpgradeManager.getSpawnerUpgrade(upgradeSetup.getRateUpgrade()).getSpawnRate() : Settings.baseRateTicks;

        int RFt = 80; //baseRF * MobVats.CONFIG.Spawner.DEF_BASE_RATE_TICKS;
        int RFmob = xpPerTick * xpLevel * 320;//MobVats.CONFIG.Spawner.DEF_BASE_RATE_TICKS;
        int RFupgrade = 320; //upgradeSetup.getRfPerTickCost() * Settings.Spawner.DEF_BASE_RATE_TICKS;
        int RFcount = RFmob + ((int)((float)RFmob * 0.33)* (mobCount - 1));
        int RFtotal = RFt + RFcount + RFupgrade;

        /*
        LogHelper.info("Power for " + mobName);
        LogHelper.info(String.format("Power baseRf:%d DEF_BASE_RATE_TICKS:%d xpPerTick:%d xpLevel:%d",
                baseRF, Settings.Spawner.DEF_BASE_RATE_TICKS,  xpPerTick, xpLevel));
        LogHelper.info(String.format("Power upgradeRFtick:%d", upgradeSetup.getRfPerTickCost()));
        LogHelper.info(String.format("Power RFt:%d RFmob:%d RFupgrade:%d RFcount:%d",  RFt, RFmob, RFupgrade, RFcount));
        LogHelper.debug("Power total " + RFtotal); */

//        if (upgradeSetup.hasEfficiencyUpgrade()) {
//            int f = UpgradeManager.getSpawnerUpgrade(upgradeSetup.getEfficiencyUpgrade()).getEfficiency();
//            int saving = (int)((RFtotal / 100.0F) * f);
//            RFtotal -= saving;
//            if (RFtotal < 0)
//                RFtotal = 1;
//        }

        return new SpawnReq(RFtotal, spawnTime);
    }

    public int getSpawnXp(String mobName, TileEntity te) {

        if (!MobVats.mobRegistry.hasXp(mobName)) {
            Entity entity = spawnEntity(mobName, te.getWorld(), te.getPos());
            if (entity != null) {
                //int xp = ((EntityLiving)entity).getExperienceValue();
                int xp = EntityHelper.getExperienceValue((EntityLiving)entity);
                AxisAlignedBB box = entity.getRenderBoundingBox();
                MobVats.mobRegistry.addMapping(mobName, xp, new BlockPos(box.maxX - box.minX, box.maxY - box.minY, box.maxZ - box.minZ));
            }
            entity = null;
        }

        return MobVats.mobRegistry.getSpawnXp(mobName);
    }

    private void createLootBox(World world, BlockPos originPos) {

        for (int y = 1; y <= 3; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos p = new BlockPos(originPos.getX() + x, y, originPos.getZ() + z);
                    if (!world.isAirBlock(p))
                        world.setBlockToAir(p);
                }
            }
        }
    }

    private Entity spawnEntity(String mobName, World world, BlockPos blockPos) {

        Entity entity = MobVats.mobRegistry.createEntity(mobName, world);

        if (entity != null) {
            // Allow custom spawn mechanics from other mods
            if (!ForgeEventFactory.doSpecialSpawn((EntityLiving)entity, world, blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                ((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(blockPos), null);

            BlockPos originPos = new BlockPos(blockPos.getX(), 1, blockPos.getZ());
            entity.setPosition(originPos.getX(), originPos.getY(), originPos.getZ());

            /**
             * Create a box to hold any of the loot dropped by any mod not using
             * the LivingEventDrops droplist.
             *
             * If not then the loot can be spawned in a block causing it to be accelerated upwards
             */
            createLootBox(world, originPos);

            /**
             * Random loot drop needs a non-zero recentlyHit value
             */
            EntityHelper.setRecentlyHit((EntityLiving)entity, 100);
        }

        return entity;
    }

    public void spawn(String mobName, EnumEnchantKey enchantKey, World world, BlockPos blockPos) {

        Entity entity = spawnEntity(mobName, world, blockPos);
        if (entity == null)
            return;

        FakePlayer fakePlayer = FakePlayerPool.getFakePlayer((WorldServer)world, enchantKey);
        if (fakePlayer == null)
            return;

        /**
         * BUG0022 - Need to set the attackingPlayer or the 1.9 loot tables will not
         * give us all the drops, as some are conditional on killed_by_player
         */
        EntityHelper.setAttackingPlayer((EntityLivingBase)entity,fakePlayer);
//        entity.setAttackingPlayer(fakePlayer);
        ((EntityLivingBase) entity).onDeath(DamageSource.causePlayerDamage(fakePlayer));
    }

    int calcDeathXp(String mobName, SpawnerUpgrade upgrade) {

        if (upgrade == null)
            return 0;

        int base = MobVats.mobRegistry.getDeathXp(mobName);
//        if (upgrade.getUpgradeType() == EnumSpawnerUpgrade.XP_I)
//            return base;
//
//        float boost = (float)upgrade.getXpBoost();
//        int extra = (int)((base / 100.0F) * boost);
//        if (extra < 1)
//            extra = 1;
        return base; //+ extra;
    }

    void enchantItemStack(ItemStack itemStack, DifficultyInstance difficulty) {

        /* Same way as vanilla setEnchantmentBasedOnDifficulty */
        float f = difficulty.getClampedAdditionalDifficulty();

        boolean allowTreasure = false;
        EnchantmentHelper.addRandomEnchantment(MobVats.RANDOM, itemStack, (int)(5.0F + f * (float)MobVats.RANDOM.nextInt(18)), allowTreasure);
    }

    boolean shouldEnchant(DifficultyInstance difficulty) {

        return  MobVats.RANDOM.nextFloat() < (0.25F  * difficulty.getClampedAdditionalDifficulty());
    }

    public SpawnLoot getSpawnerLoot(String mobName, UpgradeSetup upgradeSetup, DifficultyInstance difficulty) {

        SpawnLoot spawnLoot = new SpawnLoot();
        int mobCount = 1;//Settings.Spawner.DEF_BASE_MOB_COUNT;
        if (upgradeSetup.hasMassUpgrade())
            mobCount = UpgradeManager.getSpawnerUpgrade(upgradeSetup.getMassUpgrade()).getMass();

        SpawnerUpgrade xpUpgrade = null;
        if (upgradeSetup.hasXpUpgrade())
            xpUpgrade = UpgradeManager.getSpawnerUpgrade(upgradeSetup.getXpUpgrade());

        for (int i = 0; i < mobCount; i++) {

            List<ItemStack> dropList = MobVats.LOOT_TABLE_MANAGER.getDrops(mobName, upgradeSetup.getEnchantKey());
            if (!dropList.isEmpty()) {

                boolean shouldEnchant = shouldEnchant(difficulty);

                /* cycle a present enchant */
                for (ItemStack stack : dropList) {
                    if (stack.isItemEnchanted()) {
                        shouldEnchant = false;
                        stack.getTagCompound().removeTag("ench");
                        enchantItemStack(stack, difficulty);
                    }
                }

                /* force add an enchant */
                if (shouldEnchant) {
                    for (ItemStack stack : dropList) {
                        if (stack.isItemEnchantable()) {
                            enchantItemStack(stack, difficulty);
                            break;
                        }
                    }
                }
            }

            spawnLoot.drops.addAll(dropList);
            spawnLoot.xp += calcDeathXp(mobName, xpUpgrade);
            if (upgradeSetup.hasDecapitateUpgrade()) {
                ItemStack headStack = MobVats.headRegistry.handleDecap(mobName, upgradeSetup.getDecapitateUpgrade());
                if (headStack != null)
                    spawnLoot.drops.add(headStack);
            }
        }

        return spawnLoot;
    }

    public class SpawnLoot {

        List<ItemStack> drops;
        int xp;

        public SpawnLoot() {

            this.xp = 0;
            this.drops = new ArrayList<ItemStack>();
        }

        public int getXp() { return this.xp; }
        public List<ItemStack> getDropList() { return this.drops; }
    }
}
