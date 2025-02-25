package ca.jrvs.apps.stockquote.controller;

import ca.jrvs.apps.stockquote.model.Position;
import ca.jrvs.apps.stockquote.model.Quote;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {

  private final QuoteService quoteService;
  private final PositionService positionService;
  private final Scanner scanner;

  public StockQuoteController(QuoteService quoteService, PositionService positionService) {
    this.quoteService = quoteService;
    this.positionService = positionService;
    this.scanner = new Scanner(System.in);
  }

  /**
   * User interface for our application
   */
  public void initClient() {
    while (true) {
      System.out.println("\nStock Quote App Menu:");
      System.out.println("1. Get Stock Info");
      System.out.println("2. Get Position Info");
      System.out.println("3. Buy Stock");
      System.out.println("4. Sell Stock");
      System.out.println("5. View Portfolio");
      System.out.println("6. View available Quotes");
      System.out.println("7. Add a new Quote");
      System.out.println("8. Update a Quote");
      System.out.println("9. Exit");
      System.out.print("Choose an option: ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          getStockInfo();
          break;
        case "2":
          getPosition();
          break;
        case "3":
          buyStock();
          break;
        case "4":
          sellStock();
          break;
        case "5":
          viewPortfolio();
          break;
        case "6":
          viewQuotes();
          break;
        case "7":
          addNewQuote();
          break;
        case "8":
          updateAllQuotes();
          break;
        case "9":
          System.out.println("Exiting application...");
          return;
        default:
          System.out.println("Invalid option. Please try again.");
      }
    }
  }

  private void getStockInfo() {
    System.out.print("Enter stock ticker: ");
    String ticker = scanner.nextLine().toUpperCase();

    try {
      Optional<Quote> quote = quoteService.getQuoteByTicker(ticker);
      if (quote.isPresent()) {
        System.out.println("Quote found: " + quote.get());
      } else {
        System.out.println("No quote found for ticker: " + ticker);
      }
    } catch (Exception e) {
      System.out.println("Error retrieving stock information: " + e.getMessage());
    }
  }


  private void buyStock() {
    System.out.print("Enter stock ticker: ");
    String ticker = scanner.nextLine().toUpperCase();
    System.out.print("Enter number of shares to buy: ");
    try {
      int shares = Integer.parseInt(scanner.nextLine());
      positionService.buy(ticker, shares);
      System.out.println("You have successfully bought " + shares + " shares of " + ticker + "...Congratulations!");
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a valid number of shares.");
    } catch (Exception e) {
      System.out.println("Error processing buy order: " + e.getMessage());
    }
  }

  private void sellStock() {
    System.out.print("Enter stock ticker to sell: ");
    String ticker = scanner.nextLine().toUpperCase();
    try {
      boolean success = positionService.sell(ticker);
      if (success) {
        System.out.println("Successfully sold all shares of " + ticker);
      } else {
        System.out.println("No shares found for ticker: " + ticker);
      }
    } catch (Exception e) {
      System.out.println("Error processing sell order: " + e.getMessage());
    }
  }


  private void viewPortfolio() {
    System.out.println("\nYour Portfolio:");
    positionService.getAllPositions();
  }

  private void viewQuotes() {
    System.out.println("\nHere are your available Quotes:");
    quoteService.getAllQuotes();
  }

  private void addNewQuote() {
    System.out.print("Enter the new Quote ticker to add: ");
    String ticker = scanner.nextLine().toUpperCase();

    if (!ticker.equals("")) {
      System.out.println("Ticker can not be empty.");
    }
    try {
      quoteService.addNewQuote(ticker);
      System.out.println("Successfully added new Quote: " + ticker);
    } catch (Exception e) {
      System.out.println("Error adding new quote, please make sure you have a valid ticker.");
    }
  }

  private void updateAllQuotes() {
    System.out.print("Enter the Quote ticker to be updated: ");
    String ticker = scanner.nextLine().toUpperCase();
    try {
      Optional<Quote> updatedQuote = quoteService.fetchQuoteDataFromAPI(ticker);
      if (updatedQuote.isPresent()) {
        System.out.println("Successfully updated Quote: " + ticker);
      } else {
        System.out.println("Error updating quote: No data found for symbol: " + ticker);
      }
    } catch (Exception e) {
      System.out.println("Error updating quote: " + e.getMessage());
    }
  }

  private void getPosition() {
    System.out.print("Enter Position ticker: ");
    String ticker = scanner.nextLine().toUpperCase();
    try {
      Optional<Position> position = positionService.getPosition(ticker);
      if (position.isPresent()) {
        System.out.println("Position found ! " + position.get());
      } else {
        System.out.println("No position found for ticker: " + ticker);
      }
    } catch (Exception e) {
      System.out.println("Error retrieving Position information: " + e.getMessage());
    }
  }


}