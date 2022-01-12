package main.conquer.Commands;

import main.conquer.FontMessage.CenteredChatMessage;
import main.conquer.FontMessage.DefaultFontInfo;
import main.conquer.Listeners.Faction;
import main.conquer.Listeners.FactionListener;
import main.conquer.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.HashMap;

public class CommandListeners extends FactionListener {


    private final HashMap<String, ArrayList<String>> invitedPlayers;

    private String chatDescription = " We're the earthquake to your claimed land...";



    public CommandListeners(){
        invitedPlayers = new HashMap<>();
    }
//TODO Message values
    public void showFaction(String factionName, Player commandSender){
        if(!factionExists(factionName)){
            commandSender.sendMessage(ChatColor.RED + "That faction doesn't exist!");
            return;
        }

        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.YELLOW+ ""+ChatColor.UNDERLINE + "____________________________________________________");
        commandSender.sendMessage("");
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.AQUA+""+ ChatColor.BOLD + "\u23F4 General \u23F5");
        commandSender.sendMessage(ChatColor.YELLOW+"\u2023 Faction Name: " + ChatColor.GREEN+ factionName);
        commandSender.sendMessage(ChatColor.YELLOW +"\u2023 Description: " + ChatColor.GREEN + chatDescription);
        commandSender.sendMessage("");
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.AQUA+""+ ChatColor.BOLD + "\u23F4 Members \u23F5");
        commandSender.sendMessage(ChatColor.YELLOW +"\u2023 Players Online:" + ChatColor.GOLD + " (1), "+ChatColor.GREEN + "\u265A Rewind");
        commandSender.sendMessage(ChatColor.YELLOW+"\u2023 Players Offline: " + ChatColor.GREEN + "0");
        commandSender.sendMessage("");
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.AQUA+""+ ChatColor.BOLD +"\u23F4 Relationship \u23F5");
        commandSender.sendMessage(ChatColor.YELLOW +"\u2023 Allies: " + ChatColor.GREEN + "null");
        commandSender.sendMessage(ChatColor.YELLOW+"\u2023 Enemies: " + ChatColor.RED + "null");
        commandSender.sendMessage("");
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.AQUA+""+ ChatColor.BOLD + "\u23F4 Extra \u23F5");
        commandSender.sendMessage(ChatColor.YELLOW+"\u2023 Under Siege / Attackable: "+ ChatColor.GREEN + "Yes / No (16h:45m)");
        commandSender.sendMessage(ChatColor.YELLOW+"\u2023 Joining: " + ChatColor.GREEN + "Open");
        commandSender.sendMessage(ChatColor.YELLOW+"\u2023 Total Members: " +ChatColor.GREEN + "1");
        CenteredChatMessage.sendCenteredMessage(commandSender, ChatColor.YELLOW+ ""+ChatColor.UNDERLINE + "\u239D____________________________________________________\u23A0");
        commandSender.sendMessage("");



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
