package net.vvxzv.tfcsuperhammer.compat.kubejs.builder;

import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.vvxzv.tfcsuperhammer.common.item.SuperHammerItem;

@SuppressWarnings("unused")
public class SuperHammerItemBuilder extends LevelTierToolBuilder {
    public SuperHammerItemBuilder(ResourceLocation i) {
        super(i, 1.5f, -3.2f);
    }

    @Override
    public Item createObject() {
        return new SuperHammerItem(this.toolTier, new Item.Properties().attributes(ToolItem.productAttributes(this.toolTier, this.attackDamageBaseline, this.speedBaseline)));
    }
}
