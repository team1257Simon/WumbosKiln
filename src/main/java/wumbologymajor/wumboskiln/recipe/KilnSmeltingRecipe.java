package wumbologymajor.wumboskiln.recipe;

import net.minecraft.advancements.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNullElse;
import static net.minecraft.advancements.AdvancementRequirements.Strategy.*;
import static net.minecraft.advancements.AdvancementRewards.Builder.recipe;
import static net.minecraft.advancements.critereon.RecipeUnlockedTrigger.*;
import static net.minecraft.core.registries.BuiltInRegistries.ITEM;
import static net.minecraft.world.item.crafting.CookingBookCategory.*;
import static net.minecraft.world.item.crafting.RecipeManager.*;
import static wumbologymajor.wumboskiln.WumbosKiln.*;
import static wumbologymajor.wumboskiln.init.WKItems.*;
import static wumbologymajor.wumboskiln.init.WKRecipes.*;
import static wumbologymajor.wumboskiln.util.DataInject.*;

public class KilnSmeltingRecipe extends AbstractCookingRecipe {
    public static final ResourceKey<RecipePropertySet> KILN_INPUT = ResourceKey.create(RecipePropertySet.TYPE_KEY, modResourceLocation("kiln_input"));
    private static final Supplier<IngredientExtractor> EXTRACTOR = KilnSmeltingRecipe::deferredExtractor;
    private final RecipeSerializer<KilnSmeltingRecipe> SERIALIZER = KILN_SMELTING_SERIALIZER.get();
    private final RecipeType<KilnSmeltingRecipe> TYPE = KILN_SMELTING.get();

    public KilnSmeltingRecipe(String group, CookingBookCategory category, Ingredient input, ItemStack result, float experience, int cookingTime) {
        super(group, category, input, result, experience, cookingTime);
    }

    public static void injectPropertySet() {
        RECIPE_PROPERTY_SETS = injectEntries(RECIPE_PROPERTY_SETS, Stream.of(Map.entry(KILN_INPUT, EXTRACTOR.get())));
    }

    private static @NotNull IngredientExtractor deferredExtractor() {
        return forSingleInput(KILN_SMELTING.get());
    }

    @Override
    public @NotNull RecipeSerializer<KilnSmeltingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<KilnSmeltingRecipe> getType() {
        return TYPE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return switch (this.category()) {
            case BLOCKS -> RecipeBookCategories.FURNACE_BLOCKS;
            case FOOD -> RecipeBookCategories.FURNACE_FOOD;
            case MISC -> RecipeBookCategories.FURNACE_MISC;
        };
    }

    @Override
    protected @NotNull Item furnaceIcon() {
        return KILN.asItem();
    }

    public static class Builder implements RecipeBuilder, Supplier<RecipeHolder<KilnSmeltingRecipe>> {

        private RecipeCategory category;
        private CookingBookCategory bookCategory;
        private Item result;
        private ItemStack stackResult; // Neo: add stack result support
        private Ingredient ingredient;
        private float experience;
        private int cookingTime;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
        private String group = "";

        @Override
        public @NotNull Item getResult() {
            return this.result;
        }

        private @NotNull AdvancementHolder buildAdvancement(@NotNull RecipeOutput output, @NotNull ResourceKey<Recipe<?>> key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", unlocked(key))
                    .rewards(recipe(key))
                    .requirements(OR);
            criteria.forEach(advancement::addCriterion);
            return advancement.build(key.location().withPrefix("recipes/" + category.getFolderName() + "/"));
        }

        private @NotNull KilnSmeltingRecipe buildRecipe() {
            return new KilnSmeltingRecipe(group, bookCategory, ingredient, stackResult, experience, cookingTime);
        }

        private @NotNull ResourceKey<Recipe<?>> createKey() {
            return ResourceKey.create(Registries.RECIPE, getModifiedResourceLocation(result));
        }

        @Override
        public void save(@NotNull RecipeOutput output, @NotNull ResourceKey<Recipe<?>> key) {
            output.accept(key, buildRecipe(), buildAdvancement(output, key));
        }

        @Contract("_ -> new")
        private static @NotNull ResourceLocation getModifiedResourceLocation(@NotNull ItemLike itemLike) {
            return modResourceLocation(ITEM.getKey(itemLike.asItem()).withPrefix("kiln_smelting/").getPath());
        }

        @Override
        public void save(@NotNull RecipeOutput recipeOutput) {
            this.save(recipeOutput, createKey());
        }

        @Override
        public RecipeHolder<KilnSmeltingRecipe> get() {
            return new RecipeHolder<>(createKey(), buildRecipe());
        }

        @Override
        public @NotNull Builder unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
            criteria.put(name, criterion);
            return this;
        }

        @Override
        public @NotNull Builder group(@Nullable String group) {
            this.group = requireNonNullElse(group, "");
            return this;
        }

        public @NotNull Builder category(RecipeCategory category) {
            this.category = category;
            return this;
        }

        public @NotNull Builder result(@NotNull ItemStack stackResult) {
            this.stackResult = stackResult;
            this.result = stackResult.getItem();
            this.bookCategory = this.result.asItem() instanceof BlockItem ? BLOCKS : MISC;
            return this;
        }

        public @NotNull Builder ingredient(Ingredient ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public @NotNull Builder experience(float experience) {
            this.experience = experience;
            return this;
        }

        public @NotNull Builder cookingTime(int cookingTime) {
            this.cookingTime = cookingTime;
            return this;
        }
    }
}
