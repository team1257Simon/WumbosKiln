package wumbologymajor.wumboskiln.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;
import wumbologymajor.wumboskiln.recipe.KilnSmeltingRecipe;

import static net.minecraft.world.inventory.RecipeBookType.*;
import static wumbologymajor.wumboskiln.init.WKMenus.*;
import static wumbologymajor.wumboskiln.init.WKRecipes.*;

public class KilnMenu extends AbstractFurnaceMenu {

    public static final RecipeType<KilnSmeltingRecipe> RECIPE_TYPE = KILN_SMELTING.get();

    public KilnMenu(int containerId, Inventory playerInventory) {
        super(KILN_MENU.get(), RECIPE_TYPE, RecipePropertySet.FURNACE_INPUT, FURNACE, containerId, playerInventory);
    }
    public KilnMenu(int containerId, Inventory playerInventory, Container kilnContainer, ContainerData kilnData) {
        super(KILN_MENU.get(), RECIPE_TYPE, RecipePropertySet.FURNACE_INPUT, FURNACE, containerId, playerInventory, kilnContainer, kilnData);
    }
}
