MagicFurnace
=============

By drtshock.

Description
===
Smelt a configurable material in a furnace to send fake blocks to other players. 

Configuration
===
 <pre>
 // Auto update stuff.
check-update: true
download-update: false # This plugin does not break between minecraft versions.

// Range
// This is how many blocks out from the furnace will be sent as stone.
range: 12


// Material
// This is the material used for the fake blocks.
// Default is STONE for normal worlds, NETHERRACK for nether, and ENDER_STONE for end worlds.
// If a world is of a different type then it will default back to normal.
material:
  normal: STONE
  nether: NETHERRACK
  end: ENDER_STONE
  
// Smelter
// Which material do you wish to make the players smelt to make a magic furnace.
// Defaults to DIAMOND.
smeltme: DIAMOND

// Whether or not you want to integrate with Factions.
// This requires you to have version 2.2.x or newer of Factions (I'm not sure about older versions)
// This will make it so if a furnace is in a faction's territory, it will not send the fake blocks 
//   to the members of that faction. 
// I recommend this so people can still be in the base while it's on.
// If you don't use factions then don't worry about this. It won't matter :)
use-factions: true

// Should people be able to do this in the wilderness? 
// If you allow them to do this in the wilderness then they can create a furnace in the middle
//    of a pvp battle.
allow-in-wilderness: false
  </pre>
Hooked Plugins
==
Factions - Don't send fake blocks to players if their faction owns the land.
mcore - Factions needs it anyway.

Permissions
==
<table>
<tr>
<th>Permission</th><th>Description</th>
</tr>
<tr>
<td>magicfurnace.use</td><td>Allow use of magic furnaces</td>
</tr>
<tr>
<td>magicfurnace.notify</td><td>Notify on login if there is an update</td>
</tr>
</table>

Commands
==
No commands!

