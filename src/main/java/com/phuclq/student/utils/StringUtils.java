package com.phuclq.student.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class StringUtils extends org.springframework.util.StringUtils {

    /**
     * codau.
     */
    private static final char[] VIET_CHARS = {'à', 'á', 'ả', 'ã', 'ạ', 'ă', 'ằ', 'ắ', 'ẳ', 'ẵ', 'ặ', 'â',
            'ầ', 'ấ', 'ẩ', 'ẫ', 'ậ', 'À', 'Á', 'Ả', 'Ã', 'Ạ', 'Ă', 'Ằ', 'Ắ', 'Ẳ', 'Ẵ', 'Ặ', 'Â', 'Ầ', 'Ấ',
            'Ẩ', 'Ẫ', 'Ậ', 'è', 'é', 'ẻ', 'ẽ', 'ẹ', 'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ', 'È', 'É', 'Ẻ', 'Ẽ', 'Ẹ',
            'Ê', 'Ề', 'Ế', 'Ể', 'Ễ', 'Ệ', 'ì', 'í', 'ỉ', 'ĩ', 'ị', 'Ì', 'Í', 'Ỉ', 'Ĩ', 'Ị', 'ò', 'ó', 'ỏ',
            'õ', 'ọ', 'ô', 'ồ', 'ố', 'ổ', 'ỗ', 'ộ', 'ơ', 'ờ', 'ớ', 'ở', 'ỡ', 'ợ', 'Ò', 'Ó', 'Ỏ', 'Õ', 'Ọ',
            'Ô', 'Ồ', 'Ố', 'Ổ', 'Ỗ', 'Ộ', 'Ơ', 'Ờ', 'Ớ', 'Ở', 'Ỡ', 'Ợ', 'ù', 'ú', 'ủ', 'ũ', 'ụ', 'ư', 'ừ',
            'ứ', 'ử', 'ữ', 'ự', 'Ù', 'Ú', 'Ủ', 'Ũ', 'Ụ', 'ỳ', 'ý', 'ỷ', 'ỹ', 'ỵ', 'Ỳ', 'Ý', 'Ỷ', 'Ỹ', 'Ỵ',
            'đ', 'Đ', 'Ư', 'Ừ', 'Ử', 'Ữ', 'Ứ', 'Ự'};
    /**
     * khongdau.
     */
    private static final char[] NORMAL_CHARS = {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
            'a', 'a', 'a', 'a', 'a', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
            'A', 'A', 'A', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'E', 'E', 'E', 'E', 'E',
            'E', 'E', 'E', 'E', 'E', 'E', 'i', 'i', 'i', 'i', 'i', 'I', 'I', 'I', 'I', 'I', 'o', 'o', 'o',
            'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'O', 'O', 'O', 'O', 'O',
            'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'u', 'u', 'u', 'u', 'u', 'u', 'u',
            'u', 'u', 'u', 'u', 'U', 'U', 'U', 'U', 'U', 'y', 'y', 'y', 'y', 'y', 'Y', 'Y', 'Y', 'Y', 'Y',
            'd', 'D', 'U', 'U', 'U', 'U', 'U', 'U'};
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static boolean isStringNotNullAndHasValue(String value) {
        return isStringNotNull(value) && isStringHasValue(value);
    }

    public static boolean isStringNotNull(String value) {
        return value != null;
    }

    public static boolean isStringHasValue(String value) {
        return !value.isEmpty();
    }

    public static boolean isEmpty(String str) {
        return (str == null || "".equals(str.trim()));
    }

    public static String getSearchableString(String input) {
        return removeAccents(input);
    }


    public static String getSearchableStringUrl(String input, int exit) {
        if (Objects.isNull(input)) return null;
        input = input.replace(" ", "-");
        input = input.replace("?", "");
        if (exit != 0) {
            exit = exit + 1;
            input = input + "-" + exit;
        }
        return removeAccents(input);
    }

    public static String getSearchableStringUrlExit(String input) {
        if (Objects.isNull(input)) return null;
        input = input.replace(" ", "-");
        return removeAccents(input);
    }

    public static String removeAccents(String input) {
        if (isEmpty(input)) {
            return "";
        }

        input = input.trim();
        for (int i = 0; i < VIET_CHARS.length; i++) {
            input = input.replace(VIET_CHARS[i], NORMAL_CHARS[i]);
        }

        return input;
    }

    public static String escapeStringForMySQL(String s) {
        return s.replace("\\", "\\\\").replace("\b", "\\b").replace("\n", "\\n").replace("\r", "\\r")
                .replace("\t", "\\t").replace("\\x1A", "\\Z").replace("\\x00", "\\0").replace("'", "\\'")
                .replace("\"", "\\\"");
    }

    public static String escapeWildcardsForMySqlLikeSearch(String s) {
        return escapeStringForMySQL(s).replace("%", "\\%").replace("_", "\\_");
    }

    public static String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isEquals(CharSequence string1, CharSequence string2) {
        if (string1 == null && string2 == null) {
            return true;
        }
        if (string1 != null) {
            return string1.equals(string2);
        }

        return string2.equals(string1);
    }

    public static String convertMultiSpaces(String text, String convertTo) {
        if (text == null) {
            return null;
        }

        return text.replaceAll("\\s+", convertTo);
    }

    public static String convertLineBreaks(String text, String convertTo) {
        if (text == null) {
            return null;
        }

        return text.replaceAll("\n", convertTo).replaceAll("\r", convertTo);
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return OBJECT_MAPPER.writeValueAsString(object);
    }
}
