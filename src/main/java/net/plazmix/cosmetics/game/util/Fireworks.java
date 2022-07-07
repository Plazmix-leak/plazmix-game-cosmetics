package net.plazmix.cosmetics.game.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

/**
 * @author JustCodeSmth <vk@danya.simanov>
 */
public class Fireworks {
    
    private static final Random R = new Random();
    
    private static final FireworkEffect.Type[] TYPES = {
            FireworkEffect.Type.BALL, FireworkEffect.Type.BALL_LARGE,
            FireworkEffect.Type.BURST, FireworkEffect.Type.STAR
    };
    
    public static Firework launch(Location loc) {
        Firework firework = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder()
                .flicker(R.nextBoolean())
                .withColor(ColorUtil.random())
                .withFade(ColorUtil.random())
                .with(TYPES[R.nextInt(TYPES.length)])
                .trail(R.nextBoolean())
                .build()
        );
        meta.setPower(R.nextInt(1) + 1);
        firework.setFireworkMeta(meta);
        return firework;
    }
    
    private static class ColorUtil {
        
        private static final Color[] COLORS = {
                Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GREEN, Color.LIME,
                Color.MAROON,  Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE,
                Color.RED, Color.SILVER, Color.WHITE, Color.TEAL, Color.YELLOW
        };
        
        public static Color random() {
            return COLORS[(int) (Math.random() * COLORS.length)];
        }
        
    }
    
    
}
