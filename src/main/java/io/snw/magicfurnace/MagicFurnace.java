package io.snw.magicfurnace;

import io.snw.magicfurnace.listener.JoinListener;
import io.snw.magicfurnace.listener.SmeltListener;
import io.snw.magicfurnace.manager.Factions1694;
import io.snw.magicfurnace.manager.Factions182;
import io.snw.magicfurnace.manager.Factions2x;
import io.snw.magicfurnace.util.MetricsLite;
import io.snw.magicfurnace.util.Updater;
import io.snw.magicfurnace.util.Updater.UpdateResult;
import io.snw.magicfurnace.util.Updater.UpdateType;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author drtshock
 */
public class MagicFurnace extends JavaPlugin implements Listener {

    private boolean useFactions = false;
    private MagicFurnace plugin;
    private boolean needsUpdate = false;
    private String newVersion = "";
    Factions1694 factions1694;
    Factions182 factions182;
    Factions2x factions2x;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        bettyCrocker();
        getServer().getPluginManager().registerEvents(new SmeltListener(this), this);
        checkFactions();
        startMetrics();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelAllTasks(); // Clean up after ourselves.
    }

    private void checkFactions() {
        if (getConfig().getBoolean("use-factions")) {
            Plugin factions = getServer().getPluginManager().getPlugin("Factions");
            if (plugin != null) {
                String version = factions.getDescription().getVersion();
                String[] ver = version.split("\\.");
                String two = ver[0] + "." + ver[1];
                if (ver[0].equalsIgnoreCase("2")) {
                    getLogger().info("Factions " + version + " found. Hook enabled.");
                } else if (two.equalsIgnoreCase("1.8")) {
                    getLogger().info("Factions " + version + " found. Hook enabled.");
                }   else if (two.equalsIgnoreCase("1.6")) {
                    getLogger().info("Factions " + version + " found. Hook enabled.");
                } else {
                    getLogger().warning("Factions found but we don't support version: " + version);
                }
            } else {
                getLogger().info("Factions hook enabled, but Factions wasn't found. What were you thinking ;o");
            }
        }
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

    public boolean isUsingFactions() {
        return useFactions;
    }
}
