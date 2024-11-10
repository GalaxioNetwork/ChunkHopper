package me.bugsum.chunkHopper.utils;

import me.bugsum.chunkHopper.ChunkHopper;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PersistentData {

    public static boolean hasPersistentData(ItemStack itemStack, String paramString){
        if(!itemStack.hasItemMeta()){
            return false;
        }
        return itemStack.getItemMeta().getPersistentDataContainer().getKeys().contains(createNamespacedKey(paramString));
    }

    public static String getPersistentValue(ItemStack itemStack, String paramString){
        if(!itemStack.hasItemMeta()){
            return null;
        }
        return itemStack.getItemMeta().getPersistentDataContainer().get(createNamespacedKey(paramString), PersistentDataType.STRING);
    }

    public static NamespacedKey createNamespacedKey(String paramString){
        return new NamespacedKey(ChunkHopper.getInstance(), paramString);
    }

    public static void checkPersistentData(Hopper hopper, String paramString){
        if(!hopper.getPersistentDataContainer().getKeys().contains(createNamespacedKey(paramString))){
            hopper.getPersistentDataContainer().set(createNamespacedKey(paramString), PersistentDataType.STRING, "NONE");
            hopper.update();
        }
    }
}