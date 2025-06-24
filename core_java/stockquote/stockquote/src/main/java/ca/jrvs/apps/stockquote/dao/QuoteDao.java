package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.model.Quote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteDao implements CrudDao<Quote, String> {

  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private final Connection c;

  public QuoteDao(Connection c) {
    this.c = c;
  }

  @Override
  public Quote save(Quote quote) throws IllegalArgumentException {
    String sql =
        "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT (symbol) DO UPDATE SET " +
            "open=EXCLUDED.open, high=EXCLUDED.high, low=EXCLUDED.low, price=EXCLUDED.price, " +
            "volume=EXCLUDED.volume, latest_trading_day=EXCLUDED.latest_trading_day, " +
            "previous_close=EXCLUDED.previous_close, change=EXCLUDED.change, " +
            "change_percent=EXCLUDED.change_percent, timestamp=EXCLUDED.timestamp";

    try (PreparedStatement stmt = c.prepareStatement(sql)) {
      stmt.setString(1, quote.getTicker());
      stmt.setDouble(2, quote.getOpen());
      stmt.setDouble(3, quote.getHigh());
      stmt.setDouble(4, quote.getLow());
      stmt.setDouble(5, quote.getPrice());
      stmt.setInt(6, quote.getVolume());
      stmt.setDate(7, new java.sql.Date(quote.getLatestTradingDay().getTime()));
      stmt.setDouble(8, quote.getPreviousClose());
      stmt.setDouble(9, quote.getChange());
      stmt.setString(10, quote.getChangePercent());

      if (quote.getTimestamp() != null) {
        stmt.setTimestamp(11, new java.sql.Timestamp(quote.getTimestamp().getTime()));
      } else {
        stmt.setTimestamp(11, new java.sql.Timestamp(System.currentTimeMillis()));
      }

      stmt.executeUpdate();

      logger.info("\nQuote: {} has been saved successfully!", quote.getTicker());
      return quote;
    } catch (SQLException e) {
      logger.error("\nError saving quote: {}", quote.getTicker(), e);
      throw new IllegalArgumentException("Error saving quote", e);
    }
  }


  @Override
  public Optional<Quote> findById(String id) throws IllegalArgumentException {
    String sql = "SELECT * FROM quote WHERE symbol=?";
    Quote quote;
    try (PreparedStatement stmt = c.prepareStatement(sql)) {
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        quote = new Quote();
        quote.setTicker(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));
        quote.setTimestamp(rs.getTimestamp("timestamp"));

        logger.info("\nQuote found: {}", quote);
        return Optional.of(quote);
      }
      logger.error("\nQuote NOT found !");
      return Optional.empty();
    } catch (SQLException e) {
      throw new IllegalArgumentException("Error finding quote", e);
    }
  }

  @Override
  public Iterable<Quote> findAll() {
    String sql = "SELECT * FROM quote";
    List<Quote> quotes = new ArrayList<>();

    try (PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Quote quote = new Quote();
        quote.setTicker(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));
        quote.setTimestamp(rs.getTimestamp("timestamp"));
        quotes.add(quote);
      }
    } catch (SQLException e) {
      logger.error("\nQuote NOT found");
      throw new IllegalArgumentException("Error retrieving all quotes", e);
    }

    if (quotes.isEmpty()) {
      logger.info("No quotes found in the database.");
    } else {
      quotes.forEach(quote -> logger.info("Quote: {}", quote));
    }
    return quotes;
  }

  @Override
  public void deleteById(String id) throws IllegalArgumentException {
    String sql = "DELETE FROM quote WHERE symbol=?";
    try (PreparedStatement stmt = c.prepareStatement(sql)) {
      stmt.setString(1, id);
      int affected = stmt.executeUpdate();
      if (affected > 0) {
        logger.info("\nQuote deleted successfully!");
      } else {
        logger.error("\nQuote does not exist!");
      }
    } catch (SQLException e) {
      throw new IllegalArgumentException("Error deleting quote", e);
    }
  }

  @Override
  public void deleteAll() {
    String sql = "DELETE FROM quote";
    try (Statement stmt = c.createStatement()) {
      stmt.executeUpdate(sql);
      logger.info("\nAll quotes have been deleted successfully!");
    } catch (SQLException e) {
      throw new IllegalArgumentException("Error deleting all quotes", e);
    }
  }
}