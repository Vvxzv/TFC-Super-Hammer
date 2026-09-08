package net.vvxzv.tfcsuperhammer;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.stringtemplate.v4.ST;

import java.util.List;

@Mod.EventBusSubscriber(modid = TFCSuperHammer.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ENABLE_SUPER_HAMMER_GIANT_MODE = BUILDER.comment(" ", "Whether to enable 5x5 giant mode of the super hammer.", "是否开启超级锤子的5x5巨型模式").define("enableSuperHammerGiantMode", false);

    private static final ForgeConfigSpec.BooleanValue ENABLE_SUPER_SHOVEL_GIANT_MODE = BUILDER.comment(" ", "Whether to enable 5x5 giant mode of the super shovel.", "是否开启超级铲子的5x5巨型模式").define("enableSuperShovelGiantMode", false);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SUPER_TOOL_BLOCK_BLACKLIST = BUILDER.comment(" ", "The blacklist of blocks destroyed by the super tool. (Block ID, or block tag starting with #)", "超级工具破坏方块的黑名单。（方块id，或以#开头的方块标签）", "Example: [\"#tfc:loose_rocks\", \"tfc:groundcover/clam\", \"tfc:groundcover/mollusk\", \"tfc:groundcover/sea_urchin\", \"tfc:groundcover/mussel\"]").defineListAllowEmpty("superToolBlockBlacklist", List.of(), str -> str instanceof String);

    static final ForgeConfigSpec SPEC = BUILDER.build();

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
