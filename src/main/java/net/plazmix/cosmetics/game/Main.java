package net.plazmix.cosmetics.game;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JustCodeItBabe
 */
public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Game cosmetics loaded successfully!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Game cosmetics shipped successfully!");
    }
    
}
