package approt.shortSpectator;

import approt.shortSpectator.Listeners.GameModeChangeListener;
import approt.shortSpectator.Listeners.PlayerJoinListener;
import approt.shortSpectator.Listeners.PlayerTeleportListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class ShortSpectator extends JavaPlugin {

    // Config Values
    int range;
    boolean sendRangeMessage;
    String rangeMessage;
    public boolean enabled;
    public boolean inPlayer;
    public String spectateMessage;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "ShortSpectator has been enabled!");
        saveDefaultConfig();
        loadConfigValues();

        // Register the command
        Objects.requireNonNull(getCommand("shortspectator")).setExecutor(new ShortSpectatorCommand(this));

        // Register the tab completer
        Objects.requireNonNull(getCommand("shortspectator")).setTabCompleter(new ShortSpectatorTabCompleter());

        // Register the Listeners
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new GameModeChangeListener(this), this);

        // Creates a runnable that will check and teleport spectators out of range
        new BukkitRunnable()
        {
            @Override
            public void run() {
                if(enabled && !inPlayer) // Checks if the plugin has been enabled and if we are using the in-player mode
                {
                    for (Player player : Bukkit.getOnlinePlayers()) // Gets all currently online players and iterates through them
                    {
                        if (player.getGameMode() == GameMode.SPECTATOR && !player.hasPermission("shortspectator.bypass")) // Checks if the player is in spectator and does not need to be bypassed
                        {
                            boolean isInRange = false;
                            for (Entity entity : player.getNearbyEntities(range, range, range)) {
                                if (entity instanceof Player) // If there is a player in range we don't need to do anything
                                {
                                    isInRange = true;
                                    break;
                                }
                            }

                            if (!isInRange) {
                                Player closest = null;
                                double dist = Double.MAX_VALUE;
                                double tempDist;

                                for (Player target : Bukkit.getOnlinePlayers()) // Finds the closest player
                                {
                                    if(target == player)
                                        continue;
                                    tempDist = player.getLocation().distanceSquared(target.getLocation());
                                    if (tempDist < dist) {
                                        dist = tempDist;
                                        closest = target;
                                    }
                                }

                                if (closest != null) // Checks that there is a closest player
                                {
                                    // Teleports the player and sends them the message if enabled
                                    player.teleport(closest);
                                    if(sendRangeMessage)
                                        player.sendMessage(rangeMessage);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 10L);
    }

    public void loadConfigValues()
    {
        super.reloadConfig();

        range = getConfig().getInt("range", 10);
        sendRangeMessage = getConfig().getBoolean("send-range-message", true);
        rangeMessage = getConfig().getString("range-message", "§cYou must be near a player!");
        enabled = getConfig().getBoolean("enabled", true);
        inPlayer = getConfig().getBoolean("require-in-player", false);
        spectateMessage = getConfig().getString("spectator-message", "§cYou must spectate a valid player!");
    }

    public void setSpectatorTarget(Player player)
    {
        if(enabled && !player.hasPermission("shortspectator.bypass")) {
            Player closest = null;
            double dist = Double.MAX_VALUE;
            double tempDist;

            for (Player target : Bukkit.getOnlinePlayers()) // Finds the closest player
            {
                if (target == player)
                    continue;
                tempDist = player.getLocation().distanceSquared(target.getLocation());
                if (tempDist < dist) {
                    dist = tempDist;
                    closest = target;
                }
            }

            if (closest != null) {
                player.setSpectatorTarget(closest);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "ShortSpectator has been disabled!");
    }
}
