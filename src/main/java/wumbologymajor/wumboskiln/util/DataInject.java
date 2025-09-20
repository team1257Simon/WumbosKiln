package wumbologymajor.wumboskiln.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

public final class DataInject {
    private DataInject(){}

    public static <K, V> Map<K, V> injectEntries(@NotNull Map<K, V> aMap, @NotNull Stream<Entry<? extends K, ? extends V>> toAdd) {
        return concat(aMap.entrySet().stream(), toAdd).collect(toUnmodifiableMap(Entry::getKey, Entry::getValue));
    }
}
