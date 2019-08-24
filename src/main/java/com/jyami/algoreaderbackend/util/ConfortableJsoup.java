package com.jyami.algoreaderbackend.util;

import com.jyami.algoreaderbackend.exception.UnTrackedIOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class ConfortableJsoup {

    public static Long getProblemNumFromElement(Element element) {
        return Long.parseLong(element.text());
    }

    public static Document getConnection(String url) {
        try {
            return Jsoup.connect(url).timeout(10 * 1000).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnTrackedIOException();
        }
    }

    public static Document getConnectionWith404(String url) {
        try {
            return Jsoup.connect(url).timeout(10 * 1000).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertTime(String time) {
        String[] split = time.replaceAll(" ", "").split("[년월일시분초]");

        StringBuilder stringBuilder = new StringBuilder();
        for (String string : split) {
            stringBuilder.append(timeConvertTwoChar(string));
        }
        return stringBuilder.toString().substring(2, 12);
    }

    private static String timeConvertTwoChar(String timeUnit) {
        return String.format("%02d", Integer.valueOf(timeUnit));
    }
}
