package net.vvxzv.tfcsuperhammer.common.item;

import net.dries007.tfc.common.items.CreativeMiningTool;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.vvxzv.tfcsuperhammer.Config;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class SuperShovelItem  extends ToolItem implements CreativeMiningTool {
    private static final String MODE_TAG = "super_shovel_mode";
    private final TagKey<Block> blocks;

    public SuperShovelItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_SHOVEL, properties);
        this.blocks = BlockTags.MINEABLE_WITH_SHOVEL;
    }

    private boolean isGiantModeEnabled() {
        return Config.enableSuperShovelGiantMode;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer && !level.isClientSide) {
            ShovelMode currentMode = this.getCurrentMode(stack);
            ShovelMode newMode = switch (currentMode) {
                case DEFAULT_5x1 -> ShovelMode.SQUARE_3x3;
                case SQUARE_3x3 -> isGiantModeEnabled()? ShovelMode.GIANT_5x5: ShovelMode.DEFAULT_5x1;
                case GIANT_5x5 -> ShovelMode.DEFAULT_5x1;
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

    private ShovelMode getCurrentMode(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(MODE_TAG)) {
            try {
                ShovelMode mode = ShovelMode.values()[stack.getTag().getInt(MODE_TAG)];
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

    private void setCurrentMode(ItemStack stack, ShovelMode mode) {
        stack.getOrCreateTag().putInt(MODE_TAG, mode.ordinal());
    }

    private String getModeTranslateKey(ShovelMode mode) {
        return switch (mode) {
            case DEFAULT_5x1 -> "supershovel.mode.5x1";
            case SQUARE_3x3 -> "supershovel.mode.3x3";
            case GIANT_5x5 -> "supershovel.mode.5x5";
        };
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(this.blocks) ? (this.speed * 0.4f) : 1.0F;
    }

    @Override
    public void mineBlockInCreative(ItemStack stack, Level level, BlockState state, BlockPos pos, Player player) {
        doMining(stack, level, pos, player);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos origin, LivingEntity entity) {
        doMining(stack, level, origin, entity);
        return super.mineBlock(stack, level, state, origin, entity);
    }

    private void dropResourcesAndBreak(BlockState state, BlockPos pos, Player player, ItemStack stack, Level level) {
        if (!player.isCreative()) {
            Block.dropResources(state, level, pos, state.hasBlockEntity() ? level.getBlockEntity(pos) : null, player, player.getMainHandItem());
            stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
        }
    }

    private void doMining(ItemStack stack, Level level, BlockPos origin, LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player) || player.isShiftKeyDown()) return;

        final ShovelMode mode = this.getCurrentMode(stack);
        final Direction face = ((BlockHitResult) player.pick(20.0D, 0.0F, false)).getDirection();
        final int[] offsets = getOffsets(player, face, mode);

        final BlockPos startPos = origin.offset(offsets[0], offsets[1], offsets[2]);
        final BlockPos endPos = origin.offset(offsets[3], offsets[4], offsets[5]);
        final BlockState originBlock = level.getBlockState(origin);

        for (BlockPos pos : BlockPos.betweenClosed(startPos, endPos)) {
            if (pos.equals(origin) || !isCorrectToolForDrops(stack, level.getBlockState(pos)) || !isCorrectToolForDrops(stack, originBlock)) {
                continue;
            }

            final BlockState stateAt = level.getBlockState(pos);
            final BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, stateAt, player);
            if (MinecraftForge.EVENT_BUS.post(event)) continue;

            if (stateAt.hasProperty(BlockStateProperties.LAYERS)) {
                int layers = stateAt.getValue(BlockStateProperties.LAYERS);
                level.setBlock(pos, layers > 1 ? stateAt.setValue(BlockStateProperties.LAYERS, layers - 1) : Blocks.AIR.defaultBlockState(), 3);
                dropResourcesAndBreak(stateAt, pos, player, stack, level);
            } else {
                dropResourcesAndBreak(stateAt, pos, player, stack, level);
                level.destroyBlock(pos, false, player);
            }

            if (stack.getDamageValue() >= stack.getMaxDamage()) break;
        }
    }

    private static int @NotNull [] getOffsets(ServerPlayer player, Direction face, ShovelMode mode) {
        final Direction playerFace = player.getDirection();
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

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        ShovelMode mode = this.getCurrentMode(pStack);
        String key = this.getModeTranslateKey(mode);
        pTooltipComponents.add(
                Component.translatable("tooltip.supertool.mode")
                        .append(Component.translatable(key).withStyle(ChatFormatting.GOLD))
        );
    }

    public enum ShovelMode {
        DEFAULT_5x1,
        SQUARE_3x3,
        GIANT_5x5
    }
}
