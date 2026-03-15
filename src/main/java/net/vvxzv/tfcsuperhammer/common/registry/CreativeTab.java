package net.vvxzv.tfcsuperhammer.common.registry;

import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfcsuperhammer.TFCSuperHammer;

public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFCSuperHammer.MODID);

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> SUPER_HAMMER_TAB = CREATIVE_MODE_TAB.register(
            "super_hammer_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("super_hammer.tab.name"))
                    .icon(() -> new ItemStack(Items.SUPER_HAMMERS.get(Metal.Default.WROUGHT_IRON).get()))
                    .displayItems((pParameters, pOutput) -> {
                        Items.ITEMS.getEntries().forEach(item -> {
                            pOutput.accept(item.get());
                        });
                    })
                    .build()
    );

}
