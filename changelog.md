PLEASE NOTE: Forge servers are completely broken at the moment. Currently unable to fix, but working towards a solution. Sorry!

Entities
---
Glutton
- Fix Glutton ignoring maxEnchants and capEnchants configs
- Fix missing lang entries for capEnchants config

Copper Golem
- New model
- Reduce chances of desync
- Oxidization rate is now also affected by the randomTickSpeed gamerule... in case you wanted that...

Glare
- New model
- Can now pick up items that give off light and place them in dark areas (limited to non full blocks; can be disabled)
- Now utilizes spawn biome filter in config (will only spawn at y < 63)

Blocks
---
Copper Button
- Fix issue where buttons would get stuck if they oxidized while pressed
- Fix huge performance hit at high tick speeds

Misc
---
- Fix incompatibility with MobZ
- Some backend improvements