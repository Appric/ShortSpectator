package approt.shortSpectator.Listeners;

import approt.shortSpectator.ShortSpectator;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GameModeChangeListener implements Listener {
    private final ShortSpectator plugin;
    public GameModeChangeListener(ShortSpectator plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event)
    {
        if(plugin.enabled)
        {
            Player player = event.getPlayer();
            if(player.getGameMode() == GameMode.SPECTATOR)
            {
                // When a player switches or gets moved to Spectator mode, puts them in a player
                plugin.setSpectatorTarget(player);
            }
        }
    }
}
