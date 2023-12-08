package cn.com.liurz.flow.util;

import java.util.UUID;

public class UUIDUtils {
    public static String getUUID() {
        return getUUID(false, false);
    }

    public static String get32UUID() {
        return getUUID(false, true);
    }

    public static String getUUIDFromBytes(byte[] input) {
        return getUUIDFromBytes(input, false, false);
    }

    public static String getUpperUUID() {
        return getUUID(true, false);
    }

    public static String get32UpperUUID() {
        return getUUID(true, true);
    }

    public static String getUUID(boolean isUpperCase, boolean isHyphenReplaced) {
        String uuidString = UUID.randomUUID().toString();
        if (isHyphenReplaced) {
            uuidString = uuidString.replace("-", "");
        }

        if (isUpperCase) {
            uuidString = uuidString.toUpperCase();
        }

        return uuidString;
    }

    public static String getUUIDFromBytes(byte[] input, boolean isUpperCase, boolean isHyphenReplaced) {
        String uuidString = UUID.randomUUID().toString();
        if (input != null) {
            uuidString = UUID.nameUUIDFromBytes(input).toString();
        }

        if (isHyphenReplaced) {
            uuidString = uuidString.replace("-", "");
        }

        if (isUpperCase) {
            uuidString = uuidString.toUpperCase();
        }

        return uuidString;
    }
}
