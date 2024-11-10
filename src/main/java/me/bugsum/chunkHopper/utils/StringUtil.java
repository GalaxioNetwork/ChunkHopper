package me.bugsum.chunkHopper.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;

public class StringUtil {

    public static String translate(String paramString){
        return ChatColor.translateAlternateColorCodes('&', paramString);
    }

    public static void sendMessage(CommandSender paramSender, String paraString){
        paramSender.sendMessage(translate(paraString));
    }

    public static void consoleMessage(String paramString){
        Bukkit.getConsoleSender().sendMessage(translate(paramString));
    }

    public static String getDecimalFormat(double amount){
        return new DecimalFormat("#,###,###,###,###,###").format(amount);
    }

    public static String capitalize(ItemStack itemStack){
        String[] name = itemStack.getType().name().toLowerCase().replace("_spawn_egg", "").split("_");
        StringBuilder builder = new StringBuilder();
        for(int i=0; i< name.length; i++){
            builder.append(i == 0 ? name[i].substring(0, 1).toUpperCase() + name[i].substring(1) : " " + name[i].substring(0, 1).toUpperCase() + name[i].substring(1));
        }
        return builder.toString();
    }

    public static boolean NotNumeric(String paramString){
        try{
            int value = Integer.parseInt(paramString);
            return false;
        }catch (NumberFormatException e){
            return true;
        }
    }

    public static boolean isNewVersion(){
        for(String version : Arrays.asList("1.16", "1.17", "1.18", "1.19")){
            if(Bukkit.getVersion().contains(version)){
                return true;
            }
        }
        return false;
    }
}