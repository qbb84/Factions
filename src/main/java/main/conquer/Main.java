package main.conquer;

import main.conquer.Commands.FactionCommands;
import main.conquer.Commands.cooldowns.CommandCooldown;
import main.conquer.Events.FactionEvents;
import main.conquer.Listeners.FactionListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class Main extends JavaPlugin {

    private boolean isOn = true;
    private final String text = (isOn) ? ChatColor.GREEN + "Factions enabled." : ChatColor.RED + "Factions disabled.";

    private FileConfiguration customConfig;
    private File customConfigFile;

    private static Main main;

    private FactionListener listener = new FactionListener();


    @Override
    public void onEnable() {
        enableAndDisableMessage(true);
        main = this;

        createCustomConfig();
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new FactionEvents(), this);
        getCommand("faction").setExecutor(new FactionCommands());


        loadAllyRequestMap();

        CommandCooldown.getC1ass().startCountdown();


    }

    @Override
    public void onDisable() {
        enableAndDisableMessage(false);
        saveConfig();

    }

    public static Main getMain() {
        return main;
    }

    public void enableAndDisableMessage(boolean isOn) {
        this.isOn = isOn;
        getServer().getConsoleSender().sendMessage(text);
    }


    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    public void save() throws IOException, InvalidConfigurationException {
        getCustomConfig().save(customConfigFile);
        getCustomConfig().load(customConfigFile);

    }

    public void loadAllyRequestMap() {
        for (String keyName : getCustomConfig().getConfigurationSection("").getKeys(false)) {
            ArrayList<String> list = new ArrayList<>();
            listener.getAllyRequests().put(keyName, list);
            getServer().getConsoleSender().sendMessage(keyName);

            if (listener.getAllyRequests().get(keyName).isEmpty()) {
                getServer().getConsoleSender().sendMessage("Empty");
            } else {
                getServer().getConsoleSender().sendMessage(listener.getAllyRequests().get(keyName).iterator().next());
            }
        }
    }


}
