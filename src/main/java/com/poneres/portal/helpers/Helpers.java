package com.poneres.portal.helpers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

public class Helpers {

    public static byte[] base64ToBytes(String data) {
        if (data == null) return null;
        String base64Image = data.split(",")[1];
        return parseBase64Binary(base64Image);
    }

    public static String urlEncode(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }
}
