package net.vvxzv.tfcsuperhammer.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcsuperhammer.TFCSuperHammer;

public class Data {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TFCSuperHammer.MODID);

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> SUPER_TOOL_MODE = DATA_COMPONENTS.register("super_tool_mode", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());
}
