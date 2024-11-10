package me.bugsum.chunkHopper.listener;

import me.bugsum.chunkHopper.ChunkHopper;
import me.bugsum.chunkHopper.base.BaseChunkHopper;
import me.bugsum.chunkHopper.base.ChunkHopperManager;
import me.bugsum.chunkHopper.menu.ChunkHopperMenu;
import me.bugsum.chunkHopper.menu.Menu;
import me.bugsum.chunkHopper.utils.ChunkHopperUtil;
import me.bugsum.chunkHopper.utils.ConfigValues;
import me.bugsum.chunkHopper.utils.EffectUtil;
import me.bugsum.chunkHopper.utils.StringUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListener implements Listener {

    private final ChunkHopper plugin = ChunkHopper.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(!e.getAction().name().endsWith("BLOCK")){
            return;
        }
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        Location location = block.getLocation();
        if(!block.getType().equals(Material.HOPPER)){
            return;
        }
        Action action = e.getAction();
        if(ChunkHopperUtil.NotChunkHopper(location)){
            return;
        }
        if(!player.isSneaking()){
            return;
        }
        if(ChunkHopperUtil.NotOwner(location, player.getUniqueId().toString()) && !plugin.isBypassed(player)){
            if(player.hasPermission("chunkhoppers.bypass")){
                StringUtil.sendMessage(player, ConfigValues.getMessage("modify-message"));
                return;
            }
            StringUtil.sendMessage(player, ConfigValues.getMessage("not-owner-message"));
            return;
        }
        BaseChunkHopper baseChunkHopper = ChunkHopperManager.getChunkHopper(location);
        if(action == Action.RIGHT_CLICK_BLOCK){
            e.setCancelled(true);
            (new ChunkHopperMenu(player, baseChunkHopper)).openInventory();
            EffectUtil.playSound(location, Sound.BLOCK_ENDER_CHEST_OPEN);
        }else{
            if(e.getItem() == null){
                return;
            }
            ItemStack itemStack = e.getItem().clone();
            itemStack.setAmount(1);
            e.setCancelled(true);

            if(baseChunkHopper.getFilteredItems().contains(itemStack)){
                StringUtil.sendMessage(player, ConfigValues.getMessage("already-filtered-message"));
                return;
            }
            if(itemStack.hasItemMeta() && ConfigValues.isAllowedCustomItems()){
                baseChunkHopper.addFilteredItem(itemStack);
            }else{
                if(!ConfigValues.getAllowedItems().contains(itemStack)){
                    StringUtil.sendMessage(player, ConfigValues.getMessage("item-not-allowed-message"));
                    return;
                }
                baseChunkHopper.addFilteredItem(itemStack);
            }
            baseChunkHopper.updatePersistentValue();
            StringUtil.sendMessage(player, ConfigValues.getMessage("filtered-message"));
        }
    }

    @EventHandler
    public void playerClickInventory(InventoryClickEvent e){
        if(!(e.getView().getTopInventory().getHolder() instanceof Menu)){
            return;
        }
        if(e.getCurrentItem() == null){
            return;
        }
        if(e.getClickedInventory() == null){
            return;
        }
        if(e.getClickedInventory() instanceof PlayerInventory){
            e.setCancelled(true);
        }
        InventoryHolder holder = e.getClickedInventory().getHolder();
        if(holder instanceof Menu){
            ((Menu) holder).handleClickListener(e);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        if(plugin.getConfig().contains(uuid)){
            double totalEarnings = plugin.getConfig().getDouble(uuid);
            plugin.getEconomy().depositPlayer(player, totalEarnings);
            StringUtil.sendMessage(player, ConfigValues.getMessage("force-withdraw-message")
                    .replace("%amount%", String.valueOf(totalEarnings)));
            plugin.getConfig().set(uuid, null);
            plugin.saveConfig();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        ChunkHopper plugin = ChunkHopper.getInstance();
        if(plugin.isBypassed(player)){
            plugin.removeBypassedUser(player);
        }
    }
}