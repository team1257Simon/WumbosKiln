package wumbologymajor.wumboskiln.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import wumbologymajor.wumboskiln.block.KilnBlock;
import wumbologymajor.wumboskiln.block.entity.KilnBlockEntity;

import java.util.function.Supplier;

import static net.minecraft.core.registries.Registries.*;
import static net.neoforged.neoforge.registries.DeferredRegister.create;
import static net.neoforged.neoforge.registries.DeferredRegister.createBlocks;
import static wumbologymajor.wumboskiln.WumbosKiln.*;

public class WKBlocks {
    public static final DeferredRegister.Blocks BLOCKS = createBlocks(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = create(BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredBlock<KilnBlock> KILN = BLOCKS.registerBlock("kiln", KilnBlock::new);
    public static final Supplier<BlockEntityType<KilnBlockEntity>> KILN_ENTITY = BLOCK_ENTITIES.register("kiln", WKBlocks::createKilnBlockEntityType);
    @Contract(" -> new")
    private static @NotNull BlockEntityType<KilnBlockEntity> createKilnBlockEntityType() {
        return new BlockEntityType<>(KilnBlockEntity::new, false, KILN.get());
    }
}
