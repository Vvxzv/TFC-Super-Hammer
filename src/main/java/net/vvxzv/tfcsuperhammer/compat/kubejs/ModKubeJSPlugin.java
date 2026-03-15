package net.vvxzv.tfcsuperhammer.compat.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.tfcsuperhammer.TFCSuperHammer;
import net.vvxzv.tfcsuperhammer.compat.kubejs.builder.SuperHammerItemBuilder;
import net.vvxzv.tfcsuperhammer.compat.kubejs.builder.SuperShovelItemBuilder;

public class ModKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.ITEM, reg -> {
            reg.add(
                    ResourceLocation.fromNamespaceAndPath(TFCSuperHammer.MODID, "superhammer"),
                    SuperHammerItemBuilder.class,
                    SuperHammerItemBuilder::new
            );
            reg.add(
                    ResourceLocation.fromNamespaceAndPath(TFCSuperHammer.MODID, "supershovel"),
                    SuperShovelItemBuilder.class,
                    SuperShovelItemBuilder::new
            );
        });
    }
}
