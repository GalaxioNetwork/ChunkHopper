package me.bugsum.chunkHopper.listener;

import com.earth2me.essentials.Essentials;
import me.bugsum.chunkHopper.ChunkHopper;
import me.bugsum.chunkHopper.base.BaseChunkHopper;
import me.bugsum.chunkHopper.base.ChunkHopperManager;
import me.bugsum.chunkHopper.utils.ConfigValues;
import me.bugsum.chunkHopper.utils.DataUtil;
import net.brcdev.shopgui.ShopGuiPlugin;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.shop.item.ShopItem;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

public class PreventionListener implements Listener {

    @EventHandler
    public void onMoveItem(InventoryMoveItemEvent e){
        if(e.isCancelled()) return;
            
        Inventory source = e.getSource();
        if(source.getType() != InventoryType.HOPPER){
            return;
        }
        Inventory destination = e.getDestination();
        Location location = e.getSource().getLocation();
        if(!ChunkHopperManager.getLocationKeys().contains(location)){
            return;
        }
        ItemStack itemStack = e.getItem().clone();
        BaseChunkHopper baseChunkHopper = ChunkHopperManager.getChunkHopper(location);
        if(baseChunkHopper.isAutoSell()){
            if(DataUtil.hasShopGuiPlus()){
                ShopGuiPlugin shop = ShopGuiPlusApi.getPlugin();
                ShopItem shopItem = shop.getShopManager().findShopItemByItemStack(itemStack, true);
                if(shopItem != null){
                    e.setCancelled(true);
                    return;
                }
            }
            if(ConfigValues.isEssentialsWorth()){
                Essentials essentials = ChunkHopper.getInstance().getEssentials();
                BigDecimal bigDecimal = essentials.getWorth().getPrice(essentials, itemStack);
                if(bigDecimal != null){
                    e.setCancelled(true);
                    return;
                }
            }
        }
        if(ConfigValues.getPerTransferAmount() < 2){
            return;
        }
        ItemStack sourceItem = Arrays.stream(source.getContents()).filter(Objects::nonNull)
                .filter(item -> item.isSimilar(itemStack)).findFirst().orElse(null);
        if(sourceItem == null){
            return;
        }
        ItemStack destItem = Arrays.stream(destination.getContents()).filter(Objects::nonNull)
                .filter(item -> item.isSimilar(itemStack))
                .filter(item -> item.getAmount() < item.getMaxStackSize()).findFirst().orElse(null);
        int amount = Math.min(sourceItem.getAmount(), ConfigValues.getPerTransferAmount() - 1);
        if(destItem != null){
            int destAmount = destItem.getAmount() + amount <= destItem.getMaxStackSize() ? amount : destItem.getMaxStackSize() - destItem.getAmount() - 1;
            destItem.setAmount(destItem.getAmount() + destAmount);
            sourceItem.setAmount(sourceItem.getAmount() - destAmount);
        }else{
            itemStack.setAmount(amount);
            destination.addItem(itemStack);
            sourceItem.setAmount(sourceItem.getAmount() - amount);
        }
    }

    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent e){
        if(e.isCancelled()){
            return;
        }
        e.blockList().removeIf(block -> ChunkHopperManager.getLocationKeys().contains(block.getLocation()));
    }

    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent e){
        if(e.isCancelled()){
            return;
        }
        e.blockList().removeIf(block -> ChunkHopperManager.getLocationKeys().contains(block.getLocation()));
    }
}