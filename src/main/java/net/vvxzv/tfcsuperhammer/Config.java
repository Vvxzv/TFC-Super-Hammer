package net.vvxzv.tfcsuperhammer;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TFCSuperHammer.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ENABLE_SUPER_HAMMER_GIANT_MODE = BUILDER.comment(" ").comment("Whether to enable 5x5 giant mode of the super hammer.").comment("是否开启超级锤子的5x5巨型模式").define("enableSuperHammerGiantMode", false);

    private static final ForgeConfigSpec.BooleanValue ENABLE_SUPER_SHOVEL_GIANT_MODE = BUILDER.comment(" ").comment("Whether to enable 5x5 giant mode of the super shovel.").comment("是否开启超级铲子的5x5巨型模式").define("enableSuperShovelGiantMode", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enableSuperHammerGiantMode;
    public static boolean enableSuperShovelGiantMode;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableSuperHammerGiantMode = ENABLE_SUPER_HAMMER_GIANT_MODE.get();
        enableSuperShovelGiantMode = ENABLE_SUPER_SHOVEL_GIANT_MODE.get();
    }
}
