package io.snw.magicfurnace.factions;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Factions2x implements FactionsHook {

    @Override
    public boolean isFactionMember(Player player, Location loc) {
        UPlayer p = UPlayer.get(player);
        String f1 = p.getFactionName();
        String f2 = BoardColls.get().getFactionAt(PS.valueOf(loc)).getName();
        return f1.equalsIgnoreCase(f2);
    }

    @Override
    public boolean isWilderness(Location loc) {
        return FactionColls.get().getForUniverse(loc.getWorld().getName()).getNone().isDefault();
    }

    @Override
    public String getVersion() {
        return "2.X";
    }
}
