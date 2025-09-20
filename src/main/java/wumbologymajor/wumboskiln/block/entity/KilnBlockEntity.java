package wumbologymajor.wumboskiln.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import wumbologymajor.wumboskiln.menu.KilnMenu;

import static wumbologymajor.wumboskiln.init.WKBlocks.*;
import static wumbologymajor.wumboskiln.init.WKRecipes.*;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(@NotNull BlockPos pos, BlockState blockState) {
        super(KILN_ENTITY.get(), pos, blockState, KILN_SMELTING.get());
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.kiln");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory) {
        return new KilnMenu(containerId, playerInventory, this, dataAccess);
    }
}
