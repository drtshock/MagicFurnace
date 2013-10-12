MagicFurnace
=============

By drtshock.

Description
=====
Smelt a configurable material in a furnace to send fake blocks to other players. 

Configuration
=====
 Range
 This is how many blocks out from the furnace will be sent as fake.
range: 12


 Material
 This is the material used for the fake blocks.
 Default is STONE for normal worlds, NETHERRACK for nether, and ENDER_STONE for end worlds.
 If a world is of a different type then it will default back to normal.
material:
  normal: STONE
  nether: NETHERRACK
  end: ENDER_STONE
  
  
Hooked Plugins
====
Factions - Don't send fake blocks to players if their faction owns the land.
