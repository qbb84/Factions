package main.conquer.Listeners;

import main.conquer.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Objects;

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

    //TODO Have each player that is invited to have a unique countdown. [Faction: [Player:Countdown]]

    /*
    TODO For faction allies and enemy.
    TODO 1. You will need to send someone an ally request which expires after 24hrs, and the command has a 1 minute cooldown.
    TODO 2. If the ally request is accepted then you will be their ally, and they will be yours. (If they were an enemy that'll  be removed) (faction.allyrequests)
    TODO 3. If they enemy you then the ally is removed from you and the enemy is shown only for them.
    TODO 4. Each faction can have different enemies.

    --

    TODO FACTION RANKS
     */


    public FactionListener() {
        this.faction = new HashMap<>();
        list = new ArrayList<>();
    }

    //TODO add invite and also check if open/closed
    public void joinFaction(Faction faction, Player player, String factionName) {
        if (Main.getMain().getCustomConfig().getConfigurationSection(factionName) != null) {
            if (getFactionOfPlayer(player).equalsIgnoreCase("null")) {
                setMembers(player, factionName, true);
                sendEventMessage(faction, player, factionName);
            } else player.sendMessage(ChatColor.RED + "Whoops, you're already a member of a Faction!");
        }

    }

    public void leaveFaction(Faction faction, Player player) {
        if (getMembers(getFactionOfPlayer(player)).contains(player.getName())) {
            player.sendMessage(faction.getMessage() + " " + getFactionOfPlayer(player));
            setMembers(player, false);
        } else player.sendMessage("err");
    }

    //TODO Message loop through each player in faction, give a message
    public void disbandFaction(Faction faction, Player player) {
        if (player.getName().equals(getFactionLeader(player))) {
            String getFaction = getFactionOfPlayer(player);

            Main.getMain().getCustomConfig().set(getFaction, null);

            getMembers(getFaction).clear();
            this.faction.get(getFaction).clear();
            list.remove(getFaction);
            //Test of members loop
            for (String players : getMembers(getFactionOfPlayer(player))) {
                if (players.equalsIgnoreCase(player.getName())) {
                    continue;
                } else {
                    sendEventMessage(faction, Bukkit.getPlayer(players));
                }
            }
            saveConfig();
        }
    }

    //TODO update set methods
    public void createFaction(Faction faction, Player player, String factionName) {
        if (!getFactionOfPlayer(player).equalsIgnoreCase("null")) return;
        if (Main.getMain().getCustomConfig().getConfigurationSection(factionName) == null) {
            LinkedHashSet<String> members = new LinkedHashSet<>();
            LinkedHashSet<String> allies = new LinkedHashSet<>();
            LinkedHashSet<String> enemies = new LinkedHashSet<>();
            this.faction.put(factionName, members);

            list.add(this.faction);

            createConfigSection(factionName);
            addConfigSectionChildren(factionName, "leader.name", player.getName());
            addConfigSectionChildren(factionName, "members", members.toArray());
            addConfigSectionChildren(factionName, "allies", allies.toArray());
            addConfigSectionChildren(factionName, "enemies", enemies.toArray());
            addConfigSectionChildren(factionName, "description", "Empty");
            addConfigSectionChildren(factionName, "settings.joining", false);
            addConfigSectionChildren(factionName, "settings.undersiege", false);
            addConfigSectionChildren(factionName, "settings.attackable", false);
            addConfigSectionChildren(factionName, "settings.size", members.size() + 1);


            saveConfig();
            //TODO Default children
        } else {
            player.sendMessage(ChatColor.RED + "" + faction + " already exists!");
        }

    }


    public synchronized void createConfigSection(String sectionNameParent) {
        if (Main.getMain().getCustomConfig().getConfigurationSection(sectionNameParent) == null) {
            Main.getMain().getCustomConfig().createSection(sectionNameParent);
            saveConfig();
        }

    }

    public synchronized void addConfigSectionChildren(String sectionNameParent, String path, Object value) {
        //TODO Conditionals if !exist
        if (Main.getMain().getCustomConfig().getConfigurationSection(sectionNameParent) != null) {
            Objects.requireNonNull(Main.getMain().getCustomConfig().getConfigurationSection(sectionNameParent)).set(path, value);
            saveConfig();
        }

    }

    public void saveConfig() {
        try {
            Main.getMain().save();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            Bukkit.getServer().getConsoleSender().sendMessage("Config successfully saved.");
        }

    }


    public LinkedHashSet<String> getMembers(String factionName) {

        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        if (Main.getMain().getCustomConfig().getConfigurationSection(factionName) != null) {
            if (Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("members").size() > 0) {
                for (String members : Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("members")) {
                    hashSet.add(members.toString());
                    return hashSet;
                }
            }
        } else return null;
        return hashSet;


    }

    public LinkedHashSet<String> getMembersOnline(String factionName) {
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        if (Main.getMain().getCustomConfig().getConfigurationSection(factionName) != null) {
            String leader = Main.getMain().getCustomConfig().getConfigurationSection(factionName).getString("leader.name");
            String leaderWithSymbol = "\u265A" + leader;
            if (Bukkit.getPlayer(leader) != null) hashSet.add(leaderWithSymbol);

            if (Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("members").size() > 0) {
                for (String members : Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("members")) {
                    if (Bukkit.getPlayer(members) != null) {
                        hashSet.add(members);
                        return hashSet;

                    }
                }
            }
        } else return null;
        return hashSet;


    }

    public LinkedHashSet<String> getMembersOffline(String factionName) {
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        if (Main.getMain().getCustomConfig().getConfigurationSection(factionName) != null) {
            String leader = Main.getMain().getCustomConfig().getConfigurationSection(factionName).get("leader.name").toString();
            String leaderWithSymbol = "\u265A" + leader;
            if (Bukkit.getPlayer(leader) == null) hashSet.add(leaderWithSymbol);
            if (Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("members").size() > 0) {
                for (String members : Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("members")) {
                    if (Bukkit.getPlayer(members) == null) {
                        hashSet.add(members.toString());
                        return hashSet;

                    }
                }
            }
        } else return null;
        return hashSet;


    }

    public LinkedHashSet<String> getAllies(String factionName) {
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        if (factionExists(factionName)) {
            if (!Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("allies").isEmpty()) {
                for (String allies : Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("allies")) {
                    hashSet.add(allies);
                    return hashSet;
                }
            }
        }

        return hashSet;
    }

    public LinkedHashSet<String> getEnemies(String factionName) {
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        if (factionExists(factionName)) {
            if (!Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("enemies").isEmpty()) {
                for (String allies : Main.getMain().getCustomConfig().getConfigurationSection(factionName).getStringList("enemies")) {
                    hashSet.add(allies);
                    return hashSet;
                }
            }
        }

        return hashSet;
    }

    public void removeEnemy(Player player, String factionName) {
        if (factionExists(factionName)) {
            if (getEnemies(getFactionOfPlayer(player)).contains(factionName)) {
                LinkedHashSet<String> enemies = getEnemies(getFactionOfPlayer(player));
                enemies.remove(factionName);
                Main.getMain().getCustomConfig().getConfigurationSection(getFactionOfPlayer(player)).set("enemies", enemies.toArray());
                player.sendMessage(ChatColor.AQUA + "You have removed " + ChatColor.RED + factionName + ChatColor.AQUA + " as an " + ChatColor.RED + "Enemy");
                saveConfig();
            }
        }
    }

    public void removeAlly(Player player, String factionName) {
        if (factionExists(factionName)) {
            if (getAllies(getFactionOfPlayer(player)).contains(factionName)) {
                LinkedHashSet<String> allies = getAllies(getFactionOfPlayer(player));
                allies.remove(factionName);
                Main.getMain().getCustomConfig().getConfigurationSection(getFactionOfPlayer(player)).set("allies", allies.toArray());
                player.sendMessage(ChatColor.AQUA + "You have removed " + ChatColor.GREEN + factionName + ChatColor.AQUA + " as an " + ChatColor.GREEN + "Ally!");
                saveConfig();
            }
        }
    }

    public void setAllies(Player player, String factionName) {
        if (factionExists(factionName)) {
            if (!getFactionOfPlayer(player).equalsIgnoreCase(factionName)) {
                if (!getAllies(getFactionOfPlayer(player)).contains(factionName)) {
                    if (getEnemies(getFactionOfPlayer(player)).contains(factionName)) {
                        player.sendMessage(ChatColor.AQUA + "Please remove " + ChatColor.RED + factionName + ChatColor.AQUA + " as an " + ChatColor.RED + "Enemy" + ChatColor.AQUA
                                + " before setting them as an " + ChatColor.GREEN + " Ally!");
                        return;
                    }
                    LinkedHashSet<String> allies = getAllies(getFactionOfPlayer(player));
                    allies.add(factionName);
                    Main.getMain().getCustomConfig().getConfigurationSection(getFactionOfPlayer(player)).set("allies", allies.toArray());
                    player.sendMessage(ChatColor.AQUA + "You have added " + ChatColor.GREEN + factionName + ChatColor.AQUA + " as an " + ChatColor.GREEN + "Ally!");
                    saveConfig();
                }

            }
        }
    }

    public void setEnemies(Player player, String factionName) {
        if (factionExists(factionName)) {
            if (!getFactionOfPlayer(player).equalsIgnoreCase(factionName)) {
                if (!getEnemies(getFactionOfPlayer(player)).contains(factionName))
                    if (getAllies(getFactionOfPlayer(player)).contains(factionName)) {
                        player.sendMessage(ChatColor.AQUA + "Please remove " + ChatColor.GREEN + factionName + ChatColor.AQUA + " as an " + ChatColor.GREEN + "Ally" + ChatColor.AQUA
                                + " before setting them as an" + ChatColor.RED + " Enemy!");
                        return;
                    }
                getAllies(factionName).remove(factionName);
                LinkedHashSet<String> enemies = getEnemies(getFactionOfPlayer(player));
                enemies.add(factionName);
                Main.getMain().getCustomConfig().getConfigurationSection(getFactionOfPlayer(player)).set("enemies", enemies.toArray());
                player.sendMessage(ChatColor.AQUA + "You have added " + ChatColor.GREEN + factionName + ChatColor.AQUA + " as an " + ChatColor.RED + " Enemy!");
                saveConfig();


            }
        }
    }

    public void setClosedOrOpen(String faction, Player player, boolean openOrClose) {
        if (getFactionOfPlayer(player).equalsIgnoreCase("null")) {
            player.sendMessage(ChatColor.RED + "You must be in a faction to use this command.");
            return;
        } else if (!getFactionLeader(getFactionOfPlayer(player)).equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatColor.RED + "You must be a faction leader to use this command.");
            return;
        }
        boolean joining = Main.getMain().getCustomConfig().getConfigurationSection(faction).getBoolean("settings.joining");

        if (Main.getMain().getCustomConfig().getConfigurationSection(faction).getBoolean("settings.joining") == openOrClose) {
            String openOrClosed = (joining) ? ChatColor.GREEN + "Open" : ChatColor.RED + "Closed";
            player.sendMessage(ChatColor.AQUA + "Joining is already set to " + openOrClosed);
            return;
        }

        Main.getMain().getCustomConfig().getConfigurationSection(faction).set("settings.joining", openOrClose);
        saveConfig();

        String openOrClosed = (Main.getMain().getCustomConfig().getConfigurationSection(faction).getBoolean("settings.joining")) ? ChatColor.GREEN + "Open" : ChatColor.RED + "Closed";
        player.sendMessage(ChatColor.AQUA + "Joining set to " + openOrClosed);


    }


    public boolean factionExists(String factionName) {
        return Main.getMain().getCustomConfig().getConfigurationSection(factionName) != null;
    }

    //TODO Fix getting faction from a player member
    public String getFactionOfPlayer(Player player) {
        for (String keys : Main.getMain().getCustomConfig().getConfigurationSection("").getKeys(false)) {
            if (Main.getMain().getCustomConfig().getConfigurationSection(keys).get("leader.name").equals(player.getName()) ||
                    Main.getMain().getCustomConfig().getConfigurationSection(keys).getStringList("members").contains(player.getName())) {
                return keys;
            }

            for (String members : Main.getMain().getCustomConfig().getConfigurationSection(keys).getStringList("members")) {
                if (members.equalsIgnoreCase(player.getName())) {
                    return keys;
                }
            }

        }
        return "null";
    }

    public String getFactionLeader(String faction) {
        return Main.getMain().getCustomConfig().getConfigurationSection(faction).getString("leader.name");

    }

    public String getFactionLeader(Player player) {
        String factionOfPlayer = getFactionOfPlayer(player);
        return (String) Main.getMain().getCustomConfig().getConfigurationSection(factionOfPlayer).get("leader.name");

    }

    public void setDescription(Player player, StringBuilder builder) {
        Main.getMain().getCustomConfig().getConfigurationSection(getFactionOfPlayer(player)).set("description", builder.toString() + "...");
        player.sendMessage("Set description to" + builder);
        saveConfig();
    }

    public void setMembers(Player player, boolean addOrRemove) {
        if (addOrRemove) {
            LinkedHashSet<String> members = getMembers(getFactionOfPlayer(player));
            members.add(player.getName());
            this.faction.put(getFactionOfPlayer(player), members);
            Main.getMain().getCustomConfig().getConfigurationSection(getFactionOfPlayer(player)).set("members", members.toArray());
            saveConfig();
        } else {
            LinkedHashSet<String> members = getMembers(getFactionOfPlayer(player));
            members.remove(player.getName());
            this.faction.put(getFactionOfPlayer(player), members);
            Main.getMain().getCustomConfig().getConfigurationSection(getFactionOfPlayer(player)).set("members", members.toArray());
            saveConfig();
        }

    }


    public void setMembers(Player player, String factionName, boolean addOrRemove) {
        if (addOrRemove) {
            LinkedHashSet<String> members = getMembers(factionName);
            members.add(player.getName());
            this.faction.put(factionName, members);
            Main.getMain().getCustomConfig().getConfigurationSection(factionName).set("members", members.toArray());
            saveConfig();
        } else {
            LinkedHashSet<String> members = getMembers(factionName);
            members.remove(player.getName());
            this.faction.put(factionName, members);
            Main.getMain().getCustomConfig().getConfigurationSection(factionName).set("members", members.toArray());
            saveConfig();
        }

    }

    public void sendEventMessage(Faction faction, Player player) {
        switch (faction) {
            case CREATE, LEAVE, JOIN, DISBAND, PROMOTE, MENU -> player.sendMessage(faction.getMessage());
        }

    }

    public void sendEventMessage(Faction faction, Player player, String extraMessage) {
        switch (faction) {
            case CREATE, LEAVE, JOIN, DISBAND, PROMOTE, MENU -> player.sendMessage(faction.getMessage() + extraMessage);
        }
    }


}
