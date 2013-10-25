package io.snw.magicfurnace.listener;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;
import io.snw.magicfurnace.MagicFurnace;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author drtshock
 */
public class SmeltListener implements Listener {

    private MagicFurnace plugin;
    private int large;
    private int medium;
    private int small;
    private Material normal;
    private Material nether;
    private Material end;
    private boolean allowWilderness;
    private int range;
    private HashMap<Location, Integer> furnaces = new HashMap<Location, Integer>();
    private List<Location> locs = new ArrayList<Location>();

    public SmeltListener(MagicFurnace p) {
        this.plugin = p;
        this.large = plugin.getConfig().getInt("large", 20);
        this.medium = plugin.getConfig().getInt("medium", 13);
        this.small = plugin.getConfig().getInt("small", 5);
        this.range = plugin.getConfig().getInt("small", 5);
        this.normal = Material.getMaterial(plugin.getConfig().getString("material.normal"));
        this.nether = Material.getMaterial(plugin.getConfig().getString("material.nether"));
        this.end = Material.getMaterial(plugin.getConfig().getString("material.end"));
        this.allowWilderness = plugin.getConfig().getBoolean("allow-in-wilderness");
    }

    @EventHandler
    public void onSmelly(FurnaceSmeltEvent event) {
        if (event.getResult() != null && event.getResult().equals(new ItemStack(Material.RED_MUSHROOM))) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                deliverPizza(player, event.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getType().equals(InventoryType.FURNACE) && event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.getMaterial(plugin.getConfig().getString("smeltme")))) {
            Player player = (Player) event.getWhoClicked();
            if (player.hasPermission("magicfurnace.use")) {
                Location loc = ((BlockState) event.getInventory().getHolder()).getBlock().getLocation();
                furnaces.put(loc, player.hasPermission("magicfurnace.size.large") ? large : player.hasPermission("magicfurnace.size.medium") ? medium : small);
            } else {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).sendMessage(ChatColor.DARK_RED + "You don't have permission for magic furnaces.");
            }
        }
    }

    // Protect the pizza delivery guy.
    protected void deliverPizza(final Player player, final Location loc) {
        locs.add(loc);
        if (!player.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())) {
            return;
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

                if (furnaces.get(loc) != null) {
                    this.range = furnaces.get(loc);
                }

                final List<Block> blocks = new ArrayList<Block>();
                int minX = loc.getBlockX() - range / 2;
                int minY = loc.getBlockY() - range / 2;
                int minZ = loc.getBlockZ() - range / 2;

                // Gets material from constructor which got it from config. Default to normal.
                Material mat = loc.getWorld().getEnvironment() == Environment.NETHER ? this.nether : loc.getWorld().getEnvironment() == Environment.THE_END ? this.end : this.normal;
                World world = loc.getWorld();
                for (int x = minX; x < minX + range; x++) {
                    for (int y = minY; y < minY + range; y++) {
                        for (int z = minZ; z < minZ + range; z++) {
                            Location pizza = new Location(world, x, y, z);
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
                        locs.remove(loc);
                        for (Block b : blocks) {
                            player.sendBlockChange(b.getLocation(), b.getType(), (byte) 0);
                        }
                    }
                }, 200L);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for (Location loc : locs) {
                    deliverPizza(event.getPlayer(), loc);
                }
            }
        }, 1L);

    }
}
