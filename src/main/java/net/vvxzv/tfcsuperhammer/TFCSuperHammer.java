package net.vvxzv.tfcsuperhammer;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.vvxzv.tfcsuperhammer.common.registry.CreativeTab;
import net.vvxzv.tfcsuperhammer.common.registry.Data;
import net.vvxzv.tfcsuperhammer.common.registry.Items;

@Mod(TFCSuperHammer.MODID)
public class TFCSuperHammer {
    public static final String MODID = "tfcsuperhammer";
    public TFCSuperHammer(IEventBus modEventBus, ModContainer modContainer) {
        Items.ITEMS.register(modEventBus);
        Data.DATA_COMPONENTS.register(modEventBus);
        CreativeTab.CREATIVE_MODE_TAB.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
