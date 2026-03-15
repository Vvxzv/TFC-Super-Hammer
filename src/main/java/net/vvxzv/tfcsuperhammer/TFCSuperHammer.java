package net.vvxzv.tfcsuperhammer;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.tfcsuperhammer.common.registry.CreativeTab;
import net.vvxzv.tfcsuperhammer.common.registry.Items;

@Mod(TFCSuperHammer.MODID)
public class TFCSuperHammer {
    public static final String MODID = "tfcsuperhammer";

    @SuppressWarnings("removal")
    public TFCSuperHammer() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Items.ITEMS.register(modEventBus);
        CreativeTab.CREATIVE_MODE_TAB.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
