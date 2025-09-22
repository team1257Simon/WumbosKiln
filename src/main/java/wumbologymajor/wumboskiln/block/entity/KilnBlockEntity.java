package wumbologymajor.wumboskiln.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import wumbologymajor.wumboskiln.menu.KilnMenu;
import wumbologymajor.wumboskiln.annotation.NonNullAPI;

import static wumbologymajor.wumboskiln.init.WKBlocks.*;
import static wumbologymajor.wumboskiln.init.WKRecipes.*;

@NonNullAPI
public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(BlockPos pos, BlockState blockState) {
        super(KILN_ENTITY.get(), pos, blockState, KILN_SMELTING.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.kiln");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new KilnMenu(containerId, playerInventory, this, dataAccess);
    }

    @Override
    protected int getBurnDuration(FuelValues fuelValues, ItemStack stack) {
        return super.getBurnDuration(fuelValues, stack) / 2;
    }
}
