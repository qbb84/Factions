package main.conquer.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;

public class FactionEvents implements Listener {

    //TODO Save members as a hashmap, key = faction | value = members
    //TODO Getter for config, check if keys/value exists before setting them *updating*

    @EventHandler
    public void onBreak(BlockBreakEvent event) throws IOException {

    }
}
