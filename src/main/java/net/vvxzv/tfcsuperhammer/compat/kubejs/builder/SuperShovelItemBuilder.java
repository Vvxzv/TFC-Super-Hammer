package net.vvxzv.tfcsuperhammer.compat.kubejs.builder;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.vvxzv.tfcsuperhammer.common.item.SuperHammerItem;
import net.vvxzv.tfcsuperhammer.common.item.SuperShovelItem;

@SuppressWarnings("unused")
public class SuperShovelItemBuilder extends HandheldItemBuilder {
    public SuperShovelItemBuilder(ResourceLocation i) {
        super(i, 1.25f, -3.1f);
    }

    @Override
    public Item createObject() {
        return new SuperShovelItem(
                this.toolTier,
                ToolItem.calculateVanillaAttackDamage(this.attackDamageBaseline, this.toolTier),
                this.speedBaseline,
                new Item.Properties()
        );
    }
}
