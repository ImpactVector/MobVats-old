package net.impactvector.mobvats.event;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.data.EnumEnchantKey;
import net.impactvector.mobvats.utils.FakePlayerPool;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerLivingDropsEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDropsEvent(LivingDropsEvent e) {

        /**
         * This is called for players and mobs
         */
        if (e.getEntity() instanceof EntityLiving) {

            DamageSource damageSource = e.getSource();
            if (damageSource != null) {

                if (FakePlayerPool.isOurFakePlayer(damageSource.getSourceOfDamage())) {
                    // Cancel the  factory kills so we dont spawn any loot in the world
                    e.setCanceled(true);
                }

                /**
                 *  Killed in one of our spawners, but we dont care which one.
                 *  We only store the drop information.
                 */
                String mobID = MobVats.mobRegistry.createMobVatsName((EntityLiving) e.getEntity());
                EnumEnchantKey key = EnumEnchantKey.getEnchantKey(e.getLootingLevel());
                MobVats.LOOT_TABLE_MANAGER.update(mobID, key, e.getDrops(), true);


            } else if (!MobVats.CONFIG.strictFactorySpawns) {

                /**
                 * Damn you slimes
                 * For Slime only care about size == 1 as that drops the loot
                 * For MagmaCube only care about size != 1 as they can drop the loot
                 * Remember MagmaCube extends Slime hence the order
                 */
                if (e.getEntity() instanceof EntityMagmaCube) {

                    if (((EntityMagmaCube) e.getEntity()).isSmallSlime())
                        return;

                } else if (e.getEntity() instanceof EntitySlime) {

                    if (((EntitySlime) e.getEntity()).getSlimeSize() != 1)
                        return;
                }

                /**
                 * Convert the non-spawner kill into a damage source if possible
                 */
                String mobID = MobVats.mobRegistry.createMobVatsName((EntityLiving) e.getEntity());
                EnumEnchantKey key = EnumEnchantKey.getEnchantKey(e.getLootingLevel());
                MobVats.LOOT_TABLE_MANAGER.update(mobID, key, e.getDrops(), true);
            }

//            /* handle decapitate */
//            if (damageSource != null && !e.isCanceled()) {
//                if (!(e.getSource().getEntity() instanceof EntityPlayer))
//                    return;
//
//                EntityPlayer player = (EntityPlayer)e.getSource().getEntity();
//                ItemStack equipped = player.getHeldItemMainhand();
//                if (!EnchantmentDecapitate.hasEnchantmentDecapitate(equipped))
//                    return;
//
//                EnchantmentDecapitate.handleLivingDrops(e);
//
//            }
        }
    }
}
