package com.net;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyAmazingBot extends TelegramLongPollingBot {
   
   public static String extractLink( String message )
   {
      String[] words = message.split( "\\s+" );
      for ( String word : words )
      {
         if ( word.startsWith( "http" ) || word.startsWith( "https" ) )
         {
            return word;
         }
      }
      return "";
   }
   

   public static String replaceLink(String message, String newLink) {
       String[] words = message.split("\\s+");
       for (int i = 0; i < words.length; i++) {
           String word = words[i];
           if (word.startsWith("http") || word.startsWith("https")) {
               words[i] = newLink;
           }
       }
       return String.join(" ", words);
   }
   
      public void onUpdateReceived(Update update) {
         //  String tag = "?tag=vinodwus-21";
          // We check if the update has a message and the message has text
          if (update.hasMessage() && update.getMessage().hasText()) {
              // Set variables
              String message_text = update.getMessage().getText();
              String link = extractLink(message_text);
              String decodeAmazonShortenURL = AmazonShortenURLDecoder.decodeAmazonShortenURL( link );
              String shortUrl = AmazonUrlShortener.getShortUrl( decodeAmazonShortenURL );
              String updated = replaceLink( message_text, shortUrl );
              Long chat_id = update.getMessage().getChatId();

              SendMessage message = new SendMessage(); // Create a message object object
                    message.setChatId(chat_id.toString());
                    message.setText(updated);
              try {
                  execute(message); // Sending our message object to user
              } catch (TelegramApiException e) {
                  e.printStackTrace();
              }
          }
      }

      public String getBotUsername() {
          // Return bot username
          // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
          return "testingvinodwusbot";
      }

      @Override
      public String getBotToken() {
          // Return bot token from BotFather
          return "6295391205:AAFUPKaed4Joiott4P0UKUaZeve3IG6lcxM";
      }
      
}
