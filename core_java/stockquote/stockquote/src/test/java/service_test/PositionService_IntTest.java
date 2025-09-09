package service_test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.model.Position;
import ca.jrvs.apps.stockquote.service.PositionService;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

class PositionService_IntTest {

  private PositionService positionService;
  private PositionDao positionDao;
  private Connection connection;

  @BeforeEach
  void setUp() throws SQLException {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    // Use a dedicated test database (e.g., stock_quote_test)
    dataSource.setURL("jdbc:postgresql://localhost:5432/stock_quote");
    dataSource.setUser("postgres");
    dataSource.setPassword("password");
    connection = dataSource.getConnection();

    positionDao = new PositionDao(connection);
    positionService = new PositionService(positionDao);
  }

//  @AfterEach
//  void tearDown() throws SQLException {
//    if (connection != null && !connection.isClosed()) {
//      // Clean up for test isolation
//      positionDao.deleteAll();
//      connection.close();
//    }
//  }

  @Test
  void testBuy_NewPosition() {
    String ticker = "GOOGL";
    int shares = 100;

    Position result = positionService.buy(ticker, shares);
    assertNotNull(result);
    assertEquals(ticker, result.getTicker());
    assertEquals(shares, result.getNumOfShares());

    Optional<Position> dbPosition = positionDao.findById(ticker);
    assertTrue(dbPosition.isPresent());
    assertEquals(shares, dbPosition.get().getNumOfShares());
  }

  @Test
  void testBuy_ExistingPosition() {
    String ticker = "AAPL";
    int initialShares = 50;
    int additionalShares = 50;

    // Insert initial position directly
    Position initialPosition = new Position();
    initialPosition.setTicker(ticker);
    initialPosition.setNumOfShares(initialShares);
    positionDao.save(initialPosition);

    // Perform buy operation
    Position result = positionService.buy(ticker, additionalShares);
    assertNotNull(result);
    assertEquals(ticker, result.getTicker());
    assertEquals(initialShares + additionalShares, result.getNumOfShares());

    Optional<Position> dbPosition = positionDao.findById(ticker);
    assertTrue(dbPosition.isPresent());
    assertEquals(initialShares + additionalShares, dbPosition.get().getNumOfShares());
  }


  @Test
  void testSell_existingPosition() {
    // Ensure GOOGL exists before selling
    assertTrue(positionDao.findById("GOOGL").isPresent());

    // Act
    positionService.sell("GOOGL");

    // Assert
    assertFalse(positionDao.findById("GOOGL").isPresent());
  }

  @Test
  void testSell_nonExistingPosition() {
    // Act
    positionService.sell("AAPL");

    // Assert (AAPL should not have been in the DB, so nothing happens)
    assertFalse(positionDao.findById("AAPL").isPresent());
  }
}
