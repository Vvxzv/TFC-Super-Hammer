package net.vvxzv.tfcsuperhammer.common.registry;

import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcsuperhammer.TFCSuperHammer;
import net.vvxzv.tfcsuperhammer.common.item.SuperHammerItem;
import net.vvxzv.tfcsuperhammer.common.item.SuperShovelItem;

import java.util.Map;

@SuppressWarnings("unused")
public class Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TFCSuperHammer.MODID);

    public static final Map<Metal, DeferredItem<Item>> SUPER_HAMMERS = Helpers.mapOf(
            Metal.class,
            Metal.ItemType.PICKAXE::has,
            metal -> ITEMS.register(
                    "metal/superhammer/" + metal.getSerializedName(),
                    () -> new SuperHammerItem(
                            metal.toolTier(),
                            tool(metal, 1.5f, -3.2F)
                    )
            )
    );

    public static final Map<Metal, DeferredItem<Item>> SUPER_HAMMERS_HEAD = Helpers.mapOf(
            Metal.class,
            Metal.ItemType.PICKAXE::has,
            metal -> ITEMS.register(
                    "metal/superhammer_head/" + metal.getSerializedName(),
                    () -> new Item(new Item.Properties())
            )
    );

    public static final Map<Metal, DeferredItem<Item>> SUPER_SHOVELS = Helpers.mapOf(
            Metal.class,
            Metal.ItemType.SHOVEL::has,
            metal -> ITEMS.register(
                    "metal/supershovel/" + metal.getSerializedName(),
                    () -> new SuperShovelItem(
                            metal.toolTier(),
                            tool(metal, 1.25f, -3.1f)
                    )
            )
    );

    public static final Map<Metal, DeferredItem<Item>> SUPER_SHOVELS_HEAD = Helpers.mapOf(
            Metal.class,
            Metal.ItemType.SHOVEL::has,
            metal -> ITEMS.register(
                    "metal/supershovel_head/" + metal.getSerializedName(),
                    () -> new Item(new Item.Properties())
            )
    );

    private static Item.Properties tool(RegistryMetal metal, float attackDamageFactor, float attackSpeed) {
        return new Item.Properties().rarity(metal.rarity()).attributes(ToolItem.productAttributes(metal.toolTier(), attackDamageFactor, attackSpeed));
    }
}
