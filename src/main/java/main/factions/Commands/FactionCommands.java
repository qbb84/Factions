package main.factions.Commands;

import main.factions.Listeners.FactionListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FactionCommands implements CommandExecutor {

    FactionListener faction = new FactionListener();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(label.equalsIgnoreCase("faction")){
            if(args.length < 3) return false;
            if(sender instanceof Player) {
                Player p = (Player) sender;


            }
        }
        return false;
    }
}
