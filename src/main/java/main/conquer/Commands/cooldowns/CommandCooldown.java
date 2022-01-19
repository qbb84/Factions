package main.conquer.Commands.cooldowns;

import main.conquer.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class CommandCooldown {

    //TODO Use generics

    private static final CommandCooldown c1ass = new CommandCooldown();
    public HashMap<String, HashMap<String, Integer>> commandCooldown;
    private HashMap<String, Integer> cooldowns;

    public CommandCooldown() {
        this.commandCooldown = new HashMap<>();
        this.cooldowns = new HashMap<>();
    }

    public CommandCooldown(HashMap<String, HashMap<String, Integer>> commandCooldown) {
        this.commandCooldown = commandCooldown;
    }

    public static CommandCooldown getC1ass() {
        return c1ass;
    }

    public void addCooldown(String mainKey, String key, Integer value) {
        cooldowns.put(key, value);
        commandCooldown.put(mainKey, cooldowns);

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
}
