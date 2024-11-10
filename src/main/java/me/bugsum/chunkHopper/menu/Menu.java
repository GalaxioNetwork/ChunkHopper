package me.bugsum.chunkHopper.menu;

import me.bugsum.chunkHopper.ChunkHopper;
import me.bugsum.chunkHopper.base.BaseChunkHopper;
import me.bugsum.chunkHopper.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {

    protected ChunkHopper plugin = ChunkHopper.getInstance();
    protected Inventory inventory;
    protected Player player;
    protected BaseChunkHopper baseChunkHopper;
    protected int page;

    public Menu(Player player, BaseChunkHopper baseChunkHopper) {
        this.player = player;
        this.baseChunkHopper = baseChunkHopper;
    }

    public abstract String title();
    public abstract int size();
    public abstract void setContents();
    public abstract void handleClickListener(InventoryClickEvent e);
    public void openInventory(){
        inventory = Bukkit.createInventory(this, size(), StringUtil.translate(title()));
        setContents();
        player.openInventory(inventory);
    }
}