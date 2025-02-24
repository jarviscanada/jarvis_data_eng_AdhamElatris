package dao_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.model.Quote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class QuoteDao_Test {

  private QuoteDao quoteDao;

  @Mock
  private Connection mockConnection;
  @Mock
  private PreparedStatement mockStmt;
  @Mock
  private ResultSet mockResultSet;
  @Mock
  private Statement mockStatement;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    quoteDao = new QuoteDao(mockConnection);
  }

  @Test
  public void testSave() throws Exception {
    // Mock a Quote object instead of calling the API
    Quote mockQuote = new Quote();
    mockQuote.setTicker("AAPL");
    mockQuote.setOpen(180.5);
    mockQuote.setHigh(185.0);
    mockQuote.setLow(179.0);
    mockQuote.setPrice(182.3);
    mockQuote.setVolume(500000);
    mockQuote.setLatestTradingDay(new java.util.Date());
    mockQuote.setPreviousClose(181.5);
    mockQuote.setChange(0.8);
    mockQuote.setChangePercent("0.44%");
    mockQuote.setTimestamp(new Timestamp(System.currentTimeMillis()));

    String sql =
        "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
            + "ON CONFLICT (symbol) DO UPDATE SET "
            + "open=EXCLUDED.open, high=EXCLUDED.high, low=EXCLUDED.low, price=EXCLUDED.price, "
            + "volume=EXCLUDED.volume, latest_trading_day=EXCLUDED.latest_trading_day, "
            + "previous_close=EXCLUDED.previous_close, change=EXCLUDED.change, change_percent=EXCLUDED.change_percent";

    // Mock the behavior of Connection and PreparedStatement
    when(mockConnection.prepareStatement(sql)).thenReturn(mockStmt);
    when(mockStmt.executeUpdate()).thenReturn(1);  // Simulate successful save

    // Call the save method
    Quote savedQuote = quoteDao.save(mockQuote);

    // Verify that the SQL execution happened
    verify(mockStmt, times(1)).executeUpdate();

    // Validate the returned quote
    assertNotNull(savedQuote);
    assertEquals("AAPL", savedQuote.getTicker());
  }

  @Test
  public void testFindById() throws Exception {
    String sql = "SELECT * FROM quote WHERE symbol=?";
    when(mockConnection.prepareStatement(sql)).thenReturn(mockStmt);
    when(mockStmt.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getString("symbol")).thenReturn("AAPL");

    Optional<Quote> result = quoteDao.findById("AAPL");
    assertTrue(result.isPresent());
    assertEquals("AAPL", result.get().getTicker());
  }

  @Test
  public void testFindAll() throws Exception {
    String sql = "SELECT * FROM quote";
    when(mockConnection.prepareStatement(sql)).thenReturn(mockStmt);
    when(mockStmt.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true, true, false);
    when(mockResultSet.getString("symbol")).thenReturn("AAPL", "GOOGL");

    List<Quote> quotes = (List<Quote>) quoteDao.findAll();
    assertEquals(2, quotes.size());
    assertEquals("GOOGL", quotes.get(1).getTicker());
  }

  @Test
  public void testDeleteById() throws Exception {
    String sql = "DELETE FROM quote WHERE symbol=?";
    when(mockConnection.prepareStatement(sql)).thenReturn(mockStmt);
    when(mockStmt.executeUpdate()).thenReturn(1);

    quoteDao.deleteById("AAPL");

    verify(mockStmt).setString(1, "AAPL");
    verify(mockStmt).executeUpdate();
  }

  @Test
  public void testDeleteAll() throws Exception {
    String sql = "DELETE FROM quote";
    when(mockConnection.createStatement()).thenReturn(mockStatement);
    when(mockStatement.executeUpdate(sql)).thenReturn(1);

    quoteDao.deleteAll();

    verify(mockStatement).executeUpdate(sql);
  }
}
