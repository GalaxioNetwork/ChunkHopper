package me.bugsum.chunkHopper.utils;

import me.bugsum.chunkHopper.ChunkHopper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomItem {

    private ItemStack itemStack;

    public CustomItem item(ItemStack itemStack){
        this.itemStack = itemStack;
        return this;
    }

    public CustomItem material(Material material){
        itemStack = new ItemStack(material);
        return this;
    }

    public CustomItem amount(int amount){
        itemStack.setAmount(amount);
        return this;
    }

    public CustomItem displayname(String paramString){
        ItemMeta meta = meta();
        meta.setDisplayName(StringUtil.translate(paramString));
        itemStack.setItemMeta(meta);
        return this;
    }

    public CustomItem lore(String...lores){
        ItemMeta meta = meta();
        List<String> finalLore = new ArrayList<>();
        if(meta.hasLore()){
            finalLore.addAll(meta.getLore());
        }
        Arrays.stream(lores).map(StringUtil::translate).forEach(finalLore::add);
        meta.setLore(finalLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public CustomItem lore(List<String> lores){
        ItemMeta meta = meta();
        meta.setLore(lores.stream().map(StringUtil::translate).collect(Collectors.toList()));
        itemStack.setItemMeta(meta);
        return this;
    }

    public CustomItem skull(String uuid){
        SkullMeta meta = (SkullMeta) meta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
        itemStack.setItemMeta(meta);
        return this;
    }

    public CustomItem removeLoreLastLineBy(int count){
        ItemMeta meta = meta();
        List<String> lore = meta.getLore();
        for(int i=0; i<count; i++){
            lore.remove(lore.size()-1);
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public CustomItem glow(){
        ItemMeta meta = meta();
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return this;
    }

    public CustomItem persistentString(String paramString, String paramValue){
        ItemMeta meta = meta();
        meta.getPersistentDataContainer().set(new NamespacedKey(ChunkHopper.getInstance(), paramString),
                PersistentDataType.STRING, paramValue == null ? paramString.toUpperCase() : paramValue);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStack getItemStack(){
        return itemStack;
    }

    private ItemMeta meta(){
        return itemStack.getItemMeta();
    }
}