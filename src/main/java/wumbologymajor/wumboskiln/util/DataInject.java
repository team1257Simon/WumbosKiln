package wumbologymajor.wumboskiln.util;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

public final class DataInject {
    private DataInject(){}

    public static <K, V> Map<K, V> injectEntries(Map<K, V> aMap, Stream<Map.Entry<? extends K, ? extends V>> toAdd) {
        return concat(aMap.entrySet().stream(), toAdd).collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
