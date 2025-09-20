package wumbologymajor.wumboskiln.data.recipes;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static net.minecraft.data.recipes.RecipeCategory.*;
import static net.minecraft.tags.ItemTags.SMELTS_TO_GLASS;
import static net.minecraft.world.item.Items.NETHER_BRICK;
import static net.minecraft.world.level.block.Blocks.*;
import static wumbologymajor.wumboskiln.recipe.KilnSmeltingRecipe.Builder;

public class KilnRecipeProvider extends RecipeProvider {

    private record VanillaSmeltingDescriptor(ItemLike input, ItemLike output, RecipeCategory category, String advancement, float exp, int vanillaTime) {
        public VanillaSmeltingDescriptor(ItemLike input, ItemLike output, RecipeCategory category, String advancement, float exp) {
            this(input, output, category, advancement, exp, 200);
        }
        public VanillaSmeltingDescriptor(ItemLike input, ItemLike output, RecipeCategory category, String advancement) {
            this(input, output, category, advancement, 0.1F);
        }
        public @NotNull Builder toRecipeBuilder(@NotNull Function<ItemLike, Criterion<?>> haveItemCriterion) {
                return new Builder().ingredient(Ingredient.of(input()))
                        .result(new ItemStack(output()))
                        .category(category())
                        .unlockedBy(advancement(), haveItemCriterion.apply(input()))
                        .experience(exp())
                        .cookingTime(vanillaTime() / 2);
        }
    }

    private static final VanillaSmeltingDescriptor[] VANILLA_SMELTS = {
            new VanillaSmeltingDescriptor(CLAY, TERRACOTTA, BUILDING_BLOCKS, "has_clay_block", 0.35F),
            new VanillaSmeltingDescriptor(NETHERRACK, NETHER_BRICK, MISC, "has_netherrack"),
            new VanillaSmeltingDescriptor(WET_SPONGE, SPONGE, BUILDING_BLOCKS, "has_wet_sponge", 0.15F),
            new VanillaSmeltingDescriptor(COBBLESTONE, STONE, BUILDING_BLOCKS, "has_cobblestone"),
            new VanillaSmeltingDescriptor(STONE, SMOOTH_STONE, BUILDING_BLOCKS, "has_stone"),
            new VanillaSmeltingDescriptor(SANDSTONE, SMOOTH_SANDSTONE, BUILDING_BLOCKS, "has_sandstone"),
            new VanillaSmeltingDescriptor(RED_SANDSTONE, SMOOTH_RED_SANDSTONE, BUILDING_BLOCKS, "has_red_sandstone"),
            new VanillaSmeltingDescriptor(QUARTZ_BLOCK, SMOOTH_QUARTZ, BUILDING_BLOCKS, "has_quartz_block"),
            new VanillaSmeltingDescriptor(STONE_BRICKS, CRACKED_STONE_BRICKS, BUILDING_BLOCKS, "has_stone_bricks"),
            new VanillaSmeltingDescriptor(COBBLED_DEEPSLATE, DEEPSLATE, BUILDING_BLOCKS, "has_cobbled_deepslate"),
            new VanillaSmeltingDescriptor(BASALT, SMOOTH_BASALT, BUILDING_BLOCKS, "has_basalt"),
            new VanillaSmeltingDescriptor(BLACK_TERRACOTTA, BLACK_GLAZED_TERRACOTTA, DECORATIONS, "has_black_terracotta"),
            new VanillaSmeltingDescriptor(BLUE_TERRACOTTA, BLUE_GLAZED_TERRACOTTA, DECORATIONS, "has_blue_terracotta"),
            new VanillaSmeltingDescriptor(BROWN_TERRACOTTA, BROWN_GLAZED_TERRACOTTA, DECORATIONS, "has_brown_terracotta"),
            new VanillaSmeltingDescriptor(CYAN_TERRACOTTA, CYAN_GLAZED_TERRACOTTA, DECORATIONS, "has_cyan_terracotta"),
            new VanillaSmeltingDescriptor(GRAY_TERRACOTTA, GRAY_GLAZED_TERRACOTTA, DECORATIONS, "has_gray_terracotta"),
            new VanillaSmeltingDescriptor(GREEN_TERRACOTTA, GREEN_GLAZED_TERRACOTTA, DECORATIONS, "has_green_terracotta"),
            new VanillaSmeltingDescriptor(LIGHT_BLUE_TERRACOTTA, LIGHT_BLUE_GLAZED_TERRACOTTA, DECORATIONS, "has_light_blue_terracotta"),
            new VanillaSmeltingDescriptor(LIGHT_GRAY_TERRACOTTA, LIGHT_GRAY_GLAZED_TERRACOTTA, DECORATIONS, "has_light_gray_terracotta"),
            new VanillaSmeltingDescriptor(LIME_TERRACOTTA, LIME_GLAZED_TERRACOTTA, DECORATIONS, "has_lime_terracotta"),
            new VanillaSmeltingDescriptor(MAGENTA_TERRACOTTA, MAGENTA_GLAZED_TERRACOTTA, DECORATIONS, "has_magenta_terracotta"),
            new VanillaSmeltingDescriptor(ORANGE_TERRACOTTA, ORANGE_GLAZED_TERRACOTTA, DECORATIONS, "has_orange_terracotta"),
            new VanillaSmeltingDescriptor(PINK_TERRACOTTA, PINK_GLAZED_TERRACOTTA, DECORATIONS, "has_pink_terracotta"),
            new VanillaSmeltingDescriptor(PURPLE_TERRACOTTA, PURPLE_GLAZED_TERRACOTTA, DECORATIONS, "has_purple_terracotta"),
            new VanillaSmeltingDescriptor(WHITE_TERRACOTTA, WHITE_GLAZED_TERRACOTTA, DECORATIONS, "has_white_terracotta"),
            new VanillaSmeltingDescriptor(YELLOW_TERRACOTTA, YELLOW_GLAZED_TERRACOTTA, DECORATIONS, "has_yellow_terracotta")
            
    };

    private @NotNull Builder ofVanillaRecipe(@NotNull VanillaSmeltingDescriptor descriptor) {
        return descriptor.toRecipeBuilder(this::has);
    }

    private void saveRecipe(@NotNull Builder builder) {
        builder.save(output);
    }

    protected KilnRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        new Builder().ingredient(tag(SMELTS_TO_GLASS))
                .result(new ItemStack(GLASS))
                .category(BUILDING_BLOCKS)
                .experience(1.0F)
                .cookingTime(100)
                .unlockedBy("has_smelts_to_glass", has(SMELTS_TO_GLASS))
                .save(output);
        Arrays.stream(VANILLA_SMELTS).map(this::ofVanillaRecipe).forEach(this::saveRecipe);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);

        }
        @Override
        public @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput output) {
            return new KilnRecipeProvider(provider, output);
        }

        @Override
        public @NotNull String getName() {
            return "Kiln Smelting Recipes";
        }
    }
}
