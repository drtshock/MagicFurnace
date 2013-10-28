package io.snw.magicfurnace.factions;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Factions1694 implements FactionsHook {

    @Override
    public boolean isFactionMember(Player player, Location loc) {
        FPlayer fp = FPlayers.i.get(player);
        String f1 = fp.getFaction().getTag();
        String f2 = Board.getFactionAt(new FLocation(loc)).getTag();
        return f1.equalsIgnoreCase(f2);
    }

    @Override
    public boolean isWilderness(Location loc) {
        return Board.getFactionAt(new FLocation(loc)).getTag().equalsIgnoreCase("wilderness");
    }

    @Override
    public String getVersion() {
        return "1.6.9.4";
    }
}
