package com.net;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class AmazonUrlShortener
{


   private static final String ENDPOINT = "https://www.amazon.in/associates/sitestripe/getShortUrl?longUrl=";
   //private static final String ENDPOINT = "webservices.amazon.com";
   private static final String market = "&marketplaceId=44571";
   private static final String tag = "&tag=vinodkumar016-21";

   public static void main( String[] args )
   {
      String amz = "https://www.amazon.in/gp/product/B07XKCFV5V/ref=ewc_pr_img_1?smid=A2AL6IVND0I91F&psc=1";
     
      amz+=tag;
     
      
      String shortUrl = getShortUrl( amz );
   }

   public static String getShortUrl( String longUrl )
   {
      String url = null;
      try
      {
         //http://amazonspot.net/apps/URLShortner/bitly.php
         String encodedUrl = URLEncoder.encode(longUrl, "UTF-8");
         String apiUrl = "http://hybridapps.net/apps/URLShortner/bitly.php?ref=Bk105-Bitly-AmazonURL-v0&url="+encodedUrl+"&r=0.36771334148083423";
         CloseableHttpClient client = HttpClients.createDefault();
         HttpGet httpPost = new HttpGet(apiUrl);
         httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
         httpPost.addHeader("Accept-Encoding", "gzip, deflate");
         httpPost.addHeader("Accept-Language", "en-US,en;q=0.9");
         httpPost.addHeader("Cache-Control", "max-age=0");
         httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");
       //httpPost.addHeader("x-requested-with", "XMLHttpRequest");
   
         CloseableHttpResponse response = client.execute(httpPost);
         HttpEntity responseEntity = response.getEntity();
           
         if (responseEntity != null) {
             String responseString = EntityUtils.toString(responseEntity);
             JSONObject responseJson = new JSONObject(responseString);
            // String shortenedUrl = responseJson.getString("bitlydata");
             JSONObject jsonObject = responseJson.getJSONObject( "bitlydata" );
             JSONObject object = jsonObject.getJSONObject( "data" );
             Object object2 = object.get( "url" );
             url = object2.toString();
         } else {
             System.out.println("Failed to shorten URL");
         }

         response.close();
         client.close();
      }
      catch ( IOException e )
      {
         e.printStackTrace();
      }

      return url;
   }

}
