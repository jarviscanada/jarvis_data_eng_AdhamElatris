package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.model.Position;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionService {

  private static final Logger logger = LoggerFactory.getLogger(PositionService.class);
  private final PositionDao dao;

  public PositionService(PositionDao dao) {
    this.dao = dao;
  }

  /**
   * Processes a buy order and updates the database accordingly
   *
   * @param ticker
   * @param numberOfShares //* @param price
   * @return The position in our database after processing the buy
   */
  public Position buy(String ticker, int numberOfShares) {
    if (ticker == null || ticker.isBlank() || numberOfShares <= 0) {
      throw new IllegalArgumentException("\nInvalid input");
    }

    // Suppress INFO logs
    ch.qos.logback.classic.Logger daoLogger =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(PositionDao.class);
    ch.qos.logback.classic.Level originalLevel = daoLogger.getLevel();
    daoLogger.setLevel(ch.qos.logback.classic.Level.WARN); // Keep WARN/ERROR logs but suppress INFO

    if (!dao.tickerExistsInQuoteTable(ticker)) {
      daoLogger.setLevel(originalLevel);
      logger.error("\nSymbol not found in quote table");
      throw new IllegalArgumentException("Stock symbol does not exist in Quote table: " + ticker);
    }

    Optional<Position> optionalPosition = dao.findById(ticker);

    Position position;

    if (optionalPosition.isPresent()) {
      position = optionalPosition.get();
      position.setNumOfShares(position.getNumOfShares() + numberOfShares);
    } else {
      logger.info("\nNo existing position found for ticker: " + ticker + ", creating a new one.");
      position = new Position();
      position.setTicker(ticker);
      position.setNumOfShares(numberOfShares);
    }

    dao.save(position);

    Optional<Position> newPortfolio = dao.findById(ticker);
    daoLogger.setLevel(originalLevel); // Restore log level
    logger.info("\nYou have successfully bought " + numberOfShares + " shares of " + ticker);
    logger.info("\nNew updated portfolio: " + newPortfolio);

    return position;
  }

  /**
   * Sells all shares of the given ticker symbol
   *
   * @param ticker
   */
  public boolean sell(String ticker) {
    if (ticker == null || ticker.isBlank()) {
      throw new IllegalArgumentException("Invalid ticker");
    }

    Optional<Position> position = dao.findById(ticker);
    if (position.isPresent()) {
      dao.deleteById(ticker);
      logger.info("\nSold stock " + ticker + " successfully!");
      return true;
    } else {
      logger.error("\nError selling " + ticker + " stock... Not found!");
      return false;
    }
  }


  public void getAllPositions() {
    System.out.println(dao.findAll());
  }

  public Optional<Position> getPosition(String ticker) {
    return dao.findById(ticker);
  }

}