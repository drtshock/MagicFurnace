package io.snw.magicfurnace.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author drtshock
 */
public class JoinListener implements Listener {

    private String newVersion;

    public JoinListener(String nv) {
        this.newVersion = nv;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("magicfurnace.notify")) {
            event.getPlayer().sendMessage(ChatColor.GOLD + "Version " + newVersion + " of MagicFurnace is up for download!");
            event.getPlayer().sendMessage(ChatColor.GOLD + "Get it here: " + ChatColor.RED + "dev.bukkit.org/cookie/magicfurnace");
        }
    }
}
