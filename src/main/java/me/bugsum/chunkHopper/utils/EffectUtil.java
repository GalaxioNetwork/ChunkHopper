package me.bugsum.chunkHopper.utils;

import me.bugsum.chunkHopper.utils.ConfigValues;
import org.bukkit.*;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class EffectUtil {

    public static void playSound(Location location, Sound sound){
        if(!ConfigValues.isPickupSound()){
            return;
        }
        location.getWorld().playSound(location, sound, 1, 1);
    }

    public static void spawnParticle(Location location){
        if(!ConfigValues.isPickupParticles()){
            return;
        }
        int red = ThreadLocalRandom.current().nextInt(255);
        int green = ThreadLocalRandom.current().nextInt(255);
        int blue = ThreadLocalRandom.current().nextInt(255);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromBGR(red, green, blue), 2.0F);
        location.getWorld().spawnParticle(Particle.DUST, location, 10, dustOptions);
    }

    public static void playEffect(Location location){
        location.getWorld().playEffect(location.clone().add(new Vector(0, 1, 0)), Effect.SMOKE, 150);
    }
}