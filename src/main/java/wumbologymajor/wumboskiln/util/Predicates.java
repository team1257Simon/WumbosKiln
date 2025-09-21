package wumbologymajor.wumboskiln.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import org.jetbrains.annotations.Contract;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class Predicates {
    private Predicates(){}

    @Contract(pure = true, value = "_,_->new")
    public static <T, U> Predicate<T> bind(BiPredicate<? super T, ? super U> pred, U u) {
        return new BoundBiPredicate<>(pred, u);
    }

    public static boolean validLocationString(Object o) {
        return o instanceof String s && s.matches("([a-z0-9_.-]*)(:[a-z0-9_.-/]*)?");
    }

    public static boolean recipeMatches(RecipeHolder<? extends SingleItemRecipe> a, RecipeHolder<? extends SingleItemRecipe> b) {
        return a.equals(b) || (a.value().result().getItem().equals(b.value().result().getItem()) && a.value().input().equals(b.value().input()));
    }

    public static boolean outputMatches(String name, RecipeHolder<? extends SingleItemRecipe> recipe) {
        String realName = ResourceLocation.parse(name).toString();
        return recipe.value().result().getItem().toString().equals(realName) || recipe.id().location().toString().equals(realName);
    }

    private record BoundBiPredicate<T, U>(BiPredicate<? super T, ? super U> pred, U u) implements Predicate<T> {
        @Override
        public boolean test(T t) {
            return pred.test(t, u);
        }
    }
}
