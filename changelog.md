Entities
---
@a
- Fully configurable spawn biomes (biome categories are also supported; can be done like so: #desert)
- Re-mixed most entity sounds

Wildfire
- Now uses the Minecraft Dungeons texture
- New variant method
- No longer has a chance to spawn from Blaze Spawners
- No longer drops Blaze Rods
- Chance to drop Molten Shard increased to 20% + 10% for each Looting level
- Change how additional Blaze spawns are handled
- Blaze groups will spawn slightly more spread out
- Fix dropping helmets that didn't match variant

Glutton
- Renamed from Hunger
- New variant method (will only take the variant of its spawn block)
- Variants will now be named differently
- Adjusted knockback
- Is now less aggressive to other entities (except other Gluttons) and will only attack if threatened
- Added config option for stealing item enchantments
- Added config option for adjusting overenchantment cap
- Can no longer be moved when burrowed
- Reduce volume of sounds when burrowed
- Fix items affected by mods able to be infinitely levelled up
- Fix health bugs

Barnacle
- Renamed from Kraken
- No longer affects player's oxygen levels, instead just periodically bites down
- Will now clamp down on players after a period of time which attacks rapidly
- Fix inconsistent and sometimes broken attacks
- Fix possible crash on spawn

Copper Golem
- Added Copper Golem
- Will randomly push copper buttons in its vicinity at random intervals
- Will oxidize over time, causing it to move slower until it freezes when fully oxidized
- Can be waxed with honeycomb and deoxidized with an axe or lightning strike
- Can be healed with copper ingots akin to Iron Golem

Blocks
---
Copper Button
- Oxidizes over time; can be waxed and scraped like normal copper
- Press time depends on oxidation level, i.e. unoxidized has a press time of 10 ticks (1/2 second, 2x as fast as a stone button), while a fully oxidized button will have a press time of 40 ticks (2 whole seconds, 10 ticks longer than wooden buttons)

Items
---
Wildfire Helmet
- Now uses the Minecraft Dungeons texture
- Item texture now adheres to Wildfire variant config option
- Soul variant appears in creative tabs if config is enabled
- Can now alter durability timer in config
- Fix being able to apply mending to the helmet
- Fix possible missing recipe
- Fix rendering bug with baby models

Wildfire Shield
- Now supports banner customization
- Fix model rendering for blocking
- Fix shield consuming no durability
- Fix recipe crash when using FastSuite
- Fix lang entries for shields with banners
- Fix incorrect repair items

Molten Shard
- Renamed from Shattered Blaze Piece
- New texture
- Can now be used to craft 4 Blaze Powder at once

Void Heart, Barnacle Tooth, & Wildfire Shield Part
- New texture

Patchouli Book
- Now has a crafting recipe (lava bucket + book for now)
- Config option to give book on first login

Misc
---
- Now requires Architectury API
- Config now utilizes JSON formatting and has an in-game screen (old configs are incompatible); requires Cloth Config
- Fix incompatibility with Netherite Plus
- Xaeros Minimap support
- Updated Patchouli entries