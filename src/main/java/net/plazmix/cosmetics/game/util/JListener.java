package net.plazmix.cosmetics.game.util;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JustCodeItBabe
 */
public class JListener implements Listener {
    
    @Getter
    private boolean registered = false;
    
    public JListener(JavaPlugin plugin) {
        register(plugin);
    }
    
    public void register(JavaPlugin plugin) {
        if (this.registered)
            return;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.registered = true;
    }
    
    public void unregister() {
        if (!this.registered)
            return;
        HandlerList.unregisterAll(this);
        this.registered = false;
    }
    
}
