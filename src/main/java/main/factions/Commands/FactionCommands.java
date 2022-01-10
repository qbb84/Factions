package main.factions.Commands;

import main.factions.Listeners.Faction;
import main.factions.Listeners.FactionListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class FactionCommands implements CommandExecutor {

    FactionListener faction = new FactionListener();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(label.equalsIgnoreCase("faction")){
            if(args.length == 0) return true;
            if(!(sender instanceof Player)) return true;

            Player p = (Player) sender;

            switch (args[0].toLowerCase()){
                case "create":
                    if(args.length == 2) {
                        String factionName = args[1];
                        faction.createFaction(Faction.CREATE, p, factionName);
                        return true;

                    }
                case "join":
                    if(args.length == 2){
                        String factionName = args[1];
                        faction.joinFaction(Faction.JOIN, p, factionName);
                        return true;
                    }
                case "leave":
                    faction.leaveFaction(Faction.LEAVE, p);
            }

        }
        return false;
    }
}
