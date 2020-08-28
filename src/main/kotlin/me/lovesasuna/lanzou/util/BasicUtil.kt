package me.lovesasuna.lanzou.util;

import java.io.File;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:21
 **/
public class BasicUtil {
    private static final Pattern pattern = Pattern.compile("\\d+");

    public static int extractInt(String string, int defaultValue) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            builder.append(matcher.group());
        }
        if (builder.toString().isEmpty()) {
            return defaultValue;
        } else {
            return Integer.parseInt(builder.toString());
        }
    }

    public static File getLocation(Class<?> c) {
        if (System.getProperty("os.name").contains("Windows")) {
            return Paths.get(c.getProtectionDomain().getCodeSource().getLocation().getPath().replaceFirst("/", "")).getParent().toFile();
        } else {
            return Paths.get(c.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent().toFile();
        }
    }

    public static File getLocation(String fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append(getLocation(BasicUtil.class).getPath()).append(File.separator).append(fileName);
        return new File(builder.toString());
    }

    public static <T> void runAnyTimes(int times, T t, Consumer<T> consumer) {
        for (int i = 0; i < times; i++) {
            consumer.accept(t);
        }
    }
}
