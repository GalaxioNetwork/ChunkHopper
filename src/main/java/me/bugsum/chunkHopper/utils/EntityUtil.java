package me.bugsum.chunkHopper.utils;

import me.bugsum.chunkHopper.utils.ConfigValues;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUtil {

    public static List<ItemStack> getAllSpawnEggs(List<ItemStack> filteredMobs){
        return Arrays.stream(Material.values()).filter(material -> material.name().endsWith("_SPAWN_EGG"))
                .map(ItemStack::new).filter(itemStack -> !filteredMobs.contains(itemStack)).collect(Collectors.toList());
    }

    public static List<ItemStack> getAllowedItems(List<ItemStack> filteredItems){
        return ConfigValues.getAllowedItems().stream().filter(itemStack -> !filteredItems.contains(itemStack)).collect(Collectors.toList());
    }
}