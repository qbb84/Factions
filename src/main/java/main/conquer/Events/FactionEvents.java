package main.conquer.Events;

import main.conquer.Commands.cooldowns.CommandCooldown;
import main.conquer.Listeners.FactionListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FactionEvents implements Listener {

    //TODO Save members as a hashmap, key = faction | value = members
    //TODO Getter for config, check if keys/value exists before setting them *updating*

    @EventHandler
    public void onBreak(BlockBreakEvent event) throws IOException {
        FactionListener listener = new FactionListener();

        for (Map.Entry<String, HashMap<String, Integer>> entry : CommandCooldown.getC1ass().commandCooldown.entrySet()) {
            event.getPlayer().sendMessage(entry.getKey() + " : " + entry.getValue());
        }


        event.getPlayer().sendMessage(listener.getMembers(listener.getFactionOfPlayer(event.getPlayer())).toString());
        event.getPlayer().sendMessage(listener.getMembersOnline(listener.getFactionOfPlayer(event.getPlayer())).toString());
        event.getPlayer().sendMessage(listener.getMembers1(listener.getFactionOfPlayer(event.getPlayer())).toString());
    }
}
