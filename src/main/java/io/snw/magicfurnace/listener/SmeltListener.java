package io.snw.magicfurnace.listener;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;
import io.snw.magicfurnace.MagicFurnace;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author drtshock
 */
public class SmeltListener implements Listener {

    private MagicFurnace plugin;
    private int range;
    private Material normal;
    private Material nether;
    private Material end;
    private boolean allowWilderness;

    public SmeltListener(MagicFurnace p) {
        this.plugin = p;
        this.range = plugin.getConfig().getInt("range");
        this.normal = Material.getMaterial(plugin.getConfig().getString("material.normal"));
        this.nether = Material.getMaterial(plugin.getConfig().getString("material.nether"));
        this.end = Material.getMaterial(plugin.getConfig().getString("material.end"));
        this.allowWilderness = plugin.getConfig().getBoolean("allow-in-wilderness");
    }

    @EventHandler
    public void onSmelly(FurnaceSmeltEvent event) {
        if (event.getResult().equals(new ItemStack(Material.RED_MUSHROOM))) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                deliverPizza(player, event.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getType().equals(InventoryType.FURNACE) && event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.getMaterial(plugin.getConfig().getString("smeltme"))) && !((Player) event.getWhoClicked()).hasPermission("magicfurnace.use")) {
            event.setCancelled(true);
            ((Player) event.getWhoClicked()).sendMessage(ChatColor.DARK_RED + "You don't have permission for magic furnaces.");
        }
    }

    // Protect the pizza delivery guy.
    protected void deliverPizza(final Player player, Location loc) {
        if (!player.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())) {
        } else {
            if (loc.distance(player.getLocation()) < 90) {

                if (MagicFurnace.useFactions) {
                    UPlayer p = UPlayer.get(player);
                    String f1 = p.getFactionName();
                    String f2 = BoardColls.get().getFactionAt(PS.valueOf(loc)).getName();
                    boolean isWilderness = FactionColls.get().getForUniverse(loc.getWorld().getName()).getNone().isDefault();

                    // Need to check to make sure the furnace isn't in wilderness and the player isn't in the wilderness faction (stupid factions).
                    if ((f1.equalsIgnoreCase(f2) && !isWilderness) || (!this.allowWilderness && isWilderness)) {
                        return;
                    }
                }

                final List<Block> blocks = new ArrayList<Block>();
                int minX = loc.getBlockX() - range / 2;
                int minY = loc.getBlockY() - range / 2;
                int minZ = loc.getBlockZ() - range / 2;

                // Gets material from constructor which got it from config. Default to normal.
                Material mat = loc.getWorld().getEnvironment() == Environment.NETHER ? this.nether : loc.getWorld().getEnvironment() == Environment.THE_END ? this.end : this.normal;

                for (int x = minX; x < minX + range; x++) {
                    for (int y = minY; y < minY + range; y++) {
                        for (int z = minZ; z < minZ + range; z++) {
                            Location pizza = new Location(loc.getWorld(), x, y, z);
                            player.sendBlockChange(pizza, mat, (byte) 0);
                            Block block = pizza.getBlock();
                            if (!block.getType().equals(Material.BURNING_FURNACE) || !block.getType().equals(Material.FURNACE)) {
                                blocks.add(block);
                            }
                        }
                    }
                }
                // Clean up those blocks. Needs to be sent BEFORE another stone block is set.
                // I found 200 ticks is about as close as you can get to minimize flicker.
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        for (Block b : blocks) {
                            player.sendBlockChange(b.getLocation(), b.getType(), (byte) 0);
                        }
                    }
                }, 200L);
            }
        }
    }
}
