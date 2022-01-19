package main.conquer.Commands;

import main.conquer.Listeners.Faction;
import main.conquer.Listeners.FactionListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FactionCommands implements CommandExecutor {

    FactionListener faction = new FactionListener();

    CommandListeners listeners = new CommandListeners();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("faction")) {
            if (args.length == 0) return true;
            if (!(sender instanceof Player)) return true;

            Player p = (Player) sender;

            switch (args[0].toLowerCase()) {
                case "create":
                    if (args.length == 2) {
                        String factionName = args[1];
                        faction.createFaction(Faction.CREATE, p, factionName);
                        return true;

                    }
                case "join":
                    if (args.length == 2) {
                        String factionName = args[1];
                        faction.joinFaction(Faction.JOIN, p, factionName);
                        return true;
                    }
                case "leave":
                    faction.leaveFaction(Faction.LEAVE, p);
                    return true;

                case "who":
                case "list":
                    if (args.length == 1) {
                        if (!faction.getFactionOfPlayer(p).equalsIgnoreCase("null")) {
                            listeners.showFaction(faction.getFactionOfPlayer(p), p);
                        }
                    } else if (args.length == 2) {
                        String factionName = args[1];
                        listeners.showFaction(factionName, p);

                    }
                    return true;
                case "description":
                    if (faction.getFactionOfPlayer(p).equalsIgnoreCase("null")) {
                        p.sendMessage("You must be in a faction to run this command");
                        return true;
                    } else if (!faction.getFactionLeader(faction.getFactionOfPlayer(p)).equalsIgnoreCase(p.getName())) {
                        p.sendMessage("You must be a leader to use this command!");
                        return true;
                    }
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i <= args.length - 1; i++) {
                        builder.append(" ").append(args[i]);
                    }
                    if (builder.chars().filter(ch -> ch != ' ').count() <= 30) {
                        if (containsIllegals(builder.toString())) {
                            p.sendMessage("Only alphabet characters are allowed!");
                            return true;
                        }
                        faction.setDescription(p, builder);
                        return true;
                    } else p.sendMessage("Description needs to be less than 30 characters!");
                    return true;
                case "ally":
                    if (args.length == 2) {
                        String factionName = args[1];
                        faction.setAllyRequests(p, factionName);
                        return true;
                    }

                    return true;
                case "enemy":
                    if (args.length == 2) {
                        String factionName = args[1];
                        faction.setEnemies(p, factionName);
                        return true;
                    }
                    return true;
                case "remove":
                    if (args.length == 3) {
                        if (args[1].equalsIgnoreCase("ally")) {
                            String factionName = args[2];
                            faction.removeAlly(p, factionName);
                            return true;
                        } else if (args[1].equalsIgnoreCase("enemy")) {
                            String factionName = args[2];
                            faction.removeEnemy(p, factionName);
                            return true;
                        }

                    }
                case "open":
                    if (args.length == 1) {
                        faction.setClosedOrOpen(faction.getFactionOfPlayer(p), p, true);
                        return true;
                    }
                case "close":
                    if (args.length == 1) {
                        faction.setClosedOrOpen(faction.getFactionOfPlayer(p), p, false);
                        return true;
                    }

            }

        }
        return false;
    }

    public boolean containsIllegals(String toExamine) {
        Pattern pattern = Pattern.compile("[!~#@*+%{}<>\\[\\]|\"\\_^]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }
}
