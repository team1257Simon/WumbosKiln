package wumbologymajor.wumboskiln.client.gui.screen;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import wumbologymajor.wumboskiln.menu.KilnMenu;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class KilnScreen extends AbstractFurnaceScreen<KilnMenu> {
    private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
    private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.kiln");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(Items.SAND, RecipeBookCategories.FURNACE_BLOCKS)
    );

    public KilnScreen(KilnMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, FILTER_NAME, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE, TABS);
    }
}
