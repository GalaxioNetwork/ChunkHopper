package me.bugsum.chunkHopper.commands;

import me.bugsum.chunkHopper.utils.ConfigValues;
import me.bugsum.chunkHopper.utils.StringUtil;
import me.bugsum.chunkHopper.commands.CommandInfo;
import org.bukkit.command.CommandSender;

@CommandInfo(command = "reload", permission = "chunkhoppers.reload", syntax = "/chunkhopper reload")
public class ReloadCommand extends Subcommand{
    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        ConfigValues.loadConfigValues();
        plugin.cancelTask();
        plugin.collectItems();
        StringUtil.sendMessage(sender, ConfigValues.getMessage("reload-message"));
    }
}