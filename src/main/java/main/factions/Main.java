package main.factions;

import main.factions.Commands.FactionCommands;
import main.factions.Events.FactionEvents;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    private boolean isOn = true;
    private final String text = (isOn) ? ChatColor.GREEN + "Factions enabled." : ChatColor.RED + "Factions disabled.";

    private FileConfiguration customConfig;
    private File customConfigFile;

    private static Main main;


    @Override
    public void onEnable() {
        enableAndDisableMessage(true);
        createCustomConfig();
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new FactionEvents(), this);
        getCommand("faction").setExecutor(new FactionCommands());

        main = this;



    }

    @Override
    public void onDisable() {
        enableAndDisableMessage(false);

    }

    public static Main getMain() {
        return main;
    }

    public void enableAndDisableMessage(boolean isOn){
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

    public<T> void setConfigPath(String path, T value){
         getCustomConfig().set(path, value);
         saveConfig();
    }

    public void save() throws IOException {
        getCustomConfig().save(customConfigFile);
    }

}
