# TFC Super Hammer
### 
## Mod Contents
### Tools Modes
#### Super Hammer:
1. 3x3 default mode, used for excavating a 3x3 range.
2. 5x3 horizontal scaling mode, used for excavating a 5x3 range in the horizontal direction.
3. 5x5 giant mode, used for excavating a 5x5 range. (if enabled in config)

#### Super Shovel:
1. 5x1 default mode, used for excavating a 5x1 range.
2. 3x3 square mode, used for excavating a 3x3 range.
3. 5x5 giant mode, used for excavating a 5x5 range. (if enabled in config)

### If you hold down the shift key while mining, you will only mine current one block.

## KubeJS (Custom Super Hammer, Custom Super Shovel)
```JavaScript
StartupEvents.registry('item', event => {
    event.create('steel_superhammer', 'tfcsuperhammer:superhammer')
        .tier(
            'custom_steel',                         // String name
            "minecraft:incorrect_for_diamond_tool", // ResourceLocation tag
            4,                                      // int level
            3300,                                   // int uses
            9.5,                                    // float speed
            5.75,                                   // float damage
            12                                      // int enchantmentValue
        )
        .texture('tfcsuperhammer:item/metal/superhammer/steel')

    event.create('steel_supershovel', 'tfcsuperhammer:supershovel')
        .tier(
            'custom_steel',
            "minecraft:incorrect_for_diamond_tool",
            4,
            3300,
            9.5,
            5.75,
            12
        )
        .texture('tfcsuperhammer:item/metal/supershovel/steel')
})

// name:                  level,  uses,    speed, attackDamageBonus, enchantmentValue,              tag

// "igneous_intrusive":     0,    60,       4.7,        2.0,                5,      "minecraft:incorrect_for_wooden_tool"
// "igneous_extrusive":     0,    70,       4.7,        2.0,                5,      "minecraft:incorrect_for_wooden_tool"
// "sedimentary":           0,    50,       4.0,        2.0,                5,      "minecraft:incorrect_for_wooden_tool"
// "metamorphic":           0,    55,       4.35,       2.0,                5,      "minecraft:incorrect_for_wooden_tool"
// "copper":                1,    600,      5.25,       3.25,               8,      "minecraft:incorrect_for_stone_tool"
// "bronze":                2,    1300,     7.3,        4.0,                13,     "minecraft:incorrect_for_iron_tool"
// "bismuth_bronze":        2,    1200,     6.65,       4.0,                10,     "minecraft:incorrect_for_iron_tool"
// "black_bronze":          2,    1460,     6.0,        4.25,               10,     "minecraft:incorrect_for_iron_tool"
// "wrought_iron":          3,    2200,     8.0,        4.75,               12,     "minecraft:incorrect_for_iron_tool"
// "steel":                 4,    3300,     9.5,        5.75,               12,     "minecraft:incorrect_for_diamond_tool"
// "black_steel":           5,    4200,     11.0,       7.0,                17,     "minecraft:incorrect_for_netherite_tool"
// "blue_steel":            6,    6500,     12.0,       9.0,                22,     "minecraft:incorrect_for_netherite_tool"
// "red_steel":             6,    6500,     12.0,       9.0,                22,     "minecraft:incorrect_for_netherite_tool"
```