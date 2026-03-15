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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.vvxzv.tfcsuperhammer.Config;
import net.vvxzv.tfcsuperhammer.common.registry.Data;

import java.util.List;

public class SuperHammerItem extends ToolItem implements CreativeMiningTool {
    private final Tier tier;
    private final TagKey<Block> blocks;

    public SuperHammerItem(Tier tier, Item.Properties properties) {
        super(tier,  BlockTags.MINEABLE_WITH_PICKAXE, properties);
        this.tier = tier;
        this.blocks = BlockTags.MINEABLE_WITH_PICKAXE;
    }

    private boolean isGiantModeEnabled() {
        return Config.enableSuperHammerGiantMode;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer && !level.isClientSide) {
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

    private HammerMode getCurrentMode(ItemStack stack) {
        if (stack.get(Data.SUPER_TOOL_MODE) != null) {
            try {
                HammerMode mode = HammerMode.values()[stack.get(Data.SUPER_TOOL_MODE)];
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

    private void setCurrentMode(ItemStack stack, HammerMode mode) {
        stack.set(Data.SUPER_TOOL_MODE, mode.ordinal());
    }

    private String getModeTranslateKey(HammerMode mode) {
        return switch (mode) {
            case DEFAULT_3x3 -> "superhammer.mode.3x3";
            case WIDE_5x3 -> "superhammer.mode.5x3";
            case GIANT_5x5 -> "superhammer.mode.5x5";
        };
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(this.blocks) ? (this.tier.getSpeed() * 0.4f) : 1.0F;
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
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
    }

    private void doMining(ItemStack stack, Level level, BlockPos origin, LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player) || player.isShiftKeyDown()) return;

        final HammerMode mode = this.getCurrentMode(stack);
        final Direction face = ((BlockHitResult) player.pick(20.0D, 0.0F, false)).getDirection();

        final int[] offsets = switch (face) {
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

        final BlockPos startPos = origin.offset(offsets[0], offsets[1], offsets[2]);
        final BlockPos endPos = origin.offset(offsets[3], offsets[4], offsets[5]);
        final BlockState originBlock = level.getBlockState(origin);

        for (BlockPos pos : BlockPos.betweenClosed(startPos, endPos)) {
            if (pos.equals(origin) || !isCorrectToolForDrops(stack, level.getBlockState(pos)) || !isCorrectToolForDrops(stack, originBlock)) {
                continue;
            }

            final BlockState stateAt = level.getBlockState(pos);
            final BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, stateAt, player);
            NeoForge.EVENT_BUS.post(event);
            if (event.isCanceled()) continue;

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

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext p_339594_, List<Component> pTooltipComponents, TooltipFlag p_41424_) {
        HammerMode mode = this.getCurrentMode(pStack);
        String key = this.getModeTranslateKey(mode);
        pTooltipComponents.add(
                Component.translatable("tooltip.supertool.mode")
                        .append(Component.translatable(key).withStyle(ChatFormatting.GOLD))
        );
    }

    public enum HammerMode {
        DEFAULT_3x3,
        WIDE_5x3,
        GIANT_5x5
    }
}