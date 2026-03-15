package net.vvxzv.tfcsuperhammer.compat.kubejs.builder;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.vvxzv.tfcsuperhammer.common.item.SuperHammerItem;

@SuppressWarnings("unused")
public class SuperHammerItemBuilder extends HandheldItemBuilder {
    public SuperHammerItemBuilder(ResourceLocation i) {
        super(i, 1.5f, -3.2f);
    }

    @Override
    public Item createObject() {
        return new SuperHammerItem(
                this.toolTier,
                ToolItem.calculateVanillaAttackDamage(this.attackDamageBaseline, this.toolTier),
                this.speedBaseline,
                new Item.Properties()
        );
    }
}
