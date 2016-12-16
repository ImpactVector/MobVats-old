package net.impactvector.mobvats.common.data;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.ModEventLog;
import net.impactvector.mobvats.utils.StringHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public class MobRegistry {

    public static final String INVALID_MOB_NAME = "InvalidMob";
    public static final String ENDER_DRAGON = "MobVats:none:EnderDragon";
    HashMap<String, MobInfo> mobInfoHashMap = new HashMap<String, MobInfo>();

    Map<String, Integer> mobCostMap = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);

    public static String getMcName(String mobVatsName) {

        // Woot:tag:mcname
        return mobVatsName.substring(mobVatsName.lastIndexOf(':') + 1);
    }

    public boolean isValidMobName(String mobName) {

        return mobName != null && !mobName.equals(INVALID_MOB_NAME) && !mobName.equals("");
    }

    public void addCosting(String mobName, int cost) {

        if (mobName != null && cost > 0)
            mobCostMap.put(mobName, cost);
    }

    public String createMobVatsName(EntityLiving entityLiving) {

        String name = EntityList.getEntityString(entityLiving);
        if (entityLiving instanceof EntitySkeleton) {
            if (((EntitySkeleton) entityLiving).getSkeletonType() == SkeletonType.WITHER)
                name = "wither:" + name;
            else
                name = "none:" + name;
        } else if (entityLiving instanceof EntityCreeper) {
            if (((EntityCreeper) entityLiving).getPowered() == true)
                name = "charged:" + name;
            else
                name = "none:" + name;
        } else if (entityLiving instanceof EntityGuardian) {
            if (((EntityGuardian)entityLiving).isElder() == true)
                name = "elder:" + name;
            else
                name = "none:" + name;
        } else {
            name = "none:" + name;
        }

        // Name is of the form "Woot:<tag>:mcname"
        return  MobVats.MODID + ":" + name;
    }

    public String createMobVatsName(String name) {

        return MobVats.MODID + ":" + name;
    }

    private String createDisplayName(EntityLiving entityLiving) {

        if (entityLiving instanceof  EntitySkeleton) {
            if (((EntitySkeleton) entityLiving).getSkeletonType() == SkeletonType.WITHER)
                return StringHelper.localize("entity.MobVats:witherskelly.name");
            else
                return entityLiving.getName();
        } else if (entityLiving instanceof EntityCreeper) {
            if (((EntityCreeper) entityLiving).getPowered() == true)
                return StringHelper.localize("entity.MobVats:chargedcreeper.name");
            else
                return entityLiving.getName();
        } else if (entityLiving instanceof EntityGuardian) {
            if (((EntityGuardian)entityLiving).isElder() == true)
                return StringHelper.localize("entity.MobVats:elderguardian.name");
            else
                return entityLiving.getName();
        } else {
            return entityLiving.getName();
        }
    }

    public boolean isPrismValid(String mobVatsName) {

        if (isEnderDragon(mobVatsName))
            return false;

        if (isCyberware(mobVatsName))
            return false;

        String[] mobList;
        if (MobVats.CONFIG.useSyringeWhitelist)
            mobList = MobVats.CONFIG.syringeWhitelist;
        else
            mobList = MobVats.CONFIG.syringeBlacklist;

        for (int i = 0; i < mobList.length; i++) {
            if (mobList[i].equalsIgnoreCase(mobVatsName)) {
                return mobList == MobVats.CONFIG.syringeWhitelist;
            }
        }

        // not on blacklist: valid
        // not on whitelist: invalid
        return mobList == MobVats.CONFIG.syringeBlacklist;
    }

    public void cmdDumpPrism(ICommandSender sender) {

        //sender.addChatMessage(new TextComponentTranslation("commands.MobVats:mobvats.dump.blacklist.summary", ENDER_DRAGON));
        if (MobVats.CONFIG.useSyringeWhitelist) {
            for (int i = 0; i < MobVats.CONFIG.syringeWhitelist.length; i++)
                sender.addChatMessage(new TextComponentTranslation("commands.MobVats:mobvats.dump.prism.whitelist.summary", MobVats.CONFIG.syringeWhitelist[i]));
        } else {
            for (int i = 0; i < MobVats.CONFIG.syringeBlacklist.length; i++)
                sender.addChatMessage(new TextComponentTranslation("commands.MobVats:mobvats.dump.prism.blacklist.summary", MobVats.CONFIG.syringeBlacklist[i]));
        }
    }

    public void cmdDumpCosts(ICommandSender sender) {

        StringBuilder sb = new StringBuilder();
        for (String name : mobCostMap.keySet())
            sender.addChatMessage(new TextComponentTranslation("commands.MobVats:mobvats.cost.summary",
                    name, mobCostMap.get(name)));

    }

    public String onEntityLiving(EntityLiving entityLiving) {

        String name = EntityList.getEntityString(entityLiving);
        String mobVatsName = createMobVatsName(entityLiving);
        String displayName = createDisplayName(entityLiving);
        AxisAlignedBB box = entityLiving.getRenderBoundingBox();
        BlockPos mobSize = new BlockPos(box.maxX - box.minX, box.maxY - box.minY, box.maxZ - box.minZ);
        if (!mobInfoHashMap.containsKey(mobVatsName)) {
            MobInfo info = new MobInfo(mobVatsName, displayName, mobSize);
            mobInfoHashMap.put(mobVatsName, info);
        } else {
            MobInfo mobInfo = mobInfoHashMap.get(mobVatsName);
            if (mobInfo.displayName.equals(INVALID_MOB_NAME))
                mobInfo.displayName = displayName;
        }

        return mobVatsName;
    }

    public void addMapping(String mobVatsName, int xp, BlockPos size) {

        if (!mobInfoHashMap.containsKey(mobVatsName)) {
            MobInfo info = new MobInfo(mobVatsName, xp, size);
            mobInfoHashMap.put(mobVatsName, info);
        } else {
            mobInfoHashMap.get(mobVatsName).setXp(xp);
        }
    }

    public boolean isKnown(String mobVatsName) {

        return mobInfoHashMap.containsKey(mobVatsName);
    }

    void extraEntitySetup(MobInfo mobInfo, Entity entity, World world) {

        if (isWitherSkeleton(mobInfo.mobVatsMobName, entity)) {
            //((EntitySkeleton) entity).setSkeletonType(SkeletonType.WITHER);
            ((EntitySkeleton) entity).setSkeletonType(SkeletonType.WITHER);
        } else if (isSlime(mobInfo.mobVatsMobName, entity)) {
            if (((EntitySlime)entity).getSlimeSize() != 1)
                setSlimeSize((EntitySlime)entity, 1);
        } else if (isMagmaCube(mobInfo.mobVatsMobName, entity)) {
            if (((EntitySlime)entity).getSlimeSize() == 1)
                setSlimeSize((EntitySlime)entity, 2);
        } else if (isChargedCreeper(mobInfo.mobVatsMobName, entity)) {
            entity.onStruckByLightning(new EntityLightningBolt(world,
                    entity.getPosition().getX(),
                    entity.getPosition().getY(),
                    entity.getPosition().getZ(), true));
        } else if (isElderGuardian(mobInfo.mobVatsMobName, entity)) {
            ((EntityGuardian)entity).setElder(true);
        }
    }

    private void setSlimeSize(EntitySlime entitySlime, int size) {

        String[] methodNames = new String[]{ "func_70799_a", "setSlimeSize" };

        try {
            Method m = ReflectionHelper.findMethod(EntitySlime.class, null, methodNames, int.class);
            m.invoke(entitySlime, size);
        } catch (Throwable e){
            ModEventLog.warning("Reflection EntitySlime.setSlimeSize failed");
        }
    }

    boolean isCyberware(String mobVatsName) {

        return mobVatsName.toLowerCase().contains("cyberware");
    }

    boolean isEnderDragon(String mobVatsName) {

        return ENDER_DRAGON.equals(mobVatsName);
    }

    boolean isWitherSkeleton(String mobVatsName, Entity entity) {

        return entity instanceof EntitySkeleton && mobVatsName.equals(MobVats.MODID + ":" + "wither:Skeleton");
    }

    boolean isSlime(String mobVatsName, Entity entity) {

        return entity instanceof EntitySlime && mobVatsName.equals(MobVats.MODID + ":" + "none:Slime");
    }

    boolean isMagmaCube(String mobVatsName, Entity entity) {

        return entity instanceof EntityMagmaCube && mobVatsName.equals(MobVats.MODID + ":" + "none:LavaSlime");
    }

    boolean isChargedCreeper(String mobVatsName, Entity entity) {

        return entity instanceof EntityCreeper && mobVatsName.equals(MobVats.MODID + ":" + "charged:Creeper");
    }

    boolean isElderGuardian(String mobVatsName, Entity entity) {

        return entity instanceof EntityGuardian && mobVatsName.equals(MobVats.MODID + ":" + "elder:Guardian");
    }

    public Entity createEntity(String mobVatsName, World world) {

        if (!isKnown(mobVatsName))
            addMapping(mobVatsName, -1, null);

        Entity entity = EntityList.createEntityByName(mobInfoHashMap.get(mobVatsName).getMcMobName(), world);
        if (entity != null)
            extraEntitySetup(mobInfoHashMap.get(mobVatsName), entity, world);

        return entity;
    }

    public boolean hasXp(String mobVatsName) {

        return mobInfoHashMap.containsKey(mobVatsName) && mobInfoHashMap.get(mobVatsName).hasXp();

    }

    public int getSpawnXp(String mobVatsName) {

        Integer cost = MobVats.mobRegistry.mobCostMap.get(mobVatsName);
        if (cost != null)
            return cost;

        if (mobInfoHashMap.containsKey(mobVatsName))
            return mobInfoHashMap.get(mobVatsName).getDeathXp();

        return 1;
    }

    public BlockPos getSize(String mobVatsName) {

        if (mobInfoHashMap.containsKey(mobVatsName))
            return mobInfoHashMap.get(mobVatsName).getSize();

        return new BlockPos(1,2,1);
    }

    public int getDeathXp(String mobVatsName) {

        if (mobInfoHashMap.containsKey(mobVatsName))
            return mobInfoHashMap.get(mobVatsName).getDeathXp();

        return 1;
    }

    public String getDisplayName(String mobVatsName) {

        if (mobInfoHashMap.containsKey(mobVatsName))
            return mobInfoHashMap.get(mobVatsName).getDisplayName();

        return INVALID_MOB_NAME;
    }

    public class MobInfo {

        public final static int MIN_XP_VALUE = 1;

        String mobVatsMobName;
        String mcMobName;
        String displayName;
        int deathXp;
        BlockPos size;

        private MobInfo() { }

        public MobInfo(String mobVatsMobName, int mobXp, BlockPos size) {

           this(mobVatsMobName, INVALID_MOB_NAME, mobXp, size);
        }

        public MobInfo(String mobVatsMobName, String displayName, BlockPos size) {

            this(mobVatsMobName, displayName, -1, size);
        }

        public MobInfo(String mobVatsMobName, String displayName, int mobXp, BlockPos size) {

            this.mobVatsMobName = mobVatsMobName;
            this.mcMobName = MobRegistry.getMcName(mobVatsMobName);
            this.displayName = displayName;
            this.size = size;
            setXp(mobXp);
        }

        public String getMcMobName() { return mcMobName; }
        public String getDisplayName() { return displayName; }
        public int getDeathXp() { return deathXp; }
        public boolean hasXp() { return deathXp != -1; }
        public BlockPos getSize() { return size; }

        public void setXp(int mobXp) {

            if (mobXp == 0)
                this.deathXp = MIN_XP_VALUE;
            else
                this.deathXp = mobXp;
        }
    }
}

/**
 * Vanilla Mobs And Default Remapping
 *
 *
 * Tier I [Bone/Flesh]
 * ------
 *
 * Bat - 1
 * Chicken - 1
 * Cow - 1
 * Horse - 1
 * Mooshroom - 1
 * Ocelot - 1
 * Pig - 1
 * Rabbit - 1
 * Sheep - 1
 * Squid - 1
 * Villager - 1
 * Wolf - 1
 * Shulker - 5
 * Silverfish  - 5
 * Skeleton - 5
 * Slime - 4
 * Spider - 5
 * Zombie - 5
 * Cave Spider - 5
 * Creeper - 5
 * Endermite - 3
 * Giant Zombie - 5
 *
 * Tier II [Blaze]
 * -------
 *
 * Magma Cube - 2
 * Witch - 5
 * Blaze - 10
 * Ghast - 5
 * Zombie Pigman - 5
 *
 * Tier III [Enderpearl]
 * --------
 *
 * Iron Golem - 1
 * Enderman - 5
 * Guardian - 10
 * Wither Skeleton - 5
 *
 * Tier IV [Wither Star]
 * -------
 *
 * Wither - 50
 * Dragon - 12000(?)
 *
 *
 */
