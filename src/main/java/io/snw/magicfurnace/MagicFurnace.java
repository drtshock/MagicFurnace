package io.snw.magicfurnace;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author drtshock
 */
public class MagicFurnace extends JavaPlugin {
    
    public static boolean useFactions = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        bettyCrocker();
        getServer().getPluginManager().registerEvents(new SmeltListener(this), this);
        if(getServer().getPluginManager().getPlugin("Factions") != null) {
            useFactions = true;
        }
    }

    protected void bettyCrocker() {
        getServer().addRecipe(new FurnaceRecipe(new ItemStack(Material.RED_MUSHROOM, 1), Material.DIAMOND));
    }
}
