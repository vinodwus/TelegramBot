package com.net;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Telegram
{
   public static void main(String[] args) throws TelegramApiException {
      // Initialize Api Context
      ApiContextInitializer.init();

      // Instantiate Telegram Bots API
      TelegramBotsApi botsApi = new TelegramBotsApi();
      // Register our bot
      try {
          botsApi.registerBot(new MyAmazingBot());
      } catch (TelegramApiException e) {
          e.printStackTrace();
      }
  }
}
