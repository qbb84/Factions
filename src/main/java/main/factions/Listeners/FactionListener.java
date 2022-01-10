package main.factions.Listeners;

import main.factions.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import javax.print.attribute.standard.JobKOctets;
import java.lang.reflect.Array;
import java.util.*;

public class FactionListener {

    /*
    TODO MAJOR CONDITIONALS!
     */

    //TODO Save hashmaps to a config on disable, and load them from the config onenable. (collection memory is wiped on server end)
    //TODO Test not having collections just config

    // HashMap for each faction created upon creation ->
    // key = factionName, value is members in order of insertion, (1 will always == the leader.)
    private final HashMap<String, LinkedHashSet<String>> faction;
    //Array of each faction for internal use
    private final ArrayList<HashMap<String, LinkedHashSet<String>>> list;


    public FactionListener(){
        this.faction = new HashMap<>();
        list = new ArrayList<>();
    }

    //Save arraylist in members, and just add, make sure to always get array
    //TODO boolean checks if invited for the future
    //TODO Check if player isn't in a Faction
    public void joinFaction(Faction faction, Player player, String factionName){
            if(!this.faction.get(factionName).contains(player.getName()) && Main.getMain().getCustomConfig().getConfigurationSection(factionName) != null){
                setMembers(player, true);
                sendEventMessage(faction, player, factionName);
            }else{
            player.sendMessage(ChatColor.RED + "Whoops, you're already a member of that Faction!");
            }

    }

    public void leaveFaction(Faction faction, Player player){
            setMembers(player, false);
            player.sendMessage(faction.getMessage() +" "+  getFactionOfPlayer(player));
            //update config
            //TODO Put new data in faction hashmap
    }

    //TODO Message loop through each player in faction, give a message
    public void disbandFaction(Faction faction, Player player){
        if(player.getName().equals(getFactionLeader(player))){
            String getFaction = getFactionOfPlayer(player);

            Main.getMain().getCustomConfig().set(getFaction, null);

             getMembers(getFaction).clear(); this.faction.get(getFaction).clear(); list.remove(getFaction);
             //Test of members loop
             for(String players : getMembers(getFactionOfPlayer(player))){
                 if(players.equalsIgnoreCase(player.getName())){
                     continue;
                 }else {
                     sendEventMessage(faction, Bukkit.getPlayer(players));
                 }
             }
            saveConfig();
        }
    }

    public void createFaction(Faction faction, Player player, String factionName)  {
        if(!getFactionOfPlayer(player).equalsIgnoreCase("null")) return;
        if(Main.getMain().getCustomConfig().getConfigurationSection(factionName) == null) {
            LinkedHashSet<String> members = new LinkedHashSet<>();
            this.faction.put(factionName, members);

            list.add(this.faction);

            createConfigSection(factionName);
            addConfigSectionChildren(factionName, "leader.name", player.getName());
            addConfigSectionChildren(factionName, "members", members.toArray());
            //TODO Default children
        }else{
           player.sendMessage(ChatColor.RED +""+ faction + " already exists!");
        }

    }



    public synchronized void createConfigSection(String sectionNameParent) {
        if(Main.getMain().getCustomConfig().getConfigurationSection(sectionNameParent) == null) {
            Main.getMain().getCustomConfig().createSection(sectionNameParent);
            saveConfig();
        }

    }

    public synchronized void addConfigSectionChildren(String sectionNameParent, String path, Object value){
        //TODO Conditionals if !exist
        if(Main.getMain().getCustomConfig().getConfigurationSection(sectionNameParent) != null) {
            Objects.requireNonNull(Main.getMain().getCustomConfig().getConfigurationSection(sectionNameParent)).set(path, value);
            saveConfig();
        }

    }
    public void saveConfig(){
        try {
            Main.getMain().save();
        }catch (Exception exception){
            exception.printStackTrace();
        }finally{
            Bukkit.getServer().getConsoleSender().sendMessage("Config successfully saved.");
        }

    }

    public LinkedHashSet<String> getMembers(String factionName){
        LinkedHashSet<String> members = this.faction.get(factionName);
        return members;

    }
    public String getFactionOfPlayer(@NotNull Player player){
        for (String keys: Main.getMain().getCustomConfig().getConfigurationSection("").getKeys(false)) {
            if(Main.getMain().getCustomConfig().getConfigurationSection(keys).get("leader.name").equals(player.getName()) ||
                    Main.getMain().getCustomConfig().getConfigurationSection(keys).get("members").equals(player.getName())){
                return keys;

            }

        }
        return "null";
    }

    public String getFactionLeader(Player player){
        String factionOfPlayer = getFactionOfPlayer(player);
        return (String) Main.getMain().getCustomConfig().getConfigurationSection(factionOfPlayer).get("leader.name");

    }

    //TODO Test
    public void setMembers(Player player, boolean addOrRemove){
        LinkedHashSet<String> members = getMembers(getFactionOfPlayer(player));
        if(addOrRemove) {
            members.add(player.getName());
            this.faction.put(getFactionOfPlayer(player), members);
            Main.getMain().getCustomConfig().getConfigurationSection(getFactionOfPlayer(player)).set("members", members.toArray());
        }else{
            members.remove(player.getName());
            this.faction.put(getFactionOfPlayer(player), members);
            Main.getMain().getCustomConfig().getConfigurationSection(getFactionOfPlayer(player)).set("members", members.toArray());
        }
        saveConfig();
    }

    public void sendEventMessage(Faction faction, Player player){
        switch (faction){
            case CREATE, LEAVE, JOIN, DISBAND, PROMOTE, MENU -> player.sendMessage(faction.getMessage());
        }

    }

    public void sendEventMessage(Faction faction, Player player, String extraMessage) {
        switch (faction) {
            case CREATE, LEAVE, JOIN, DISBAND, PROMOTE, MENU -> player.sendMessage(faction.getMessage() + extraMessage);
        }
    }


}
