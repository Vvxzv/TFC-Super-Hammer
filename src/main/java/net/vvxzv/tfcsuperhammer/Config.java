package net.vvxzv.tfcsuperhammer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TFCSuperHammer.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_SUPER_HAMMER_GIANT_MODE = BUILDER.comment(" ").comment("Whether to enable 5x5 giant mode of the super hammer.").comment("是否开启超级锤子的5x5巨型模式").define("enableSuperHammerGiantMode", false);

    private static final ModConfigSpec.BooleanValue ENABLE_SUPER_SHOVEL_GIANT_MODE = BUILDER.comment(" ").comment("Whether to enable 5x5 giant mode of the super shovel.").comment("是否开启超级铲子的5x5巨型模式").define("enableSuperShovelGiantMode", false);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> SUPER_TOOL_BLOCK_BLACKLIST = BUILDER.comment(" ", "The blacklist of blocks destroyed by the super tool. (Block ID, or block tag starting with #)", "超级工具破坏方块的黑名单。（方块id，或以#开头的方块标签）", "Example: [\"#c:stones/loose\", \"tfc:groundcover/clam\", \"tfc:groundcover/mollusk\", \"tfc:groundcover/sea_urchin\", \"tfc:groundcover/mussel\"]").defineListAllowEmpty("superToolBlockBlacklist", List.of(), str -> str instanceof String);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableSuperHammerGiantMode;
    public static boolean enableSuperShovelGiantMode;
    public static List<? extends String> superToolBlockBlacklist;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableSuperHammerGiantMode = ENABLE_SUPER_HAMMER_GIANT_MODE.get();
        enableSuperShovelGiantMode = ENABLE_SUPER_SHOVEL_GIANT_MODE.get();
        superToolBlockBlacklist = SUPER_TOOL_BLOCK_BLACKLIST.get();
    }
}
