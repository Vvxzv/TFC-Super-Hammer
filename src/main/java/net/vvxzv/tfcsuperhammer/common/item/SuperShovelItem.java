package net.vvxzv.tfcsuperhammer.common.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.vvxzv.tfcsuperhammer.Config;

public class SuperShovelItem extends AbstractSuperToolItem<SuperShovelItem.ShovelMode> {
    private static final String MODE_TAG = "super_shovel_mode";

    public SuperShovelItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_SHOVEL, properties);
    }

    private boolean isGiantModeEnabled() {
        return Config.enableSuperShovelGiantMode;
    }

    @Override
    protected ShovelMode switchMode(ItemStack stack) {
        ShovelMode currentMode = this.getCurrentMode(stack);
        ShovelMode newMode = switch (currentMode) {
            case DEFAULT_5x1 -> ShovelMode.SQUARE_3x3;
            case SQUARE_3x3 -> isGiantModeEnabled()? ShovelMode.GIANT_5x5: ShovelMode.DEFAULT_5x1;
            case GIANT_5x5 -> ShovelMode.DEFAULT_5x1;
        };
        this.setCurrentMode(stack, newMode);

        return newMode;
    }

    @Override
    protected ShovelMode getCurrentMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(MODE_TAG)) {
            try {
                ShovelMode mode = ShovelMode.values()[tag.getInt(MODE_TAG)];
                if (mode == ShovelMode.GIANT_5x5 && !isGiantModeEnabled()) {
                    this.setCurrentMode(stack, ShovelMode.DEFAULT_5x1);
                    return ShovelMode.DEFAULT_5x1;
                }
                return mode;
            } catch (ArrayIndexOutOfBoundsException e) {
                this.setCurrentMode(stack, ShovelMode.DEFAULT_5x1);
                return ShovelMode.DEFAULT_5x1;
            }
        }
        this.setCurrentMode(stack, ShovelMode.DEFAULT_5x1);
        return ShovelMode.DEFAULT_5x1;
    }

    @Override
    protected void setCurrentMode(ItemStack stack, ShovelMode mode) {
        stack.getOrCreateTag().putInt(MODE_TAG, mode.ordinal());
    }

    @Override
    protected String getModeTranslateKey(ShovelMode mode) {
        return switch (mode) {
            case DEFAULT_5x1 -> "supershovel.mode.5x1";
            case SQUARE_3x3 -> "supershovel.mode.3x3";
            case GIANT_5x5 -> "supershovel.mode.5x5";
        };
    }

    @Override
    protected int[] getMineBlockOffsets(ServerPlayer player,Direction face, ShovelMode mode) {
        Direction playerFace = player.getDirection();
        return switch (face) {
            case UP, DOWN -> switch (mode) {
                case DEFAULT_5x1 -> switch (playerFace) {
                    case NORTH, SOUTH -> new int[]{-2, 0, 0, 2, 0, 0};
                    case EAST, WEST -> new int[]{0, 0, -2, 0, 0, 2};
                    default -> new int[]{0, 0, 0, 0, 0, 0};
                };
                case SQUARE_3x3 -> new int[]{-1, 0, -1, 1, 0, 1};
                case GIANT_5x5 -> new int[]{-2, 0, -2, 2, 0, 2};
            };
            case EAST, WEST -> switch (mode) {
                case DEFAULT_5x1 -> new int[]{0, 0, -2, 0, 0, 2};
                case SQUARE_3x3 -> new int[]{0, -1, -1, 0, 1, 1};
                case GIANT_5x5 -> new int[]{0, -2, -2, 0, 2, 2};
            };
            case NORTH, SOUTH -> switch (mode) {
                case DEFAULT_5x1 -> new int[]{-2, 0, 0, 2, 0, 0};
                case SQUARE_3x3 -> new int[]{-1, -1, 0, 1, 1, 0};
                case GIANT_5x5 -> new int[]{-2, -2, 0, 2, 2, 0};
            };
        };
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }

    public enum ShovelMode {
        DEFAULT_5x1,
        SQUARE_3x3,
        GIANT_5x5
    }
}
