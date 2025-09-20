package wumbologymajor.wumboskiln.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Functional {
    private Functional(){}

    @Contract(pure = true)
    public static <T, U> @NotNull Function<T, U> supplyChain(@NotNull Function<? super T, ? extends Supplier<? extends U>> converter) {
        return converter.andThen(Supplier::get)::apply;
    }

    @Contract(pure = true)
    public static <T, U> @NotNull Predicate<T> bind(BiPredicate<? super T, ? super U> pred, U u) {
        return t -> pred.test(t, u);
    }
}
