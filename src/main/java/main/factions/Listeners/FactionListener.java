package main.factions.Listeners;

import main.factions.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FactionListener {

    // HashMap for each faction created upon creation ->
    // key = factionName, value is members in order of insertion, (1 will always == the leader.)
    private final HashMap<String, LinkedHashSet<String>> faction;
    //Array of each faction for internal use
    private final ArrayList<HashMap<String, LinkedHashSet<String>>> list = new ArrayList<>();


    public FactionListener(){
       // list.add(faction);
        faction = new HashMap<>();
    }

    //Save arraylist in members, and just add, make sure to always get array
    public void joinFaction(Faction faction, Player player, String factionName){

    }

    public void leaveFaction(Faction faction, Player player){

    }

    public void disbandFaction(Faction faction, Player player){

    }

    public void createFaction(Faction faction, Player player, String factionName)  {
        LinkedHashSet<String> members = new LinkedHashSet<>();
        members.add(player.getName());
        createConfigSection(factionName);
        addConfigSectionChildren(factionName, "leader.name", player.getName());
        addConfigSectionChildren(factionName, "members", members.toArray());

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

    public List<String> getFactionMembers(Player player){
        return null;
    }
    public synchronized void createConfigSection(String sectionNameParent) {
        if(Main.getMain().getCustomConfig().getConfigurationSection(sectionNameParent) == null) {
            Main.getMain().getCustomConfig().createSection(sectionNameParent);
            saveConfig();
        }

    }

    public synchronized void addConfigSectionChildren(String sectionNameParent, String path, Object value){
        //TODO Conditionals if !exist
            Objects.requireNonNull(Main.getMain().getCustomConfig().getConfigurationSection(sectionNameParent)).set(path, value);
            saveConfig();

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


}
