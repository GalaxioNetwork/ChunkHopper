package me.bugsum.chunkHopper.commands;

import me.bugsum.chunkHopper.ChunkHopper;
import org.bukkit.command.CommandSender;

public abstract class Subcommand {
    protected final ChunkHopper plugin = ChunkHopper.getInstance();
    public void handleCommand(CommandSender sender, String[] args){
        executeCommand(sender, args);
    }

    public abstract void executeCommand(CommandSender sender, String[] args);

    public CommandInfo values(){
        return getClass().getAnnotation(CommandInfo.class);
    }
}