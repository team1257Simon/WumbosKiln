package wumbologymajor.wumboskiln.init;

import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import wumbologymajor.wumboskiln.recipe.KilnSmeltingRecipe;

import java.util.function.Supplier;

import static net.minecraft.core.registries.Registries.*;
import static wumbologymajor.wumboskiln.WumbosKiln.*;

public class WKRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(RECIPE_TYPE, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(RECIPE_SERIALIZER, MODID);
    public static final Supplier<RecipeType<KilnSmeltingRecipe>> KILN_SMELTING = RECIPE_TYPES.register("kiln_smelting", RecipeType::simple);
    public static final Supplier<RecipeSerializer<KilnSmeltingRecipe>> KILN_SMELTING_SERIALIZER = RECIPE_SERIALIZERS.register("kiln_smelting", WKRecipes::createKilnSmeltingSerializer);

    @Contract(" -> new")
    private static @NotNull RecipeSerializer<KilnSmeltingRecipe> createKilnSmeltingSerializer() {
        return new AbstractCookingRecipe.Serializer<>(KilnSmeltingRecipe::new, 100);
    }
}
