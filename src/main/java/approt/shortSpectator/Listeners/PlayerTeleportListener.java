package approt.shortSpectator.Listeners;

import approt.shortSpectator.ShortSpectator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    private final ShortSpectator plugin;
    public PlayerTeleportListener(ShortSpectator plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();

        if(plugin.enabled && event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && plugin.inPlayer && !player.hasPermission("shortspectator.bypass")) // Checks that the plugin is enabled, the cause is spectator teleport, we are using the in-player mode, and it is not someone we should bypass
        {
            // Cancel the event if they try to leave a player or enter an entity
            if(player.getSpectatorTarget() == null || !(player.getSpectatorTarget() instanceof Player)) {
                event.setCancelled(true);
                player.sendMessage(plugin.spectateMessage);
            }
        }
    }
}
