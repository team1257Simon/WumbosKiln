package wumbologymajor.wumboskiln.util;

import org.jetbrains.annotations.Contract;

import java.util.function.*;

@SuppressWarnings("unused")
public final class Functional {
    private Functional(){}

    @Contract(pure = true)
    public static <T, U> Function<T, U> supplyChain(Function<? super T, ? extends Supplier<? extends U>> converter) {
        return converter.andThen(Supplier::get)::apply;
    }

    @Contract(pure = true, value = "_,_->new")
    public static <T, U, V> Function<T, V> bind(BiFunction<? super T, ? super U, ? extends V> fn, U u) {
        return new BoundBiFunction<>(fn, u);
    }

    @Contract(pure = true, value = "_->new")
    public static <T, U, V> BiFunction<U, T, V> permute(BiFunction<? super T, ? super U, ? extends V> fn) {
        return new PermutedBiFunction<>(fn);
    }

    @Contract(pure = true, value = "_,_->new")
    public static <T, U> Supplier<U> bind(Function<? super T, ? extends U> fn, T t) {
        return new BoundFunction<>(fn, t);
    }

    private record BoundBiFunction<T, U, V>(BiFunction<? super T, ? super U, ? extends V> original, U u)
            implements Function<T, V> {
        @Override
        public V apply(T t) {
            return original.apply(t, u);
        }
    }

    private record BoundFunction<T, U>(Function<? super T, ? extends U> fn, T t)
        implements Supplier<U> {
        @Override
        public U get() {
            return fn.apply(t);
        }
    }

    private record PermutedBiFunction<T, U, V>(BiFunction<? super U, ? super T, ? extends V> original)
        implements BiFunction<T, U, V> {
        @Override
        public V apply(T t, U u) {
            return original().apply(u, t);
        }
    }
}
