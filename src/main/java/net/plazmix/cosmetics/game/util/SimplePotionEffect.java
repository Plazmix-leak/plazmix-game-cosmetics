package net.plazmix.cosmetics.game.util;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author JustCodeSmth <vk@danya.simanov>
 */
public class SimplePotionEffect extends PotionEffect {
    
    public SimplePotionEffect(PotionEffectType type, int level) {
        this(type, level, -1D);
    }
    
    public SimplePotionEffect(PotionEffectType type, int level, double duration) {
        super(type, (duration == -1D) ? 120000 : (int) (duration * 20D), level - 1);
    }
    
}
