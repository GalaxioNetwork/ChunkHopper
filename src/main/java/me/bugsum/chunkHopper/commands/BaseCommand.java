package me.bugsum.chunkHopper.commands;

import me.bugsum.chunkHopper.utils.ConfigValues;
import me.bugsum.chunkHopper.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BaseCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            StringUtil.sendMessage(sender, "&b---------------- &lChunkHopper &b-------------------");
            for(String syntax : CommandManager.getSubcommandSyntax()){
                StringUtil.sendMessage(sender, "&b" + syntax);
            }
            return true;
        }
        Subcommand subcommand = CommandManager.getSubcommandFromString(args[0]);
        if(subcommand == null){
            StringUtil.sendMessage(sender, ConfigValues.getMessage("invalid-command-message"));
            return true;
        }
        if(!sender.hasPermission(subcommand.values().permission())){
            StringUtil.sendMessage(sender, ConfigValues.getMessage("no-permission-message"));
            return true;
        }
        if(args.length != subcommand.values().length()){
            StringUtil.sendMessage(sender, "&e" + subcommand.values().syntax());
            return true;
        }
        if(subcommand.values().inGame() && ! (sender instanceof Player)){
            StringUtil.consoleMessage("&cThis command is for players only!");
            return true;
        }
        subcommand.handleCommand(sender, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if(sender.hasPermission("chunkhoppers.admin")){
            if(args.length == 1){
                CommandManager.getSubcommandStringList().stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).forEach(result::add);
            }
            if(args.length == 2 && args[0].equalsIgnoreCase("give")){
                Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).forEach(result::add);
            }
            if(args.length == 3 && args[0].equalsIgnoreCase("give")){
                Stream.of("1", "16", "32", "48", "60").filter(s -> s.startsWith(args[2].toLowerCase())).forEach(result::add);
            }
        }
        return result;
    }
}