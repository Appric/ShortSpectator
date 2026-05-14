package approt.shortSpectator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

public class ShortSpectatorCommand implements CommandExecutor {
    private final ShortSpectator plugin;

    public ShortSpectatorCommand(ShortSpectator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args){

        // Checks if they supplied any arguments
        if(args.length != 1)
            return false;

        // Checks for the reload command and proper permissions
        if(args[0].equals("reload") && sender.hasPermission("shortspectator.reload"))
        {
            plugin.loadConfigValues();
            sender.sendMessage("§7[ShortSpectator] §aThe config has been reloaded!§r");
        }
        // Checks for the toggle command and proper permissions
        else if(args[0].equals("toggle") && sender.hasPermission("shortspectator.toggle"))
        {
            if(plugin.enabled)
                plugin.getConfig().set("enabled", false);

            else
                plugin.getConfig().set("enabled", true);

            plugin.saveConfig();
            plugin.loadConfigValues();

            if(plugin.enabled)
                sender.sendMessage("§7[ShortSpectator] §aThe plugin has been enabled!§r");
            else
                sender.sendMessage("§7[ShortSpectator] §cThe plugin has been disabled!§r");
        }

        return true;
    }
}
