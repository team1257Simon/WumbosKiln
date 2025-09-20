package wumbologymajor.wumboskiln.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

@SuppressWarnings("unused")
public final class Functional {
    private Functional(){}

    @Contract(pure = true)
    public static <T, U> @NotNull Function<T, U> supplyChain(@NotNull Function<? super T, ? extends Supplier<? extends U>> converter) {
        return converter.andThen(Supplier::get)::apply;
    }

    @Contract(pure = true)
    public static <T, U> @NotNull Predicate<T> bind(@NotNull BiPredicate<? super T, ? super U> pred, U u) {
        return t -> pred.test(t, u);
    }

    @Contract(pure = true)
    public static <T, U, V> @NotNull Function<T, V> bind(@NotNull BiFunction<? super T, ? super U, ? extends V> fn, U u) {
        return t -> fn.apply(t, u);
    }

    @Contract(pure = true)
    public static <T, U> @NotNull Supplier<U> bind(@NotNull Function<? super T, ? extends U> fn, T t) {
        return () -> fn.apply(t);
    }
}
