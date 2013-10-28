package io.snw.magicfurnace.factions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Factions2x implements FactionsHook {

	@Override
	public boolean isFactionMember(Player player, Location loc) {

		return false;
	}

	@Override
	public String getVersion() {
		return "2.X";
	}
}
