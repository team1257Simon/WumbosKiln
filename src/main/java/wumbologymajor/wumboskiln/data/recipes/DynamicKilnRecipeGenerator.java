package wumbologymajor.wumboskiln.data.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.jetbrains.annotations.NotNull;
import wumbologymajor.wumboskiln.WKConfig;
import wumbologymajor.wumboskiln.init.WKRecipes;
import wumbologymajor.wumboskiln.recipe.KilnSmeltingRecipe;
import wumbologymajor.wumboskiln.util.FunctionalPreparableReloadListener;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Stream.*;
import static net.minecraft.world.item.crafting.RecipeType.*;
import static wumbologymajor.wumboskiln.WumbosKiln.LOGGER;
import static wumbologymajor.wumboskiln.util.Functional.*;

public class DynamicKilnRecipeGenerator implements BiConsumer<ResourceManager, ProfilerFiller> {
    private final RecipeManager recipeManager;
    private List<RecipeHolder<KilnSmeltingRecipe>> generated;
    private boolean injected = false;

    public DynamicKilnRecipeGenerator(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    private @NotNull Predicate<RecipeHolder<? extends AbstractCookingRecipe>> generationFilter() {
        return bind(this::recipeExistsIn, concat(concat(kilnRecipes(), blastingRecipes()), smokingRecipes()).toList())
                .or(bind(this::recipeNamedIn, WKConfig.getRecipeBlacklist()))
                .negate();
    }

    private @NotNull Stream<RecipeHolder<?>> allCurrentRecipes() {
        return recipeManager.recipes.values().stream();
    }

    private @NotNull Stream<RecipeHolder<SmeltingRecipe>> smeltingRecipes() {
        return recipeManager.recipes.byType(SMELTING).stream();
    }

    private @NotNull Stream<RecipeHolder<SmokingRecipe>> smokingRecipes() {
        return recipeManager.recipes.byType(SMOKING).stream();
    }

    private @NotNull Stream<RecipeHolder<BlastingRecipe>> blastingRecipes() {
        return recipeManager.recipes.byType(BLASTING).stream();
    }

    private @NotNull Stream<RecipeHolder<KilnSmeltingRecipe>> kilnRecipes() {
        return recipeManager.recipes.byType(WKRecipes.KILN_SMELTING.get()).stream();
    }

    @Override
    public void accept(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        if(WKConfig.CONFIG.generateDynamicResources.get() && !injected) {
            if(generated == null) {
                generated = smeltingRecipes().filter(generationFilter()).map(supplyChain(this::convert)).toList();
            }
            recipeManager.recipes = RecipeMap.create(concat(allCurrentRecipes(), generated.stream()).toList());
            injected = true;
            LOGGER.info("Generated {} dynamic recipes", generated.size());
        }
    }

    private KilnSmeltingRecipe.@NotNull Builder convertCookingRecipe(@NotNull AbstractCookingRecipe recipe) {
        LOGGER.debug("Generated recipe with output {}", recipe.result().getItem());
        return new KilnSmeltingRecipe.Builder()
                .ingredient(recipe.input())
                .result(recipe.result())
                .group(recipe.group())
                .experience(recipe.experience())
                .cookingTime(recipe.cookingTime() / 2);
    }

    private KilnSmeltingRecipe.@NotNull Builder convert(@NotNull RecipeHolder<? extends AbstractCookingRecipe> holder) {
        return convertCookingRecipe(holder.value());
    }

    private boolean recipeMatches(@NotNull RecipeHolder<? extends AbstractCookingRecipe> a, RecipeHolder<? extends AbstractCookingRecipe> b) {
        return a.equals(b) || (a.value().result().getItem().equals(b.value().result().getItem()) && a.value().input().equals(b.value().input()));
    }

    private boolean recipeExistsIn(@NotNull RecipeHolder<? extends AbstractCookingRecipe> recipe, @NotNull Collection<RecipeHolder<? extends AbstractCookingRecipe>> in) {
        return in.stream().anyMatch(bind(this::recipeMatches, recipe));
    }

    private boolean outputMatches(@NotNull String name, @NotNull RecipeHolder<? extends AbstractCookingRecipe> recipe) {
        String realName = ResourceLocation.parse(name).toString();
        return recipe.value().result().getItem().toString().equals(realName) || recipe.id().location().toString().equals(realName);
    }

    private boolean recipeNamedIn(@NotNull RecipeHolder<? extends AbstractCookingRecipe> recipe, @NotNull Collection<String> names) {
        return names.stream().anyMatch(bind(this::outputMatches, recipe));
    }

    public static class Activator extends FunctionalPreparableReloadListener<DynamicKilnRecipeGenerator> {

        private final Supplier<RecipeManager> recipeManagerSupplier;

        public Activator(@NotNull AddServerReloadListenersEvent event) {
            recipeManagerSupplier = event.getServerResources()::getRecipeManager;
        }


        @Override
        protected @NotNull DynamicKilnRecipeGenerator prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
            return new DynamicKilnRecipeGenerator(recipeManagerSupplier.get());
        }
    }
}
