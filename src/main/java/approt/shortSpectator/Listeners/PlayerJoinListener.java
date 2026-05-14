package approt.shortSpectator.Listeners;

import approt.shortSpectator.ShortSpectator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final ShortSpectator plugin;
    public PlayerJoinListener(ShortSpectator plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        // Checks if the plugin is enabled/toggled
        if(plugin.enabled) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                // Checks all online players in case they are not spectating anyone
                if (player.getGameMode() == GameMode.SPECTATOR && player.getSpectatorTarget() == null)
                    plugin.setSpectatorTarget(player);
            }
        }
    }
}
