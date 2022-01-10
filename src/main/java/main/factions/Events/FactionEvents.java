package main.factions.Events;

import main.factions.Listeners.Faction;
import main.factions.Listeners.FactionListener;
import main.factions.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;

public class FactionEvents implements Listener {

    //TODO Save members as a hashmap, key = faction | value = members
    //TODO Getter for config, check if keys/value exists before setting them *updating*

    @EventHandler
    public void onBreak(BlockBreakEvent event) throws IOException {
        event.getPlayer().sendMessage(new FactionListener().getFactionOfPlayer(event.getPlayer()));

    }
}
