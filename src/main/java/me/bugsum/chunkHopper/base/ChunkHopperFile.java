package me.bugsum.chunkHopper.base;

import me.bugsum.chunkHopper.utils.ItemSerializer;
import me.bugsum.chunkHopper.utils.PersistentData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChunkHopperFile extends CustomConfiguration {

    private BaseChunkHopper baseChunkHopper;

    public ChunkHopperFile(BaseChunkHopper baseChunkHopper) {
        super(baseChunkHopper.getUuid(), "chunkhoppers");
        this.baseChunkHopper = baseChunkHopper;
    }

    public ChunkHopperFile(File file) {
        super(file);
    }

    public void save(boolean configSave){
        String locationString = ItemSerializer.serializeLocation(baseChunkHopper.getLocation());
        if(!configSave){
            baseChunkHopper.updatePersistentValue();
        }
        configuration.set(locationString + ".totalEarnings", baseChunkHopper.getTotalEarnings());
        configuration.set(locationString + ".autoSell", baseChunkHopper.isAutoSell());
        configuration.set(locationString + ".autoKill", baseChunkHopper.isAutoKill());
        saveConfiguration();

    }

    public void remove(){
        String locationString = ItemSerializer.serializeLocation(baseChunkHopper.getLocation());
        configuration.set(locationString, null);
        saveConfiguration();
        if(configuration.getKeys(false).isEmpty()){
            file.delete();
        }
    }

    public void loadAll(){
        for(String locationString : configuration.getKeys(false)){
            Location location = ItemSerializer.deserializeLocation(locationString.split(";"));
            Block block = location.getBlock();
            if(!block.getType().equals(Material.HOPPER)){
                continue;
            }
            Hopper hopper = (Hopper) block.getState();
            PersistentData.checkPersistentData(hopper, "filteredItems");
            PersistentData.checkPersistentData(hopper, "filteredMobs");
            String stringValue = hopper.getPersistentDataContainer().get(new NamespacedKey(plugin, "filteredItems"), PersistentDataType.STRING);
            String stringValueMobs = hopper.getPersistentDataContainer().get(new NamespacedKey(plugin, "filteredMobs"), PersistentDataType.STRING);
            List<ItemStack> filteredItems = stringValue.equalsIgnoreCase("NONE") ? new ArrayList<>() : ItemSerializer.deserializeItemStacks(stringValue);
            List<ItemStack> filteredMobs = stringValueMobs.equalsIgnoreCase("NONE") ? new ArrayList<>() : ItemSerializer.deserializeItemStacks(stringValueMobs);
            double totalEarnings = configuration.getDouble(locationString + ".totalEarnings");
            boolean autoSell = configuration.getBoolean(locationString + ".autoSell");
            boolean autoKill = configuration.getBoolean(locationString + ".autoKill");
            ChunkHopperManager.addChunkHopper(new BaseChunkHopper(filename, location, filteredItems, filteredMobs, totalEarnings, autoSell, autoKill));
        }
    }
}