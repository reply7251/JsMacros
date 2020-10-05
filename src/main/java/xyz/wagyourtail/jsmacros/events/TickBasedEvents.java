package xyz.wagyourtail.jsmacros.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import xyz.wagyourtail.jsmacros.api.events.EventArmorChange;
import xyz.wagyourtail.jsmacros.api.events.EventHeldItemChange;
import xyz.wagyourtail.jsmacros.api.events.EventItemDamage;
import xyz.wagyourtail.jsmacros.api.events.EventTick;
import xyz.wagyourtail.jsmacros.api.functions.FJsMacros;

public class TickBasedEvents {
    private static boolean initialized = false;
    private static ItemStack mainHand = ItemStack.EMPTY;
    private static ItemStack offHand = ItemStack.EMPTY;

    private static ItemStack footArmor = ItemStack.EMPTY;
    private static ItemStack legArmor = ItemStack.EMPTY;
    private static ItemStack chestArmor = ItemStack.EMPTY;
    private static ItemStack headArmor = ItemStack.EMPTY;
    
    public static boolean areEqual(ItemStack a, ItemStack b) {
        return (a.isEmpty() && b.isEmpty()) || (!a.isEmpty() && !b.isEmpty() && a.isItemEqualIgnoreDamage(b) && a.getCount() == b.getCount() && ItemStack.areTagsEqual(a, b) && a.getDamage() == b.getDamage());
    }
    
    public static boolean areTagsEqualIgnoreDamage(ItemStack a, ItemStack b) {
        if (a.isEmpty() && b.isEmpty()) {
            return true;
        } else if (!a.isEmpty() && !b.isEmpty()) {
            if (a.getTag() == null && b.getTag() == null) {
                return true;
            } else {
                CompoundTag at;
                CompoundTag bt;
                if (a.getTag() != null) at = a.getTag().copy();
                else at = new CompoundTag();
                if (b.getTag() != null) bt = b.getTag().copy();
                else bt = new CompoundTag();
                at.remove("Damage");
                bt.remove("Damage");
                return at.equals(bt);
            }
            
        } else {
            return false;
        }
    }
    
    public static boolean areEqualIgnoreDamage(ItemStack a, ItemStack b) {
        return (a.isEmpty() && b.isEmpty()) || (!a.isEmpty() && !b.isEmpty() && a.isItemEqualIgnoreDamage(b) && a.getCount() == b.getCount() && areTagsEqualIgnoreDamage(a, b));    
    }
    
    public static void init() {
        if (initialized) return;
        initialized = true;
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            
            FJsMacros.tickSynchronizer.tick();
            
            new EventTick();
            
            if (mc.player != null && mc.player.inventory != null) {
                PlayerInventory inv = mc.player.inventory;

                ItemStack newMainHand = inv.getMainHandStack();
                if (!areEqual(newMainHand, mainHand)) {
                    if (areEqualIgnoreDamage(newMainHand, mainHand)) {
                        new EventItemDamage(newMainHand, newMainHand.getDamage());
                    }
                    new EventHeldItemChange(newMainHand, mainHand, false);
                    mainHand = newMainHand.copy();
                }
                
                ItemStack newOffHand = inv.offHand.get(0);
                if (!areEqual(newOffHand, offHand)) {
                    if (areEqualIgnoreDamage(newOffHand, offHand)) {
                        new EventItemDamage(newOffHand, newOffHand.getDamage());
                    }
                    new EventHeldItemChange(newOffHand, offHand, true);
                    offHand = newOffHand.copy();
                }
                
                ItemStack newHeadArmor = inv.getArmorStack(3);
                if (!areEqual(newHeadArmor, headArmor)) {
                    if (areEqualIgnoreDamage(newHeadArmor, headArmor)) {
                        new EventItemDamage(newHeadArmor, newHeadArmor.getDamage());
                    }
                    new EventArmorChange("HEAD", newHeadArmor, headArmor);
                    headArmor = newHeadArmor.copy();
                }
                
                ItemStack newChestArmor = inv.getArmorStack(2);
                if (!areEqual(newChestArmor, chestArmor)) {
                    if (areEqualIgnoreDamage(newChestArmor, chestArmor)) {
                        new EventItemDamage(newChestArmor, newChestArmor.getDamage());
                    }
                    new EventArmorChange("CHEST", newChestArmor, chestArmor);
                    chestArmor = newChestArmor.copy();
                    
                }
                
                ItemStack newLegArmor = inv.getArmorStack(1);
                if (!areEqual(newLegArmor, legArmor)) {
                    if (areEqualIgnoreDamage(newLegArmor, legArmor)) {
                        new EventItemDamage(newLegArmor, newLegArmor.getDamage());
                    }
                    new EventArmorChange("LEGS", newLegArmor, legArmor);
                    legArmor = newLegArmor.copy();
                }
                
                ItemStack newFootArmor = inv.getArmorStack(0);
                if (!areEqual(newFootArmor, footArmor)) {
                    if (areEqualIgnoreDamage(newFootArmor, footArmor)) {
                        new EventItemDamage(newFootArmor, newFootArmor.getDamage());
                    }
                    new EventArmorChange("FEET", newFootArmor, footArmor);
                    footArmor = newFootArmor.copy();
                }
            }
        });
    }
}
