package ca.jrvs.apps.stockquote.dao;


import ca.jrvs.apps.stockquote.model.Position;
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

public class PositionDao implements CrudDao<Position, String> {

  private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);
  private final Connection c;

  public PositionDao(Connection c) {
    this.c = c;
  }

  @Override
  public Position save(Position entity) throws IllegalArgumentException {

    String checkQuoteSql = "SELECT * FROM quote WHERE symbol = ?";
    double stockPrice;

    try (PreparedStatement checkStmt = c.prepareStatement(checkQuoteSql)) {
      checkStmt.setString(1, entity.getTicker());
      ResultSet rs = checkStmt.executeQuery();

      if (!rs.next()) {
        logger.error("Stock symbol does not exist in Quote table: " + entity.getTicker());
        return null;
      } else {
        stockPrice = rs.getDouble("price");
      }
    } catch (SQLException e) {
      throw new IllegalArgumentException("Error checking Quote table", e);
    }

    String sql =
        "INSERT INTO position (symbol, number_of_shares, value_paid)"
            + " VALUES (?, ?, ?)"
            + "ON CONFLICT (symbol) DO UPDATE SET number_of_shares = ?, value_paid = ?";

    double valuePaid = entity.getNumOfShares() * stockPrice;

    try (PreparedStatement stmt = c.prepareStatement(sql)) {
      stmt.setString(1, entity.getTicker());
      stmt.setInt(2, entity.getNumOfShares());
      stmt.setDouble(3, valuePaid);

      stmt.setInt(4, entity.getNumOfShares());
      stmt.setDouble(5, valuePaid);

      stmt.executeUpdate();
      logger.info("\nPosition: {} has been saved successfully!", entity.getTicker());

      return entity;
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public Optional<Position> findById(String s) throws IllegalArgumentException {
    String sql = "SELECT * FROM position WHERE symbol = ?";
    Position position = new Position();
    try (PreparedStatement stmt = c.prepareStatement(sql)) {
      stmt.setString(1, s);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(rs.getInt("number_of_shares"));
        position.setValuePaid(rs.getDouble("value_paid"));

        logger.info("\nPosition found: {}", position);
        return Optional.of(position);
      }
      logger.info("\nPosition NOT found !");
      return Optional.empty();
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public Iterable<Position> findAll() {
    String sql = "SELECT * FROM position";
    List<Position> positions = new ArrayList<>();

    try (PreparedStatement stmt = c.prepareStatement(sql)) {
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {

        while (rs.next()) {
          Position position = new Position();
          position.setTicker(rs.getString("symbol"));
          position.setNumOfShares(rs.getInt("number_of_shares"));
          position.setValuePaid(rs.getDouble("value_paid"));
          positions.add(position);
        }
      } else {
        logger.info("\nPosition NOT found");
      }
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }

    positions.forEach(position -> logger.info("Position: {}", position));
    return positions;
  }

  @Override
  public void deleteById(String s) throws IllegalArgumentException {
    String sql = "DELETE FROM position WHERE symbol=?";
    try (PreparedStatement stmt = c.prepareStatement(sql)) {
      stmt.setString(1, s);
      int affected = stmt.executeUpdate();
      if (affected > 0) {
        logger.info("\nPosition deleted successfully!");
      } else {
        logger.info("\nPosition does not exist!");
      }
    } catch (SQLException e) {
      throw new IllegalArgumentException("Error deleting Position", e);
    }
  }

  @Override
  public void deleteAll() {
    String sql = "DELETE FROM position";
    try (Statement stmt = c.createStatement()) {
      stmt.executeUpdate(sql);
      logger.info("\nAll positions have been deleted successfully!");
    } catch (SQLException e) {
      logger.info("\nError deleting all positions !");
      throw new IllegalArgumentException("Error deleting all positions", e);
    }
  }
}
