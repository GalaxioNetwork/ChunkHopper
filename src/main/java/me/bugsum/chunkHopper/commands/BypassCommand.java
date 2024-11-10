package me.bugsum.chunkHopper.commands;

import me.bugsum.chunkHopper.utils.ConfigValues;
import me.bugsum.chunkHopper.utils.StringUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(command = "bypass", permission = "chunkhoppers.bypass", inGame = true)
public class BypassCommand extends Subcommand {
    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        plugin.toggleBypassedUser(player);
        StringUtil.sendMessage(player, plugin.isBypassed(player)
                ? ConfigValues.getMessage("can-modify-message") : ConfigValues.getMessage("not-modify-message"));
    }
}