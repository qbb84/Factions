package main.conquer.Commands.cooldowns;

import main.conquer.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class CommandCooldown {

    //TODO Use generics

    public HashMap<String, HashMap<String, Integer>> commandCooldown;
    private HashMap<String, Integer> cooldowns;

    private static CommandCooldown cooldown = new CommandCooldown();

    public CommandCooldown() {
        this.commandCooldown = new HashMap<>();
    }

    public CommandCooldown(HashMap<String, HashMap<String, Integer>> commandCooldown, HashMap<String, Integer> cooldowns) {
        this.commandCooldown = commandCooldown;
        this.cooldowns = cooldowns;
    }

    public static CommandCooldown getCooldown() {
        return cooldown;
    }

    public void addCooldown(String mainKey, String key, Integer value) {
        if (commandCooldown.get(mainKey) == null) {
            this.cooldowns = new HashMap<>();
            cooldowns.put(key, value);
            commandCooldown.put(mainKey, cooldowns);
            return;
        }
        HashMap<String, Integer> getMap = commandCooldown.get(mainKey);
        getMap.put(key, value);
        commandCooldown.put(mainKey, getMap);


    }

    public void startCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String key : commandCooldown.keySet()) {
                    for (Map.Entry<String, Integer> e : commandCooldown.get(key).entrySet()) {
                        if (e.getValue() != 0) {
                            int newTime = e.getValue() - 1;
                            commandCooldown.get(key).put(e.getKey(), newTime);
                        } else {
                            commandCooldown.get(key).remove(e.getKey());
                        }

                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getMain(), 0, 20);

    }

    public void removeAndUpdate(String mainkey, String keyToRemove) {
        HashMap<String, Integer> getMap = commandCooldown.get(mainkey);
        for (Map.Entry<String, Integer> entry : getMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(keyToRemove)) {
                getMap.remove(entry.getKey(), entry.getValue());
            }
        }
        commandCooldown.put(mainkey, getMap);
    }
}
