package net.vvxzv.tfcsuperhammer.compat.kubejs.builder;

import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.common.TFCTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public abstract class LevelTierToolBuilder extends ItemBuilder {
    public transient LevelTier toolTier;
    public transient float attackDamageBaseline;
    public transient float speedBaseline;

    public LevelTierToolBuilder(ResourceLocation id, float d, float s) {
        super(id);
        this.toolTier = TFCTiers.COPPER;
        this.attackDamageBaseline = d;
        this.speedBaseline = s;
        this.parentModel(KubeAssetGenerator.HANDHELD_ITEM_MODEL);
        this.unstackable();
    }

    @Info("String name, ResourceLocation tag, int level, int uses, float speed, float damage, int enchantmentValue")
    public LevelTierToolBuilder tier(String name, ResourceLocation tag, int level, int uses, float speed, float damage, int enchantmentValue) throws InvocationTargetException, IllegalAccessException {
        Method create = ObfuscationReflectionHelper.findMethod(TFCTiers.class, "create", String.class, TagKey.class, int.class, int.class, float.class, float.class, int.class);
        if(!create.isAccessible()) {
            create.setAccessible(true);
        }
        this.toolTier = (LevelTier) create.invoke(
                null,
                name, BlockTags.create(tag), level, uses, speed, damage, enchantmentValue
        );
        return this;
    }

    @Info("Sets the base attack damage of the tool. Different tools have different baselines.\n\nFor example, a sword has a baseline of 3, while an axe has a baseline of 6.\n\nThe actual damage is the sum of the baseline and the attackDamageBonus from tier.\n")
    public LevelTierToolBuilder attackDamageBaseline(float f) {
        this.attackDamageBaseline = f;
        return this;
    }

    @Info("Sets the base attack speed of the tool. Different tools have different baselines.\n\nFor example, a sword has a baseline of -2.4, while an axe has a baseline of -3.1.\n\nThe actual speed is the sum of the baseline and the speed from tier + 4 (bare hand).\n")
    public LevelTierToolBuilder speedBaseline(float f) {
        this.speedBaseline = f;
        return this;
    }
}
