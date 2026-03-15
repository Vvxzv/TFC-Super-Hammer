package net.vvxzv.tfcsuperhammer.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.vvxzv.tfcsuperhammer.compat.kubejs.builder.SuperHammerItemBuilder;
import net.vvxzv.tfcsuperhammer.compat.kubejs.builder.SuperShovelItemBuilder;

public class ModKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void init(){
        RegistryInfo.ITEM.addType("tfcsuperhammer:superhammer", SuperHammerItemBuilder.class, SuperHammerItemBuilder::new);
        RegistryInfo.ITEM.addType("tfcsuperhammer:supershovel", SuperShovelItemBuilder.class, SuperShovelItemBuilder::new);
    }
}
