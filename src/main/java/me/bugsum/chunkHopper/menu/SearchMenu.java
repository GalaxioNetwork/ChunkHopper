package me.bugsum.chunkHopper.menu;

import me.bugsum.chunkHopper.base.BaseChunkHopper;
import me.bugsum.chunkHopper.utils.ConfigValues;
import me.bugsum.chunkHopper.utils.CustomItem;
import me.bugsum.chunkHopper.utils.EntityUtil;
import me.bugsum.chunkHopper.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SearchMenu extends Menu {
    public SearchMenu(Player player, BaseChunkHopper baseChunkHopper) {
        super(player, baseChunkHopper);
    }

    private SearchType searchType;

    @Override
    public String title() {
        return searchType == SearchType.MOBS ? "&8Mobs Selection" : "&8Items Selection";
    }

    @Override
    public int size() {
        return 54;
    }

    @Override
    public void setContents() {
        List<ItemStack> filtered = searchType == SearchType.MOBS ? EntityUtil.getAllSpawnEggs(baseChunkHopper.getFilteredMobs()) :
                EntityUtil.getAllowedItems(baseChunkHopper.getFilteredItems());
        int size = filtered.size();
        if(size > 45){
            if(page > 0){
                inventory.setItem(45, new CustomItem().material(Material.ARROW).displayname("&b▶ &fPrevious Page").getItemStack());
            }
            if(page < size / 45){
                inventory.setItem(53, new CustomItem().material(Material.ARROW).displayname("&b▶ &fNext Page").getItemStack());
            }
        }
        for(int i = page * 45; i < 45 + (page  * 45); i++){
            if(i >= size) break;
            ItemStack itemStack = filtered.get(i).clone();
            inventory.addItem(new CustomItem().item(itemStack).displayname("&6" + StringUtil.capitalize(itemStack))
                    .lore("&7click to select!").getItemStack());
        }

        inventory.setItem(49, new CustomItem().material(Material.OAK_SIGN).displayname("&eBack").lore("&7click to go back!").getItemStack());
    }

    @Override
    public void handleClickListener(InventoryClickEvent e) {
        e.setCancelled(true);
        ItemStack itemStack = e.getCurrentItem();
        int slot = e.getSlot();
        if(itemStack == null || itemStack.getType().equals(Material.AIR)){
            return;
        }
        if(slot < 45){
            itemStack = new ItemStack(itemStack.getType());
            if(searchType == SearchType.MOBS){
                if(baseChunkHopper.getFilteredMobs().contains(itemStack)){
                    return;
                }
                baseChunkHopper.addFilteredMob(itemStack);
                baseChunkHopper.updatePersistentValue();
            }else{
                if(baseChunkHopper.getFilteredItems().contains(itemStack)){
                    return;
                }
                baseChunkHopper.addFilteredItem(itemStack);
                baseChunkHopper.updatePersistentValue();
            }
            StringUtil.sendMessage(player, ConfigValues.getMessage("filtered-message"));
            openInventory();
            return;
        }
        if(slot == 45){
            page--;
        }
        if(slot == 49){
            ChunkHopperMenu menu = new ChunkHopperMenu(player, baseChunkHopper);
            menu.setFiltered(searchType != SearchType.MOBS);
            menu.openInventory();
            return;
        }
        if(slot == 53){
            page++;
        }
        openInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setSearchType(SearchType searchType){
        this.searchType = searchType;
    }
}