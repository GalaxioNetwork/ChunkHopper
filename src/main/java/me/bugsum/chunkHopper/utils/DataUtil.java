package me.bugsum.chunkHopper.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtil {

    public static int getPlayerPermissionChunkHopperLimit(Player player){
        if(player.hasPermission("chunkhoppers.limit.*") || player.hasPermission("*") || player.isOp()){
            return Integer.MAX_VALUE;
        }
        return getPermissionValues(player);
    }

    private static int getPermissionValues(Player player){
        List<Integer> values = new ArrayList<>();
        for(PermissionAttachmentInfo attachment : player.getEffectivePermissions().stream()
                .filter(permission -> permission.getPermission().startsWith("chunkhoppers.limit.")).collect(Collectors.toList())){
            String permission = attachment.getPermission();
            values.add(Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1)));
        }
        values = values.stream().sorted().collect(Collectors.toList());
        return values.isEmpty() ? 1 : values.get(values.size() - 1);
    }

    public static boolean hasShopGuiPlus(){
        return Bukkit.getPluginManager().getPlugin("ShopGUIPlus") != null;
    }
}