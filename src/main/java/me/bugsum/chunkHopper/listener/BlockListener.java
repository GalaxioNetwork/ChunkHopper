package me.bugsum.chunkHopper.listener;

import me.bugsum.chunkHopper.ChunkHopper;
import me.bugsum.chunkHopper.base.BaseChunkHopper;
import me.bugsum.chunkHopper.base.ChunkHopperManager;
import me.bugsum.chunkHopper.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BlockListener implements Listener {

    private final ChunkHopper plugin = ChunkHopper.getInstance();

    @EventHandler
    public void blockPlacement(BlockPlaceEvent e){
        if(e.isCancelled()){
            return;
        }
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        ItemStack itemStack = e.getItemInHand();
        if(itemStack == null || !itemStack.getType().equals(Material.HOPPER)){
            return;
        }
        if(!PersistentData.hasPersistentData(itemStack, "chunkhopper")){
            return;
        }
        Location location = e.getBlock().getLocation();
        if(!ConfigValues.getWorlds().contains(location.getWorld().getName())){
            e.setCancelled(true);
            StringUtil.sendMessage(player, ConfigValues.getMessage("invalid-world-message"));
            return;
        }
        if(ChunkHopperUtil.hasChunkHopper(location.getChunk())){
            e.setCancelled(true);
            StringUtil.sendMessage(player, ConfigValues.getMessage("failed-placed-message"));
            EffectUtil.playSound(location, Sound.ENTITY_VILLAGER_NO);
            return;
        }
        int totalChunkHoppers = ChunkHopperUtil.getTotalChunkHoppers(player);
        if(totalChunkHoppers >= DataUtil.getPlayerPermissionChunkHopperLimit(player)){
            e.setCancelled(true);
            StringUtil.sendMessage(player, ConfigValues.getMessage("limit-message"));
            EffectUtil.playSound(location, Sound.ENTITY_VILLAGER_NO);
            return;
        }
        if(!player.isOp() && totalChunkHoppers >= ConfigValues.getHopperLimit()){
            e.setCancelled(true);
            StringUtil.sendMessage(player, "&cYou have reached the server chunk hopper limit of 20");
            EffectUtil.playSound(location, Sound.ENTITY_VILLAGER_NO);
            return;
        }
        ChunkHopperManager.addChunkHopper(new BaseChunkHopper(uuid, location, new ArrayList<>(), new ArrayList<>(), 0.0D, false, false));
        StringUtil.sendMessage(player, ConfigValues.getMessage("placed-message"));
        EffectUtil.playSound(location, Sound.ENTITY_VILLAGER_YES);
    }

    @EventHandler
    public void onDestroyed(BlockBreakEvent e){
        if(e.isCancelled()){
            return;
        }
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        Block block = e.getBlock();
        if(!block.getType().equals(Material.HOPPER)){
            return;
        }
        Location location = block.getLocation();
        if(ChunkHopperUtil.NotChunkHopper(location)){
            return;
        }
        if(ChunkHopperUtil.NotOwner(location, uuid) && !plugin.isBypassed(player)){
            e.setCancelled(true);
            if(player.hasPermission("chunkhoppers.bypass")){
                StringUtil.sendMessage(player, ConfigValues.getMessage("modify-message"));
                return;
            }
            StringUtil.sendMessage(player, ConfigValues.getMessage("not-owner-message"));
            return;
        }
        e.setDropItems(false);
        ChunkHopperManager.removeChunkHopper(location);
        location.getWorld().dropItemNaturally(location, ChunkHopperUtil.getChunkHopper(1));
        StringUtil.sendMessage(player, ConfigValues.getMessage("destroyed-message"));
        EffectUtil.playSound(location, Sound.ENTITY_VILLAGER_YES);
    }
}