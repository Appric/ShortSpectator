package approt.shortSpectator;

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
    String message;
    boolean enabled;

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

        // Creates a runnable that will check and teleport spectators out of range
        new BukkitRunnable()
        {
            @Override
            public void run() {
                if(enabled) // Checks if the plugin has been enabled
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
                                        player.sendMessage(message);
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
        sendRangeMessage = getConfig().getBoolean("sendRangeMessage", true);
        message = getConfig().getString("rangeMessage", "§cYou must be near a player!");
        enabled = getConfig().getBoolean("enabled", true);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "ShortSpectator has been disabled!");
    }
}
