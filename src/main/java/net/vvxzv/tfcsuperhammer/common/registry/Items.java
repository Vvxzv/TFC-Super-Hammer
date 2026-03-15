package net.vvxzv.tfcsuperhammer.common.registry;

import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfcsuperhammer.TFCSuperHammer;
import net.vvxzv.tfcsuperhammer.common.item.SuperHammerItem;
import net.vvxzv.tfcsuperhammer.common.item.SuperShovelItem;

import java.util.Map;

@SuppressWarnings("unused")
public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFCSuperHammer.MODID);

    public static final Map<Metal.Default, RegistryObject<Item>> SUPER_HAMMERS = Helpers.mapOfKeys(
            Metal.Default.class,
            Metal.ItemType.PICKAXE::has,
            metal -> ITEMS.register(
                    "metal/superhammer/" + metal.getSerializedName(),
                    () -> new SuperHammerItem(
                            metal.toolTier(),
                            ToolItem.calculateVanillaAttackDamage(1.5f, metal.toolTier()),
                            -3.2f,
                            new Item.Properties().rarity(metal.getRarity())
                    )
            )
    );

    public static final Map<Metal.Default, RegistryObject<Item>> SUPER_HAMMERS_HEAD = Helpers.mapOfKeys(
            Metal.Default.class,
            Metal.ItemType.PICKAXE::has,
            metal -> ITEMS.register(
                    "metal/superhammer_head/" + metal.getSerializedName(),
                    () -> new Item(new Item.Properties())
            )
    );

    public static final Map<Metal.Default, RegistryObject<Item>> SUPER_SHOVELS = Helpers.mapOfKeys(
            Metal.Default.class,
            Metal.ItemType.SHOVEL::has,
            metal -> ITEMS.register(
                    "metal/supershovel/" + metal.getSerializedName(),
                    () -> new SuperShovelItem(
                            metal.toolTier(),
                            ToolItem.calculateVanillaAttackDamage(1.25f, metal.toolTier()),
                            -3.1f,
                            new Item.Properties().rarity(metal.getRarity())
                    )
            )
    );

    public static final Map<Metal.Default, RegistryObject<Item>> SUPER_SHOVELS_HEAD = Helpers.mapOfKeys(
            Metal.Default.class,
            Metal.ItemType.SHOVEL::has,
            metal -> ITEMS.register(
                    "metal/supershovel_head/" + metal.getSerializedName(),
                    () -> new Item(new Item.Properties())
            )
    );
}
