package main.conquer.Commands;

import main.conquer.Listeners.Faction;
import main.conquer.Listeners.FactionListener;
import main.conquer.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandListeners extends FactionListener {


    private final HashMap<String, ArrayList<String>> invitedPlayers;


    public CommandListeners(){
        invitedPlayers = new HashMap<>();
    }

    public void showFaction(String factionName, Player commandSender){
        if(!factionExists(factionName)){
            commandSender.sendMessage(ChatColor.RED + "That faction doesn't exist!");
            return;
        }

        commandSender.sendMessage("<----| " + factionName + " |----->");
        commandSender.sendMessage("Leader: " + "null");
        commandSender.sendMessage("Members: " + getMembers(factionName));

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
