package me.bugsum.chunkHopper.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager {

    private static final List<Subcommand> subcommands = new ArrayList<>();

    public CommandManager(){
        add(new ReloadCommand()).add(new GiveCommand()).add(new ListCommand()).add(new BypassCommand());
    }

    private CommandManager add(Subcommand subcommand){
        subcommands.add(subcommand);
        return this;
    }

    public static Subcommand getSubcommandFromString(String paramString){
        return subcommands.stream().filter(subcommand -> subcommand.values().command().equalsIgnoreCase(paramString)).findFirst().orElse(null);
    }

    public static List<String> getSubcommandStringList(){
        return subcommands.stream().map(subcommand -> subcommand.values().command()).collect(Collectors.toList());
    }

    public static List<String> getSubcommandSyntax(){
        return subcommands.stream().map(subcommand -> subcommand.values().syntax()).collect(Collectors.toList());
    }
}