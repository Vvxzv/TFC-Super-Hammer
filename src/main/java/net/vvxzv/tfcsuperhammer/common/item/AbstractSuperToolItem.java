package net.vvxzv.tfcsuperhammer.common.item;

import net.dries007.tfc.common.items.CreativeMiningTool;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.event.level.BlockEvent;
import net.vvxzv.tfcsuperhammer.Config;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSuperToolItem<MODE> extends ToolItem implements CreativeMiningTool {
    protected final TagKey<Block> blocks;

    protected AbstractSuperToolItem(Tier tier, float attackDamage, float attackSpeed, TagKey<Block> mineableBlocks, Properties properties) {
        super(tier, attackDamage, attackSpeed, mineableBlocks, properties);
        this.blocks = mineableBlocks;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(level.isClientSide) {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }

        if (player instanceof ServerPlayer serverPlayer && serverPlayer.isShiftKeyDown()) {
            MODE newMode = this.switchMode(stack);
            String key = this.getModeTranslateKey(newMode);
            serverPlayer.displayClientMessage(
                    Component.translatable("supertool.mode")
                            .append(Component.translatable(key).withStyle(ChatFormatting.GOLD)),
                    true
            );

            serverPlayer.getCooldowns().addCooldown(stack.getItem(), 10);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }

        return super.use(level, player, hand);
    }

    @SuppressWarnings("removal")
    protected boolean isInBlacklist(BlockState state) {
        List<? extends String> strings = Config.superToolBlockBlacklist;
        List<Block> block = new ArrayList<>();
        List<TagKey<Block>> tagKey = new ArrayList<>();
        for (String string : strings) {
            if(string.startsWith("#")) {
                String str = string.replace("#", "");
                tagKey.add(BlockTags.create(new ResourceLocation(str)));
            } else {
                block.add(BuiltInRegistries.BLOCK.get(new ResourceLocation(string)));
            }
        }

        for (Block b: block) {
            if(state.is(b)) {
                return true;
            }
        }

        for (TagKey<Block> tag: tagKey) {
            if(state.is(tag)) {
                return true;
            }
        }

        return false;
    }

    protected abstract MODE getCurrentMode(ItemStack stack);

    protected abstract void setCurrentMode(ItemStack stack, MODE mode);

    protected abstract MODE switchMode(ItemStack stack);

    protected abstract String getModeTranslateKey(MODE mode);

    protected abstract int[] getMineBlockOffsets(ServerPlayer player, Direction face, MODE mode);

    protected void dropResourcesAndBreak(BlockState state, BlockPos pos, Player player, ItemStack stack, Level level) {
        if (!player.isCreative()) {
            Block.dropResources(state, level, pos, state.hasBlockEntity() ? level.getBlockEntity(pos) : null, player, player.getMainHandItem());
            stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
        }
    }

    protected void doMining(ItemStack stack, Level level, BlockPos origin, LivingEntity entity) {
        if(this.isInBlacklist(level.getBlockState(origin))) return;
        if (!(entity instanceof ServerPlayer player) || player.isShiftKeyDown()) return;

        MODE mode = this.getCurrentMode(stack);
        Direction face = ((BlockHitResult) player.pick(20.0D, 0.0F, false)).getDirection();

        int[] offsets = this.getMineBlockOffsets(player, face, mode);

        BlockPos startPos = origin.offset(offsets[0], offsets[1], offsets[2]);
        BlockPos endPos = origin.offset(offsets[3], offsets[4], offsets[5]);
        BlockState originBlock = level.getBlockState(origin);

        for (BlockPos pos : BlockPos.betweenClosed(startPos, endPos)) {
            if (pos.equals(origin) || !isCorrectToolForDrops(stack, level.getBlockState(pos)) || !isCorrectToolForDrops(stack, originBlock)) {
                continue;
            }

            BlockState stateAt = level.getBlockState(pos);

            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, stateAt, player);
            if (MinecraftForge.EVENT_BUS.post(event)) continue;

            if (stateAt.hasProperty(BlockStateProperties.LAYERS)) {
                int layers = stateAt.getValue(BlockStateProperties.LAYERS);
                level.setBlock(pos, layers > 1 ? stateAt.setValue(BlockStateProperties.LAYERS, layers - 1) : Blocks.AIR.defaultBlockState(), 3);
                this.dropResourcesAndBreak(stateAt, pos, player, stack, level);
            } else {
                this.dropResourcesAndBreak(stateAt, pos, player, stack, level);
                level.destroyBlock(pos, false, player);
            }

            if (stack.getDamageValue() >= stack.getMaxDamage()) break;
        }
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack pStack, @NotNull BlockState pState) {
        return pState.is(this.blocks) ? (this.speed * 0.4f) : 1.0f;
    }

    @Override
    public void mineBlockInCreative(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Player player) {
        this.doMining(stack, level, pos, player);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos origin, @NotNull LivingEntity entity) {
        this.doMining(stack, level, origin, entity);
        return super.mineBlock(stack, level, state, origin, entity);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        MODE mode = this.getCurrentMode(pStack);
        String key = this.getModeTranslateKey(mode);
        pTooltipComponents.add(Component.translatable("tooltip.supertool.single_block_mode").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("tooltip.supertool.change_mode").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(
                Component.translatable("tooltip.supertool.mode")
                        .append(Component.translatable(key).withStyle(ChatFormatting.GOLD))
        );
    }
}
