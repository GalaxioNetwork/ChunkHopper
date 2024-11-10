package me.bugsum.chunkHopper.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemSerializer {

    public static String serializeLocation(Location location){
        return location.getWorld().getName() + ";" + (int) location.getX() + ";" + (int) location.getY() + ";" + (int) location.getZ();
    }

    public static Location deserializeLocation(String[] paramString){
        return new Location(Bukkit.getWorld(paramString[0]), Integer.parseInt(paramString[1]), Integer.parseInt(paramString[2]), Integer.parseInt(paramString[3]));
    }

    public static String serializeItemStacks(List<ItemStack> itemStacks){
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(itemStacks.size());
            for(ItemStack itemStack : itemStacks){
                dataOutput.writeObject(itemStack);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }catch (Exception e){
            throw new IllegalStateException("&cFailed to serialize filtered-items!");
        }
    }

    public static List<ItemStack> deserializeItemStacks(String paramString){
        try{
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(paramString));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            List<ItemStack> itemStacks = new ArrayList<>();
            int size = dataInput.readInt();
            for(int i = 0; i<size; i++){
                ItemStack itemStack = (ItemStack) dataInput.readObject();
                itemStacks.add(itemStack);
            }
            dataInput.close();
            return itemStacks;
        }catch (Exception e){
            throw new IllegalStateException("&cFailed to deserialize filtered-items!");
        }
    }
}