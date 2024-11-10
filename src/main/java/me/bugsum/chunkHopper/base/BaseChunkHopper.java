package me.bugsum.chunkHopper.base;

import me.bugsum.chunkHopper.utils.ChunkHopperUtil;
import me.bugsum.chunkHopper.utils.PersistentData;
import me.bugsum.chunkHopper.utils.EffectUtil;
import me.bugsum.chunkHopper.utils.ItemSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BaseChunkHopper {

    private final String uuid;
    private final List<ItemStack> filteredItems;
    private final List<ItemStack> filteredMobs;
    private final Location location;
    private double totalEarnings;
    private boolean autoSell;
    private boolean autoKill;
    private final OfflinePlayer offlinePlayer;

    public BaseChunkHopper(String uuid, Location location, List<ItemStack> filteredItems, List<ItemStack> filteredMobs, double totalEarnings, boolean autoSell, boolean autoKill) {
        this.uuid = uuid;
        this.offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
        this.filteredItems = filteredItems;
        this.filteredMobs = filteredMobs;
        this.location = location;
        this.totalEarnings = totalEarnings;
        this.autoSell = autoSell;
        this.autoKill = autoKill;
    }

    public void collectGroundItems(){
        Chunk chunk = location.getChunk();
        if(!chunk.isLoaded()){
            return;
        }
        Block block = location.getBlock();
        if(!block.getType().equals(Material.HOPPER)){
            ChunkHopperManager.removeChunkHopper(location);
            return;
        }
        ChunkHopperUtil.autoKillMobs(chunk, this);
        Hopper hopper = (Hopper) block.getState();
        Inventory inventory = hopper.getInventory();
        for(Item item : ChunkHopperUtil.getGroundItems(chunk, filteredItems)){
            int amount = item.getItemStack().getAmount();
            Map<Integer, ItemStack> drops = inventory.addItem(item.getItemStack());
            if(drops.isEmpty() || item.getItemStack().getAmount() < amount){
                if(drops.isEmpty()){
                    item.remove();
                }
                EffectUtil.spawnParticle(location.clone().add(new Vector(0.5, 1, 0.5)));
                EffectUtil.playEffect(item.getLocation());
                EffectUtil.playSound(location, Sound.ENTITY_ITEM_PICKUP);
            }
        }
        ChunkHopperUtil.sellItems(inventory, this);
    }

    public void updatePersistentValue(){
        Hopper hopper = (Hopper) location.getBlock().getState();
        String filteredItemsString = filteredItems.isEmpty() ? "NONE" : ItemSerializer.serializeItemStacks(filteredItems);
        String filteredMobsString = filteredMobs.isEmpty() ? "NONE" : ItemSerializer.serializeItemStacks(filteredMobs);
        hopper.getPersistentDataContainer().set(PersistentData.createNamespacedKey("filteredItems"), PersistentDataType.STRING, filteredItemsString);
        hopper.getPersistentDataContainer().set(PersistentData.createNamespacedKey("filteredMobs"), PersistentDataType.STRING, filteredMobsString);
        hopper.update();
    }

    public void saveConfiguration(){
        (new ChunkHopperFile(this)).save(true);
    }

    public boolean isAutoSell() {
        return autoSell;
    }

    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;
    }

    public boolean isAutoKill() {
        return autoKill;
    }

    public void setAutoKill(boolean autoKill) {
        this.autoKill = autoKill;
    }

    public void addFilteredItem(ItemStack itemStack){
        filteredItems.add(itemStack);
    }

    public void addFilteredMob(ItemStack itemStack){ filteredMobs.add(itemStack); }

    public void setTotalEarnings(double amount){ totalEarnings = amount; }

    public String getUuid() {
        return uuid;
    }

    public List<ItemStack> getFilteredItems() {
        return filteredItems;
    }

    public Location getLocation() {
        return location;
    }

    public double getTotalEarnings(){ return totalEarnings; }

    public List<ItemStack> getFilteredMobs() {
        return filteredMobs;
    }

    public OfflinePlayer getOfflinePlayer(){
        return offlinePlayer;
    }
}