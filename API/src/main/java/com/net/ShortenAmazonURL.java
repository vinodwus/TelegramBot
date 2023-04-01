package com.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ShortenAmazonURL {

    private static final String AMAZON_ACCESS_KEY_ID = "AKIAJLHCKZ6OKB3NOXCQ";
    private static final String AMAZON_SECRET_KEY = "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";
    private static final String AMAZON_ASSOCIATE_TAG = "vinodwqs-21";
    private static final String AMAZON_ENDPOINT = "webservices.amazon.com";
    private static final String AMAZON_SERVICE = "AWSECommerceService";
    private static final String AMAZON_VERSION = "2013-08-01";

    public static void main(String[] args) {
        String amazonUrl = "https://www.amazon.in/dp/B08JW3P8JB";
        String shortenedUrl = shortenURL(amazonUrl);
        System.out.println(shortenedUrl);
    }

    public static String shortenURL(String url) {
        String asin = getASINFromURL(url);
        if (asin == null) {
            return null;
        }
        String encodedUrl = encodeURL("https://www.amazon.in/dp/" + asin);
        String timestamp = AmazonShortenURLDecoder.getTimestamp();
        String signature = AmazonShortenURLDecoder.getSignature(asin, timestamp);
        String requestUrl = "https://" + AMAZON_ENDPOINT + "/onca/xml?" +
                "AWSAccessKeyId=" + AMAZON_ACCESS_KEY_ID +
                "&AssociateTag=" + AMAZON_ASSOCIATE_TAG +
                "&ItemId=" + asin +
                "&Operation=ItemLookup" +
                "&Service=" + AMAZON_SERVICE +
                "&Timestamp=" + timestamp +
                "&Signature=" + signature +
                "&Version=" + AMAZON_VERSION;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new URL(requestUrl).openStream());
            Document doc = dBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("ShortURL");
            if (nodeList.getLength() > 0) {
                Node shortUrlNode = nodeList.item(0);
                return shortUrlNode.getTextContent();
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getASINFromURL(String url) {
        int startIndex = url.indexOf("/dp/") + 4;
        int endIndex = url.indexOf("?", startIndex);
        if (endIndex == -1) {
            endIndex = url.length();
        }
        return url.substring(startIndex, endIndex);
    }
}

