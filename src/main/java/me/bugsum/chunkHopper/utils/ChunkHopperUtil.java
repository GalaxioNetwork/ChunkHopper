package me.bugsum.chunkHopper.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Worth;
import me.bugsum.chunkHopper.ChunkHopper;
import me.bugsum.chunkHopper.base.BaseChunkHopper;
import me.bugsum.chunkHopper.base.ChunkHopperManager;
import net.brcdev.shopgui.ShopGuiPlugin;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.shop.item.ShopItem;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChunkHopperUtil {

    public static List<Item> getGroundItems(Chunk chunk, List<ItemStack> filteredItems){
        List<Item> items =  Arrays.stream(chunk.getEntities()).filter(Entity::isOnGround).filter(entity -> !entity.isDead()).filter(entity -> entity instanceof Item)
                .map(entity -> (Item) entity).collect(Collectors.toList());
        if(!filteredItems.isEmpty()){
            items.removeIf(item -> !isSimilar(item, filteredItems));
        }
        return items;
    }

    private static boolean isSimilar(Item item, List<ItemStack> filteredItems){
        return filteredItems.stream().anyMatch(itemStack -> itemStack.isSimilar(item.getItemStack()));
    }

    public static void autoKillMobs(Chunk chunk, BaseChunkHopper baseChunkHopper){
        if(!ConfigValues.isAutoKill()){
            return;
        }
        if(!baseChunkHopper.isAutoKill()){
            return;
        }
        Arrays.stream(chunk.getEntities()).filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> isKillable(entity, baseChunkHopper.getFilteredMobs())).map(entity -> (LivingEntity) entity)
                .forEach(livingEntity ->{
                    Location location = baseChunkHopper.getLocation().clone().add(.5, 1, .5);
                    livingEntity.teleport(location);
                    location.getWorld().spawnParticle(Particle.PORTAL, location, 100);
                    Entity killer = baseChunkHopper.getOfflinePlayer().isOnline() && ConfigValues.isPlayerAsKiller() ? baseChunkHopper.getOfflinePlayer().getPlayer() : null;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ChunkHopper.getInstance(), ()-> livingEntity.damage(100, killer), 10);
                });
    }

    private static boolean isKillable(Entity entity, List<ItemStack> filteredMobs){
        for(ItemStack itemStack : filteredMobs){
            if(entity.getName().toLowerCase().startsWith("magma") && itemStack.getType().name().toLowerCase().startsWith("magma")){
                return true;
            }
            if(itemStack.getType().name().startsWith(entity.getName().toUpperCase())){
                return true;
            }
        }
        return false;
    }

    public static void sellItems(Inventory inventory, BaseChunkHopper baseChunkHopper){
        if(!ConfigValues.isAutoSell()){
            return;
        }
        if(!baseChunkHopper.isAutoSell()){
            return;
        }
        if(ChunkHopper.getInstance().getEconomy() == null){
            return;
        }
        if(!DataUtil.hasShopGuiPlus()){
            if(ConfigValues.isEssentialsWorth()){
                Essentials essentials = ChunkHopper.getInstance().getEssentials();
                setEssentialsItems(inventory, baseChunkHopper, essentials);
            }
            return;
        }

        ShopGuiPlugin shop = ShopGuiPlusApi.getPlugin();
        double totalSold = 0;

        for(ItemStack itemStack : getContents(inventory)){
            ShopItem shopItem = shop.getShopManager().findShopItemByItemStack(itemStack, true);
            if(shopItem != null){
                totalSold += (baseChunkHopper.getOfflinePlayer().isOnline() ?
                        shopItem.getSellPrice(baseChunkHopper.getOfflinePlayer().getPlayer()) : shopItem.getSellPrice()) * itemStack.getAmount();
                inventory.remove(itemStack);
            }
        }

        baseChunkHopper.setTotalEarnings(baseChunkHopper.getTotalEarnings() + totalSold);
    }

    public static void setEssentialsItems(Inventory inventory, BaseChunkHopper baseChunkHopper, Essentials essentials){
        Worth worth = essentials.getWorth();
        double totalSold = 0;
        for(ItemStack itemStack : getContents(inventory)){
            BigDecimal bigDecimal = worth.getPrice(essentials, itemStack);
            if(bigDecimal != null){
                totalSold += bigDecimal.doubleValue() * itemStack.getAmount();
                inventory.remove(itemStack);
            }
        }

        baseChunkHopper.setTotalEarnings(baseChunkHopper.getTotalEarnings() + totalSold);
    }

    private static List<ItemStack> getContents(Inventory inventory){
        return Arrays.stream(inventory.getContents()).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static boolean hasChunkHopper(Chunk chunk){
        if(ChunkHopperManager.getAllChunkHoppers().isEmpty()){
            return false;
        }
        for(Location location : ChunkHopperManager.getLocationKeys()){
            if(location.getChunk().equals(chunk)){
                return true;
            }
        }
        return false;
    }

    public static boolean NotOwner(Location location, String uuid){
        return !ChunkHopperManager.getChunkHopper(location).getUuid().equals(uuid);
    }

    public static boolean NotChunkHopper(Location location){
        return !ChunkHopperManager.getLocationKeys().contains(location);
    }

    public static ItemStack getChunkHopper(int amount){
        return new CustomItem().material(Material.HOPPER).amount(amount).displayname(ConfigValues.getDisplayname())
                .lore(ConfigValues.getLores()).glow().persistentString("chunkhopper", null).getItemStack();
    }

    public static int getTotalChunkHoppers(Player player){
        String uuid = player.getUniqueId().toString();
        if(!ChunkHopperManager.getUuidKeys().contains(uuid)){
            return 0;
        }
        return ChunkHopperManager.getPlayerChunkHoppers(uuid).size();
    }
}