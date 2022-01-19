package main.conquer.Commands;

import main.conquer.FontMessage.CenteredChatMessage;
import main.conquer.Listeners.Faction;
import main.conquer.Listeners.FactionListener;
import main.conquer.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class CommandListeners extends FactionListener {


    private final HashMap<String, ArrayList<String>> invitedPlayers;





    public CommandListeners(){
        invitedPlayers = new HashMap<>();
    }
    public void showFaction(String factionName, Player commandSender) {
        if (!factionExists(factionName)) {
            commandSender.sendMessage(ChatColor.RED + "That faction doesn't exist!");
            return;
        }

        String chatDescription = Main.getMain().getCustomConfig().getConfigurationSection(factionName).getString("description");
        String factionSize = Main.getMain().getCustomConfig().getConfigurationSection(factionName).getString("settings.size");

        boolean underSiege = Main.getMain().getCustomConfig().getConfigurationSection(factionName).getBoolean("settings.undersiege");
        boolean attackable = Main.getMain().getCustomConfig().getConfigurationSection(factionName).getBoolean("settings.attackable");
        boolean joining = Main.getMain().getCustomConfig().getConfigurationSection(factionName).getBoolean("settings.joining");

        String openOrClosed = (joining) ? ChatColor.GREEN + "Open" : ChatColor.RED + "Closed";
        String siege = (underSiege) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No";
        String attack = (attackable) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No";

        LinkedHashSet<String> onlinePlayers = getMembersOnline(factionName);
        LinkedHashSet<String> offlinePlayers = getMembersOffline(factionName);
        LinkedHashSet<String> allies = getAllies(factionName);
        LinkedHashSet<String> enemies = getEnemies(factionName);


        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "____________________________________________________");
        commandSender.sendMessage("");
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.AQUA + "" + ChatColor.BOLD + "\u23F4 General \u23F5");
        commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Faction Name: " + ChatColor.GREEN + ChatColor.BOLD + factionName);
        commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Description: " + ChatColor.GREEN + chatDescription);
        commandSender.sendMessage("");
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.AQUA + "" + ChatColor.BOLD + "\u23F4 Members \u23F5");
        if (onlinePlayers.size() != 0) {
            commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Players Online:" + ChatColor.GOLD + " (" + onlinePlayers.size() + 1 + ") " + ChatColor.GREEN + Arrays.asList(onlinePlayers).iterator().next().toString().replaceAll("\\]", "").replaceAll("\\[", ""));
        } else
            commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Players Online:" + ChatColor.GOLD + ChatColor.GREEN + " None");
        if (offlinePlayers.size() != 0) {
            commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Players Offline:" + ChatColor.GOLD + " (" + offlinePlayers.size() + 1 + ") " + ChatColor.GREEN + Arrays.asList(offlinePlayers).iterator().next().toString().replaceAll("\\]", "").replaceAll("\\[", ""));
        } else
            commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Players Offline:" + ChatColor.GOLD + ChatColor.GREEN + " None");
        commandSender.sendMessage("");
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.AQUA + "" + ChatColor.BOLD + "\u23F4 Relationship \u23F5");
        commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Allies: " + ChatColor.GREEN + Arrays.asList(allies).iterator().next().toString().replaceAll("\\]", "").replaceAll("\\[", ""));
        commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Enemies: " + ChatColor.RED + Arrays.asList(enemies).iterator().next().toString().replaceAll("\\]", "").replaceAll("\\[", ""));

        commandSender.sendMessage("");
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.AQUA + "" + ChatColor.BOLD + "\u23F4 Extra \u23F5");
        commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Under Siege / Attackable: " + siege + ChatColor.GREEN + " / " + attack);
        commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Joining: " + ChatColor.GREEN + openOrClosed);
        commandSender.sendMessage(ChatColor.YELLOW + "\u2023 Total Members: " + ChatColor.GREEN + factionSize);
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "\u239D____________________________________________________\u23A0");
        commandSender.sendMessage("");


        for (String player : onlinePlayers) {
            commandSender.sendMessage(player);
        }


    }


    public void invitePlayer(Faction faction, Player player, Player invitedPlayer) {
        if (!invitedPlayers.get(getFactionOfPlayer(player)).contains(invitedPlayer.getUniqueId().toString())) {
            return;
        }
        if (invitedPlayers.get(getFactionOfPlayer(player)) != null) {
            ArrayList<String> invPlayers = invitedPlayers.get(getFactionOfPlayer(player));
            invPlayers.add(invitedPlayer.getUniqueId().toString());
            invitedPlayers.put(getFactionOfPlayer(player), invPlayers);
        } else {
            ArrayList<String> invPlayers = new ArrayList<>();
            invPlayers.add(invitedPlayer.getUniqueId().toString());
            invitedPlayers.put(getFactionOfPlayer(player), invPlayers);
        }

        final int[] i = {180};
        ArrayList<String> invPlayers = invitedPlayers.get(getFactionOfPlayer(player));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!invPlayers.contains(invitedPlayer.getUniqueId().toString())) return;
                this.cancel();

                i[0]--;

                if (i[0] == 0) {
                    invPlayers.remove(invitedPlayer.getUniqueId().toString());
                    invitedPlayers.put(getFactionOfPlayer(player), invPlayers);
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getMain(), 0, 20);

    }


}
