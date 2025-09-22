package wumbologymajor.wumboskiln.data.recipes;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.jetbrains.annotations.Nullable;
import wumbologymajor.wumboskiln.WKConfig;
import wumbologymajor.wumboskiln.init.WKRecipes;
import wumbologymajor.wumboskiln.recipe.KilnSmeltingRecipe;
import wumbologymajor.wumboskiln.util.FunctionalPreparableReloadListener;
import wumbologymajor.wumboskiln.util.Predicates;

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
    private @Nullable List<RecipeHolder<KilnSmeltingRecipe>> generated;
    private boolean injected = false;

    public DynamicKilnRecipeGenerator(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    private Predicate<RecipeHolder<? extends AbstractCookingRecipe>> generationFilter() {
        return Predicates.bind(this::recipeExistsIn, concat(concat(kilnRecipes(), blastingRecipes()), smokingRecipes()).toList())
                .or(Predicates.bind(this::recipeNamedIn, WKConfig.getRecipeBlacklist()))
                .negate();
    }

    private Stream<RecipeHolder<?>> allCurrentRecipes() {
        return recipeManager.recipes.values().stream();
    }

    private Stream<RecipeHolder<SmeltingRecipe>> smeltingRecipes() {
        return recipeManager.recipes.byType(SMELTING).stream();
    }

    private Stream<RecipeHolder<SmokingRecipe>> smokingRecipes() {
        return recipeManager.recipes.byType(SMOKING).stream();
    }

    private Stream<RecipeHolder<BlastingRecipe>> blastingRecipes() {
        return recipeManager.recipes.byType(BLASTING).stream();
    }

    private Stream<RecipeHolder<KilnSmeltingRecipe>> kilnRecipes() {
        return recipeManager.recipes.byType(WKRecipes.KILN_SMELTING.get()).stream();
    }

    @Override
    public void accept(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        if(WKConfig.CONFIG.generateDynamicResources.get() && !injected) {
            if(generated == null) {
                generated = smeltingRecipes().filter(generationFilter()).map(collapse(this::convert)).toList();
            }
            recipeManager.recipes = RecipeMap.create(concat(allCurrentRecipes(), generated.stream()).toList());
            injected = true;
            LOGGER.info("Generated {} dynamic recipes", generated.size());
        }
    }

    private KilnSmeltingRecipe.Builder convertCookingRecipe(AbstractCookingRecipe recipe) {
        LOGGER.debug("Generated recipe with output {}", recipe.result().getItem());
        return new KilnSmeltingRecipe.Builder()
                .ingredient(recipe.input())
                .result(recipe.result())
                .group(recipe.group())
                .experience(recipe.experience())
                .cookingTime(recipe.cookingTime() / 2);
    }

    private KilnSmeltingRecipe.Builder convert(RecipeHolder<? extends AbstractCookingRecipe> holder) {
        return convertCookingRecipe(holder.value());
    }

    private boolean recipeExistsIn(RecipeHolder<? extends AbstractCookingRecipe> recipe, Collection<RecipeHolder<? extends AbstractCookingRecipe>> in) {
        return in.stream().anyMatch(Predicates.bind(Predicates::recipeMatches, recipe));
    }

    private boolean recipeNamedIn(RecipeHolder<? extends AbstractCookingRecipe> recipe, Collection<String> names) {
        return names.stream().anyMatch(Predicates.bind(Predicates::outputMatches, recipe));
    }

    public static class Activator extends FunctionalPreparableReloadListener<DynamicKilnRecipeGenerator> {

        private final Supplier<RecipeManager> recipeManagerSupplier;

        public Activator(AddServerReloadListenersEvent event) {
            recipeManagerSupplier = event.getServerResources()::getRecipeManager;
        }

        @Override
        protected DynamicKilnRecipeGenerator prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
            return new DynamicKilnRecipeGenerator(recipeManagerSupplier.get());
        }
    }
}
