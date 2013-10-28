package io.snw.magicfurnace.factions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface FactionsHook {

    public boolean isFactionMember(Player player, Location loc);

    public String getVersion();

    public boolean isWilderness(Location loc);

}
