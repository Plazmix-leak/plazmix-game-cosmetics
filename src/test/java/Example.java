import net.plazmix.cosmetics.game.DanceManager;
import net.plazmix.cosmetics.game.DeathSoundManager;
import net.plazmix.cosmetics.game.KillEffectManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JustCodeItBabe
 */
public class Example {
    
    String getDeathSoundName(DeathSoundManager sound) {
        return sound.getName();
    }
    
    void onKill(JavaPlugin plugin, Player killer, Player victim) {
        KillEffectManager.IN_LOVE.run(plugin, killer, victim);
    }
    
    void onWin(JavaPlugin plugin, Player winner) {
        DanceManager.ANVIL_RAIN.run(plugin, winner);
    }
    
    void onDeath(Player player) {
        DeathSoundManager.BAT.run(player);
    }
    
}
