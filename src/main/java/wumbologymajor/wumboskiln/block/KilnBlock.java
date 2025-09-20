package wumbologymajor.wumboskiln.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wumbologymajor.wumboskiln.block.entity.KilnBlockEntity;
import wumbologymajor.wumboskiln.init.WKBlocks;

import static net.minecraft.sounds.SoundEvents.*;
import static net.minecraft.sounds.SoundSource.*;

public class KilnBlock extends AbstractFurnaceBlock {

    public static final MapCodec<KilnBlock> CODEC = simpleCodec(KilnBlock::new);

    public KilnBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<KilnBlock> codec() {
        return CODEC;
    }

    @Override
    protected void openContainer(@NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof KilnBlockEntity kilnBlockEntity) {
            player.openMenu(kilnBlockEntity);
        }
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new KilnBlockEntity(blockPos, blockState);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (blockState.getValue(LIT)) {
            double x = (double)pos.getX() + 0.5;
            double y = pos.getY();
            double z = (double)pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                level.playLocalSound(x, y, z, FURNACE_FIRE_CRACKLE, BLOCKS, 1.0F, 1.0F, false);
            }
            Direction direction = blockState.getValue(FACING);
            Direction.Axis axis = direction.getAxis();
            double baseNoise = random.nextDouble() * 0.6 - 0.3;
            x += axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52 : baseNoise;
            y += random.nextDouble() * 6.0 / 16.0;
            z += axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52 : baseNoise;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, WKBlocks.KILN_ENTITY.get());
    }
}
