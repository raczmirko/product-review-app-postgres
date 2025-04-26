package hu.okrim.productreviewappcomplete.util;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToStringHelper {
    public static String safe(Object o) {
        return (o != null) ? o.toString() : "null";
    }

    public static String safeFormat(String format, Object... args) {
        Object[] safeArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            safeArgs[i] = (args[i] != null) ? args[i] : "null";
        }
        return String.format(format, safeArgs);
    }

    public static <T> String safeCollection(Collection<T> collection, Function<T, Object> mapper) {
        if (collection == null) {
            return "null";
        }
        return collection.stream()
                .map(item -> safe(mapper.apply(item)))
                .collect(Collectors.joining(", ", "[", "]"));
    }
}

