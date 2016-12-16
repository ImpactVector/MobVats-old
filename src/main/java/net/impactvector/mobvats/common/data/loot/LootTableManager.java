package net.impactvector.mobvats.common.data.loot;

import net.impactvector.mobvats.common.data.EnumEnchantKey;
import net.impactvector.mobvats.utils.Files;
import net.impactvector.mobvats.common.ModEventLog;
import net.impactvector.mobvats.utils.ItemStackHelper;
import net.impactvector.mobvats.utils.SerializationHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Swooped from Ipsis/Woot 11/28/2016
 * https://github.com/Ipsis/Woot
 */
public class LootTableManager {

    private HashMap<String, LootTable> lootMap;
    private List<ItemStack> blacklist;

    public LootTableManager() {

        lootMap = new HashMap<String, LootTable>();
        blacklist = new ArrayList<ItemStack>();
    }

    public void addToBlacklist(String s) {

        ItemStack itemStack = ItemStackHelper.getItemStackFromName(s);
        if (itemStack != null) {
            ModEventLog.warning("Loot blacklisted " + s);
            blacklist.add(itemStack);
        } else {
            ModEventLog.warning("Unknown Loot in blacklist " + s);
        }
    }

    public boolean isBlacklisted(ItemStack itemStack) {

        for (ItemStack cmp : blacklist) {
            if (ItemStack.areItemsEqualIgnoreDurability(cmp, itemStack))
                return true;
        }

        return false;
    }

    public void update(String mobVatsName, EnumEnchantKey key, List<EntityItem> mobDropList, boolean updateCount) {

        LootTable e = lootMap.get(mobVatsName);
        if (e == null) {
            e = new LootTable(mobVatsName);
            lootMap.put(mobVatsName, e);
        }

        e.update(key, mobDropList, updateCount);
    }

    public List<ItemStack> getDrops(String mobVatsName, EnumEnchantKey key) {

        List<ItemStack> drops = new ArrayList<ItemStack>();
        LootTable e = lootMap.get(mobVatsName);
        if (e != null)
            e.getDrops(key, drops);

        return drops;
    }

    public List<FullDropInfo> getFullDropInfo(String mobVatsName, EnumEnchantKey key) {

        List<FullDropInfo> drops = null;

        LootTable e = lootMap.get(mobVatsName);
        if (e != null)
            drops = e.getFullDropInfo(key);

        return drops;
    }

    public boolean isFull(String mobVatsName, EnumEnchantKey key) {

        LootTable e = lootMap.get(mobVatsName);
        if (e == null)
            return false;

        return e.isFull(key);
    }

    public boolean isEmpty(String mobVatsName, EnumEnchantKey key) {

        LootTable e = lootMap.get(mobVatsName);
        if (e == null)
            return true;

        return e.isEmpty(key);
    }

    /**
     * Commands
     */
    public void dumpMobs(ICommandSender sender) {

        StringBuilder sb = new StringBuilder();
        for (String mobName : lootMap.keySet())
            sb.append(mobName).append(" ");

        sender.addChatMessage(new TextComponentTranslation("commands.MobVats:mobvats.mobs.summary", sb));
    }

    public void dumpDrops(ICommandSender sender, String mobVatsName, boolean detail) {

        LootTable e = lootMap.get(mobVatsName);
        if (e != null) {
            for (EnumEnchantKey key : EnumEnchantKey.values()) {
                String s = e.getDrops(key, detail);
                sender.addChatMessage(new TextComponentTranslation("commands.MobVats:mobvats.loot.summary", mobVatsName, key.getDisplayName(), s));
            }
        }
    }

    public void flushMob(ICommandSender sender, String mobVatsName, EnumEnchantKey key) {

        LootTable e = lootMap.get(mobVatsName);
        if (e != null) {
            e.flush(key);
            sender.addChatMessage(new TextComponentTranslation("commands.MobVats:mobvats.flush.summary",
                    mobVatsName, key.getDisplayName()));
        }
    }

    public void flushAllMobs(ICommandSender sender) {

        for (LootTable table : lootMap.values()) {
            table.flush(EnumEnchantKey.NO_ENCHANT);
            table.flush(EnumEnchantKey.LOOTING_I);
            table.flush(EnumEnchantKey.LOOTING_II);
            table.flush(EnumEnchantKey.LOOTING_III);
        }

        sender.addChatMessage(new TextComponentTranslation("commands.MobVats:mobvats.flush.all.summary"));
    }

    public void dumpBlacklist(ICommandSender sender) {

        StringBuilder sb = new StringBuilder();
        for (ItemStack itemStack : blacklist)
            sb.append(String.format("%s ", itemStack.getDisplayName()));

        sender.addChatMessage(new TextComponentTranslation("commands.MobVats:mobvats.blacklist.summary", sb.toString()));
    }

    public void dumpStatus(ICommandSender sender) {

        for (String mob : lootMap.keySet()) {
            StringBuilder sb = new StringBuilder();
            for (EnumEnchantKey key : EnumEnchantKey.values()) {
                sb.append(key.getDisplayName());
                if (lootMap.get(mob).isEmpty(key))
                    sb.append(":running ");
                else
                    sb.append(":stopped ");
            }
            sender.addChatMessage(new TextComponentTranslation(
                    "commands.MobVats:mobvats.status.summary",
                    mob, sb.toString()));
        }
    }

    /**
     * Loot file handling
     */
    public void load() {

        ModEventLog.info("LootTableManager: Load loot statistics from " + Files.lootFile.toString());
        lootMap = null;
        try {
            lootMap = SerializationHelper.readHashMapFromFile(Files.lootFile);
        } catch (FileNotFoundException e) {
            /**
             * If it is no there then we start empty
             */
            lootMap = new HashMap<String, LootTable>();
        }
    }

    public void save() {

        ModEventLog.info("LootTableManager: Save loot statistics to " + Files.lootFile.toString());
        SerializationHelper.writeHashMapToFile(lootMap, Files.lootFile);
    }
}
