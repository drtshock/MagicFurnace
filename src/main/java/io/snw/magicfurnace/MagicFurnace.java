package io.snw.magicfurnace;

import io.snw.magicfurnace.util.MetricsLite;
import io.snw.magicfurnace.util.Updater;
import io.snw.magicfurnace.listener.SmeltListener;
import io.snw.magicfurnace.listener.JoinListener;
import io.snw.magicfurnace.util.Updater.UpdateResult;
import io.snw.magicfurnace.util.Updater.UpdateType;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author drtshock
 */
public class MagicFurnace extends JavaPlugin implements Listener {

    public static boolean useFactions = false;
    private MagicFurnace plugin;
    private boolean needsUpdate = false;
    private String newVersion = "";

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        bettyCrocker();
        getServer().getPluginManager().registerEvents(new SmeltListener(this), this);
        if (getConfig().getBoolean("use-factions") && getServer().getPluginManager().getPlugin("Factions") != null) {
            useFactions = true;
        }
        checkUpdate();
        startMetrics();
    }
    
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelAllTasks(); // Clean up after ourselves.
    }

    protected void bettyCrocker() {
        Material mat = Material.getMaterial(getConfig().getString("smeltme"));
        getServer().addRecipe(new FurnaceRecipe(new ItemStack(Material.RED_MUSHROOM, 1), mat));
    }

    // PROTECTED
    protected void checkUpdate() {
        if (getConfig().getBoolean("check-update")) {
            final File file = this.getFile();
            final Updater.UpdateType updateType = (getConfig().getBoolean("download-update") ? UpdateType.DEFAULT : UpdateType.NO_DOWNLOAD);
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(plugin, 67135, file, updateType, false);
                    needsUpdate = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
                    newVersion = updater.getLatestName();
                    if (updater.getResult() == UpdateResult.SUCCESS) {
                        getLogger().log(Level.INFO, "Successfully updated MagicFurnace to version {0} for next restart!", updater.getLatestName());
                    } else if (updater.getResult() == UpdateResult.NO_UPDATE) {
                        getLogger().log(Level.INFO, "We didn't find an update!");
                    }
                }
            });
            if (needsUpdate) {
                getServer().getPluginManager().registerEvents(new JoinListener(newVersion), this);
            }
        }
    }

    protected void startMetrics() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }
}
