package com.github.rutledgepaulv.util;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class StreamUtils {

    public static <T> Stream<T> fromIterator(Iterator<T> iter) {
        Iterable<T> iterable = () -> iter;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

}
