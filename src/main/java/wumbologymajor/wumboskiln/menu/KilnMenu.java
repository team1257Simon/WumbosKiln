package wumbologymajor.wumboskiln.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.crafting.RecipeType;
import wumbologymajor.wumboskiln.recipe.KilnSmeltingRecipe;
import wumbologymajor.wumboskiln.annotation.NonNullAPI;

import static net.minecraft.world.inventory.RecipeBookType.*;
import static wumbologymajor.wumboskiln.init.WKMenus.*;
import static wumbologymajor.wumboskiln.init.WKRecipes.*;
import static wumbologymajor.wumboskiln.recipe.KilnSmeltingRecipe.KILN_INPUT;

@NonNullAPI
public class KilnMenu extends AbstractFurnaceMenu {
    public static final RecipeType<KilnSmeltingRecipe> RECIPE_TYPE = KILN_SMELTING.get();

    static {
        KilnSmeltingRecipe.injectPropertySet();
    }

    public KilnMenu(int containerId, Inventory playerInventory) {
        super(KILN_MENU.get(), RECIPE_TYPE, KILN_INPUT, FURNACE, containerId, playerInventory);
    }

    public KilnMenu(int containerId, Inventory playerInventory, Container kilnContainer, ContainerData kilnData) {
        super(KILN_MENU.get(), RECIPE_TYPE, KILN_INPUT, FURNACE, containerId, playerInventory, kilnContainer, kilnData);
    }
}
