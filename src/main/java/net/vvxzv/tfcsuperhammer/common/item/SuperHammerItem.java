package net.vvxzv.tfcsuperhammer.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.vvxzv.tfcsuperhammer.Config;
import org.jetbrains.annotations.NotNull;

public class SuperHammerItem extends AbstractSuperToolItem<SuperHammerItem.HammerMode> {
    private static final String MODE_TAG = "super_hammer_mode";

    public SuperHammerItem(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    private boolean isGiantModeEnabled() {
        return Config.enableSuperHammerGiantMode;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(level.isClientSide) {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            HammerMode currentMode = this.getCurrentMode(stack);
            HammerMode newMode = switch (currentMode) {
                case DEFAULT_3x3 -> HammerMode.WIDE_5x3;
                case WIDE_5x3 -> isGiantModeEnabled()? HammerMode.GIANT_5x5: HammerMode.DEFAULT_3x3;
                case GIANT_5x5 -> HammerMode.DEFAULT_3x3;
            };
            setCurrentMode(stack, newMode);

            String key = this.getModeTranslateKey(newMode);
            serverPlayer.displayClientMessage(
                    Component.translatable("supertool.mode")
                            .append(Component.translatable(key).withStyle(ChatFormatting.GOLD)),
                    true
            );
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }
        return super.use(level, player, hand);
    }

    @Override
    protected HammerMode getCurrentMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(MODE_TAG)) {
            try {
                HammerMode mode = HammerMode.values()[tag.getInt(MODE_TAG)];
                if (mode == HammerMode.GIANT_5x5 && !isGiantModeEnabled()) {
                    this.setCurrentMode(stack, HammerMode.DEFAULT_3x3);
                    return HammerMode.DEFAULT_3x3;
                }
                return mode;
            } catch (ArrayIndexOutOfBoundsException e) {
                this.setCurrentMode(stack, HammerMode.DEFAULT_3x3);
                return HammerMode.DEFAULT_3x3;
            }
        }
        this.setCurrentMode(stack, HammerMode.DEFAULT_3x3);
        return HammerMode.DEFAULT_3x3;
    }

    @Override
    protected void setCurrentMode(ItemStack stack, HammerMode mode) {
        stack.getOrCreateTag().putInt(MODE_TAG, mode.ordinal());
    }

    @Override
    protected String getModeTranslateKey(HammerMode mode) {
        return switch (mode) {
            case DEFAULT_3x3 -> "superhammer.mode.3x3";
            case WIDE_5x3 -> "superhammer.mode.5x3";
            case GIANT_5x5 -> "superhammer.mode.5x5";
        };
    }

    @Override
    protected int[] getMineBlockOffsets(ServerPlayer player,Direction face, HammerMode mode) {
        return switch (face) {
            case UP, DOWN -> switch (mode) {
                case DEFAULT_3x3, WIDE_5x3 -> new int[]{-1, 0, -1, 1, 0, 1};
                case GIANT_5x5 -> new int[]{-2, 0, -2, 2, 0, 2};
            };
            case EAST, WEST -> switch (mode) {
                case DEFAULT_3x3 -> new int[]{0, -1, -1, 0, 1, 1};
                case WIDE_5x3 -> new int[]{0, -1, -2, 0, 1, 2};
                case GIANT_5x5 -> new int[]{0, -2, -2, 0, 2, 2};
            };
            case NORTH, SOUTH -> switch (mode) {
                case DEFAULT_3x3 -> new int[]{-1, -1, 0, 1, 1, 0};
                case WIDE_5x3 -> new int[]{-2, -1, 0, 2, 1, 0};
                case GIANT_5x5 -> new int[]{-2, -2, 0, 2, 2, 0};
            };
        };
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }

    public enum HammerMode {
        DEFAULT_3x3,
        WIDE_5x3,
        GIANT_5x5
    }
}