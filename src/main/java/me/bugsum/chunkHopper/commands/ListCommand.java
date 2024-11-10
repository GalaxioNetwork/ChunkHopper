package me.bugsum.chunkHopper.commands;

import me.bugsum.chunkHopper.menu.ListMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(command = "list", permission = "chunkhoppers.list", syntax = "/chunkhopper list", inGame = true)
public class ListCommand extends Subcommand{
    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        (new ListMenu(player, null)).openInventory();
    }
}