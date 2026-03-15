package net.vvxzv.tfcsuperhammer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TFCSuperHammer.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_SUPER_HAMMER_GIANT_MODE = BUILDER.comment(" ").comment("Whether to enable 5x5 giant mode of the super hammer.").comment("是否开启超级锤子的5x5巨型模式").define("enableSuperHammerGiantMode", false);

    private static final ModConfigSpec.BooleanValue ENABLE_SUPER_SHOVEL_GIANT_MODE = BUILDER.comment(" ").comment("Whether to enable 5x5 giant mode of the super shovel.").comment("是否开启超级铲子的5x5巨型模式").define("enableSuperShovelGiantMode", false);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableSuperHammerGiantMode;
    public static boolean enableSuperShovelGiantMode;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableSuperHammerGiantMode = ENABLE_SUPER_HAMMER_GIANT_MODE.get();
        enableSuperShovelGiantMode = ENABLE_SUPER_SHOVEL_GIANT_MODE.get();
    }
}
