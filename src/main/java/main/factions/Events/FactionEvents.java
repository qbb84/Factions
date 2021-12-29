package main.factions.Events;

import main.factions.Listeners.Faction;
import main.factions.Listeners.FactionListener;
import main.factions.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;

public class FactionEvents implements Listener {

    //TODO Save members as a hashmap, key = faction | value = members
    //TODO Getter for config, check if keys/value exists before setting them *updating*

    @EventHandler
    public void onBreak(BlockBreakEvent event) throws IOException {

        switch(event.getBlock().getType()){
            case GRASS_BLOCK:
                int size = 0;
                Main.getMain().getCustomConfig().createSection(event.getBlock().getType().name());
                Main.getMain().getCustomConfig().getConfigurationSection(event.getBlock().getType().toString()).set("location", event.getBlock().getLocation());
                Main.getMain().getCustomConfig().getConfigurationSection(event.getBlock().getType().toString()).set("members", event.getPlayer().getName());
                Main.getMain().getCustomConfig().getConfigurationSection(event.getBlock().getType().toString()).set("members.size", size=size+1);
                Main.getMain().save();
                event.getPlayer().sendMessage("sucsess.");
                break;
            case DIAMOND_BLOCK:
                Main.getMain().getCustomConfig().createSection(event.getBlock().getType().toString());
                Main.getMain().getCustomConfig().getConfigurationSection(event.getBlock().getType().toString()).set("location", event.getBlock().getLocation());
                Main.getMain().save();
                event.getPlayer().sendMessage("sucsess.");
                break;
            default:

        }
    }
}
