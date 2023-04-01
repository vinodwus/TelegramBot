package com.net;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmazonShortenURLDecoder
{
   public static void main(String[] args) {
       String url = "https://bityl.co/HxQE";
       String decodedURL = decodeAmazonShortenURL(url);
       decodedURL = decodedURL.replaceAll("&tag.*?(?=&)", "&tag=vinodkumar016-21");
       String shortenURL = AmazonUrlShortener.getShortUrl( decodedURL );
       System.out.println( shortenURL );
   }

   public static String decodeAmazonShortenURL( String apiUrl )
   {
      HttpURLConnection connection = null;
      String finalUrl = apiUrl;
      try
      {
         connection = ( HttpURLConnection ) new URL( finalUrl ).openConnection();
         connection.setInstanceFollowRedirects( true );
         connection.connect();
         String locationHeader = connection.getHeaderField( "Location" );
         if ( locationHeader != null )
         {
            finalUrl = locationHeader;
         }

      }
      catch ( Exception e )
      {
         System.out.println( "Error: " + e.getMessage() );
      }
      finally
      {
         if ( connection != null )
         {
            connection.disconnect();
         }
      }
      finalUrl = finalUrl.replaceAll( "&?tag.*?(?=&|$)", "&tag=vinodkumar016-21" );
      return finalUrl;
   }
    

   public static String getASINFromURL(String url) {
       String asin = null;
       String itemRegex = "/([A-Z0-9]{10})(?:[/?]|$)";
       Pattern pattern = Pattern.compile(itemRegex);
       Matcher matcher = pattern.matcher(url);
       if (matcher.find()) {
           asin = matcher.group(1);
       }
       if (asin != null) {
          return asin = asin.toUpperCase();
          // String itemLookupResponse = lookupItem(asin);
          // asin = getASINFromItemLookupResponse(itemLookupResponse);
       }
       return asin;
   }

}
