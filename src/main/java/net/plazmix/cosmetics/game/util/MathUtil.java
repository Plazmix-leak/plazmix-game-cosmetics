package net.plazmix.cosmetics.game.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * @author JustCodeItBabe
 */
public class MathUtil {
    
    private static final Random R = new Random();
    
    public static int random() {
        return R.nextInt();
    }
    
    public static int random(int value) {
        return R.nextInt(value);
    }
    
    public static double offset2d(Entity a, Entity b) {
        return offset2d(a.getLocation().toVector(), b.getLocation().toVector());
    }
    
    public static double offset2d(Location a, Location b) {
        return offset2d(a.toVector(), b.toVector());
    }
    
    public static double offset2d(Vector a, Vector b) {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }
    
    public static double offset(Entity a, Entity b) {
        return offset(a.getLocation().toVector(), b.getLocation().toVector());
    }
    
    public static double offset(Location a, Location b) {
        return offset(a.toVector(), b.toVector());
    }
    
    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }
    
    public static double offsetSquared(Entity a, Entity b) {
        return offsetSquared(a.getLocation(), b.getLocation());
    }
    
    public static double offsetSquared(Location a, Location b) {
        return offsetSquared(a.toVector(), b.toVector());
    }
    
    public static double offsetSquared(Vector a, Vector b) {
        return a.distanceSquared(b);
    }
    
    public static double rr(double d, boolean bidirectional) {
        if (bidirectional)
            return Math.random() * (2 * d) - d;
        return Math.random() * d;
    }
    
}
