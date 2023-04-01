package com.net;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AmazonShortenURLDecoder
{
   
   private static final String AMAZON_ASSOCIATE_TAG = "vinodwus-21";
   private static final String AMAZON_ACCESS_KEY_ID = "AKIAJLHCKZ6OKB3NOXCQ";
   private static final String AMAZON_SECRET_KEY = "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";

   private static final String AMAZON_ENDPOINT = "webservices.amazon.com";
   private static final String AMAZON_SERVICE = "AWSECommerceService";
   private static final String AMAZON_VERSION = "2011-08-01";

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
             int maxRedirects = 3; // maximum number of redirects to follow
             int responseCode;
             
             
             while (true) {
                URL url = new URL(finalUrl);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setInstanceFollowRedirects(true);
                c.connect();
                
                int response = c.getResponseCode();
                if (response == HttpURLConnection.HTTP_MOVED_TEMP || response == HttpURLConnection.HTTP_MOVED_PERM || response == HttpURLConnection.HTTP_SEE_OTHER) {
                   finalUrl = c.getHeaderField("Location");
                    System.out.println("Redirected to: " + finalUrl);
                } else {
                    String rr = c.getURL().toString();
                    System.out.println("Final URL: " + rr);
                    break;
                }
            }
             
             
             
             
             
             
            // redirect(apiUrl);
             for ( int i = 0; i < maxRedirects; i++ )
             {
                URL url = new URL( finalUrl );
                connection = ( HttpURLConnection ) url.openConnection();
                connection.setInstanceFollowRedirects( false ); // don't automatically follow redirects
                responseCode = connection.getResponseCode();

                if ( responseCode >= 300 && responseCode < 400 )
                {
                   // this is a redirect response
                   String newUrl = connection.getHeaderField( "Location" );
                   if ( newUrl == null )
                   {
                      throw new RuntimeException( "Missing Location header in redirect response" );
                   }
                   finalUrl = new URL( url, newUrl ).toString();
                }
                else
                {
                   connection = ( HttpURLConnection ) new URL( finalUrl ).openConnection();
                   connection.setInstanceFollowRedirects( true );
                   connection.connect();
                   System.out.println( "connected url: " + connection.getURL() );
                   InputStream is = connection.getInputStream();
                   System.out.println( "redirected url: " + connection.getURL() );
                   InputStream iss = connection.getInputStream();
                   String locationHeader = connection.getHeaderField( "Location" );
                   if ( locationHeader != null )
                   {
                      finalUrl = locationHeader;
                   }
                }

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
       
       
       
           public static void redirect(String redirectUrl) {
              // String redirectUrl = "http://example.com/redirect"; // starting URL

               HttpClient httpClient = HttpClientBuilder.create().build();
               HttpGet httpGet = new HttpGet(redirectUrl);
               httpGet.setHeader("User-Agent", "Mozilla/5.0"); // set a User-Agent header to avoid HTTP 403 errors

               try {
                   HttpResponse response = httpClient.execute(httpGet);

                   while (response.getStatusLine().getStatusCode() == 301 || response.getStatusLine().getStatusCode() == 302) {
                       // this is a redirect response
                       String newUrl = response.getFirstHeader("Location").getValue();
                       httpGet = new HttpGet(newUrl);
                       response = httpClient.execute(httpGet);
                   }

                   // this is the final response
                   String finalUrl = httpGet.getURI().toString();
                   System.out.println("Final URL: " + finalUrl);

                   // optional: get the response body as a string
                   String responseBody = EntityUtils.toString(response.getEntity());
                   System.out.println("Response body: " + responseBody);

               } catch (Exception e) {
                   System.out.println("Error: " + e.getMessage());
               } finally {
                   // close the HttpClient instance
                   httpClient.getConnectionManager().shutdown();
               }
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

   private static String lookupItem(String asin) {
       String timestamp = getTimestamp();
       String signature = getSignature(asin, timestamp);

       String requestURL = "https://" + AMAZON_ENDPOINT + "/onca/xml?" +
               "Service=" + AMAZON_SERVICE +
               "&Version=" + AMAZON_VERSION +
               "&Operation=ItemLookup" +
               "&ItemId=" + asin +
               "&AssociateTag=" + AMAZON_ASSOCIATE_TAG +
               "&AWSAccessKeyId=" + AMAZON_ACCESS_KEY_ID +
               "&Timestamp=" + timestamp +
               "&Signature=" + signature;

       try {
           URL url = new URL(requestURL);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           InputStream inputStream = connection.getInputStream();
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           DocumentBuilder builder = factory.newDocumentBuilder();
           Document document = builder.parse(inputStream);
           document.getDocumentElement().normalize();
           return document.getDocumentElement().getTextContent();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
   
   public static String getTimestamp() {
      Calendar calendar = Calendar.getInstance();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
      dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      return dateFormat.format(calendar.getTime());
  }

  public static String getSignature(String asin, String timestamp) {
      String message = "GET\n" + AMAZON_ENDPOINT + "\n/onca/xml\n" +
              "AWSAccessKeyId=" + AMAZON_ACCESS_KEY_ID +
              "&AssociateTag=" + AMAZON_ASSOCIATE_TAG +
              "&ItemId=" + asin +
              "&Operation=ItemLookup" +
              "&Service=" + AMAZON_SERVICE +
              "&Timestamp=" + timestamp +
              "&Version=" + AMAZON_VERSION;

      String signature = null;
      try {
          byte[] secretKeyBytes = AMAZON_SECRET_KEY.getBytes("UTF-8");
          SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
          Mac mac = Mac.getInstance("HmacSHA256");
          mac.init(secretKeySpec);
          byte[] signatureBytes = mac.doFinal(message.getBytes("UTF-8"));
          signature = Base64.getEncoder().encodeToString(signatureBytes);
          signature = signature.replace("+", "%2B");
          signature = signature.replace("=", "%3D");
          signature = signature.replace("/", "%2F");
      } catch (NoSuchAlgorithmException | InvalidKeyException | IOException e) {
          e.printStackTrace();
      }
      return signature;
  }
  
  private static String getASINFromItemLookupResponse(String response) {
     try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         InputSource inputSource = new InputSource(new StringReader(response));
         Document doc = dBuilder.parse(inputSource);
         doc.getDocumentElement().normalize();

         NodeList nodeList = doc.getElementsByTagName("ASIN");
         if (nodeList.getLength() > 0) {
             Node asinNode = nodeList.item(0);
             return asinNode.getTextContent();
         }
     } catch (ParserConfigurationException | SAXException | IOException e) {
         e.printStackTrace();
     }
     return null;
 }



}
