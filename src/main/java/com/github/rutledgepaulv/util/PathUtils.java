package com.github.rutledgepaulv.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PathUtils {
    private PathUtils(){}


    public static Stream<String> stream(String path) {
        if(path.contains(".")) {
            return Arrays.stream(path.split("\\."));
        } else {
            return Arrays.stream(new String[]{path});
        }
    }

    public static Iterator<String> iterator(String path) {
        return stream(path).iterator();
    }

    public static String combine(Iterator<String> iter) {
        Stream<String> stream = StreamUtils.fromIterator(iter);
        return stream.collect(Collectors.joining("."));
    }

}
