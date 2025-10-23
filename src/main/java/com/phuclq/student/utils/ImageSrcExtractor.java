package com.phuclq.student.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class ImageSrcExtractor {
    public static String getUrlImageFormHtml(String htmlContent) {

        // Parse HTML content using JSoup
        Document document = Jsoup.parse(htmlContent);

        // Select the 'img' element
        Element imgElement = document.select("img").first();

        // Get the value of the 'src' attribute
        if (imgElement != null) {
            System.out.println("Image URL: " + imgElement.attr("src"));

            return imgElement.attr("src");
        } else {
            System.out.println("No 'img' element found");
            return null;
        }
    }
}
