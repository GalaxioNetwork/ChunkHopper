package me.bugsum.chunkHopper.utils;

import me.bugsum.chunkHopper.ChunkHopper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigValues {

    public static final ChunkHopper plugin = ChunkHopper.getInstance();
    private static Map<String, String> messages;
    private static List<String> worlds;
    private static boolean autoSell;
    private static boolean autoKill;
    private static boolean playerAsKiller;
    private static boolean pickupSound;
    private static boolean pickupParticles;
    private static int pickupDelay;
    private static String displayname;
    private static List<String> lores;
    private static List<ItemStack> allowedItems;
    private static boolean allowedCustomItems;
    private static int perTransferAmount;
    private static boolean essentialsWorth;
    private static int hopperLimit;

    public static void loadConfigValues(){
        messages = new HashMap<>();
        for(String stringKey : plugin.getConfig().getConfigurationSection("messages").getKeys(false)){
            messages.put(stringKey, plugin.getConfig().getString("messages." + stringKey));
        }
        worlds = plugin.getConfig().getStringList("worlds");
        autoSell = (boolean) getValue("auto-sell");
        autoKill = (boolean) getValue("auto-kill");
        pickupSound = (boolean) getValue("pickup-sound");
        pickupParticles = (boolean) getValue("pickup-particles");
        pickupDelay = (int) getValue("pickup-delay");
        displayname = (String) getValue("displayname");
        lores = plugin.getConfig().getStringList("lore");
        playerAsKiller = (boolean) getValue("player-as-killer");
        allowedCustomItems = (boolean)getValue("allow-custom-items");
        perTransferAmount = (int) getValue("per-transfer-amount");
        essentialsWorth = (boolean) getValue("essentials-worth");
        hopperLimit = (int) getValue("hopper-limit");
        allowedItems = plugin.getConfig().getStringList("allowed-items").stream().map(s -> new ItemStack(Material.valueOf(s))).collect(Collectors.toList());
    }

    public static boolean isEssentialsWorth() {
        return essentialsWorth;
    }

    public static int getHopperLimit() {
        return hopperLimit;
    }

    public static String getMessage(String paramString){
        return messages.get(paramString);
    }

    public static List<String> getWorlds() {
        return worlds;
    }

    public static boolean isAutoSell() {
        return autoSell;
    }

    public static boolean isAutoKill(){ return autoKill; }

    public static boolean isPlayerAsKiller() {
        return playerAsKiller;
    }

    public static boolean isPickupSound() {
        return pickupSound;
    }

    public static boolean isPickupParticles() {
        return pickupParticles;
    }

    public static int getPickupDelay() {
        return pickupDelay;
    }

    public static String getDisplayname() {
        return displayname;
    }

    public static List<String> getLores() {
        return lores;
    }

    public static Object getValue(String paramString){
        return plugin.getConfig().get(paramString);
    }

    public static List<ItemStack> getAllowedItems() {
        return allowedItems;
    }

    public static int getPerTransferAmount() {
        return perTransferAmount;
    }

    public static boolean isAllowedCustomItems() {
        return allowedCustomItems;
    }
}