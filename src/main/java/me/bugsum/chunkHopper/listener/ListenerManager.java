package me.bugsum.chunkHopper.listener;

import me.bugsum.chunkHopper.ChunkHopper;
import me.bugsum.chunkHopper.listener.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerManager {


    public ListenerManager(){
        registerListeners(new BlockListener(), new PlayerListener(), new PreventionListener());
    }

    private void registerListeners(Listener...listeners){
        for(Listener listener : listeners){
            Bukkit.getPluginManager().registerEvents(listener, ChunkHopper.getInstance());
        }
    }
}