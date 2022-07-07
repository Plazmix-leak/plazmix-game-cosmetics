package net.plazmix.cosmetics.game.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Created by sacha on 07/08/15.
 */
public class UtilParticles {
    
    private final static int DEF_RADIUS = 128;
    
    public static void drawParticleLine(Location from, Location to, Particles effect, int particles, Color color) {
        drawParticleLine(from, to, effect, particles, color.getRed(), color.getGreen(), color.getBlue());
    }
    
    public static void drawParticleLine(Location from, Location to, Particles effect, int particles, int r, int g, int b) {
        Location location = from.clone();
        Location target = to.clone();
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        link.normalize();
        
        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        int step = 0;
        for (int i = 0; i < particles; i++) {
            if (step >= (double) particles)
                step = 0;
            step++;
            loc.add(v);
            if (effect == Particles.REDSTONE)
                effect.display(new Particles.OrdinaryColor(r, g, b), loc, 128);
            else
                effect.display(0, 0, 0, 0, 1, loc, 128);
        }
    }
    
    public static void display(Particles effect, Location location, int amount, float speed) {
        effect.display(0, 0, 0, speed, amount, location, 128);
    }
    
    public static void display(Particles effect, Location location, int amount) {
        effect.display(0, 0, 0, 0, amount, location, 128);
    }
    
    public static void display(Particles effect, Location location) {
        display(effect, location, 1);
    }
    
    public static void display(Particles effect, double x, double y, double z, Location location, int amount) {
        effect.display((float) x, (float) y, (float) z, 0f, amount, location, 128);
    }
    
    public static void display(Particles effect, int red, int green, int blue, Location location, int amount) {
        for (int i = 0; i < amount; i++)
            effect.display(new Particles.OrdinaryColor(red, green, blue), location, DEF_RADIUS);
    }
    
    public static void display(int red, int green, int blue, Location location) {
        display(Particles.REDSTONE, red, green, blue, location, 1);
    }
    
    public static void display(Particles effect, int red, int green, int blue, Location location) {
        display(effect, red, green, blue, location, 1);
    }
    
}