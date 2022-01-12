package main.conquer.Listeners;

import org.bukkit.ChatColor;

public enum Faction {

    LEAVE(ChatColor.RED,"You have left the Faction "),
    JOIN (ChatColor.AQUA, "You have joined the Faction "),
    CREATE (ChatColor.GREEN, "You have created the Faction "),
    DISBAND(ChatColor.RED, "You have disbanded the Faction "),
    PROMOTE (ChatColor.DARK_AQUA, "You have promoted member "),
    MENU (ChatColor.GREEN, "Opened Faction menu.");

    private ChatColor color;
    private String message;
     Faction(ChatColor color, String message){
         this.color = color;
         this.message = message;
     }

    public ChatColor getColor() {
        return color;
    }

    public String getMessage() {
        return message;
    }
}
