package net.vvxzv.tfcsuperhammer.compat.kubejs.builder;

import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.vvxzv.tfcsuperhammer.common.item.SuperShovelItem;

@SuppressWarnings("unused")
public class SuperShovelItemBuilder extends LevelTierToolBuilder {
    public SuperShovelItemBuilder(ResourceLocation i) {
        super(i, 1.25f, -3.1f);
    }

    @Override
    public Item createObject() {
        return new SuperShovelItem(this.toolTier, new Item.Properties().attributes(ToolItem.productAttributes(this.toolTier, this.attackDamageBaseline, this.speedBaseline)));
    }
}
