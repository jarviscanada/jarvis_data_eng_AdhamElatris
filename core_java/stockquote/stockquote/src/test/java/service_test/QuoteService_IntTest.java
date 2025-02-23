package service_test;

import static org.junit.jupiter.api.Assertions.*;

import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import org.junit.jupiter.api.*;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.model.Quote;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

class QuoteService_IntTest {

  private QuoteService quoteService;
  private Connection connection;

  @BeforeEach
  void setUp() throws SQLException {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    // Use a dedicated test database (e.g., stock_quote_test)
    dataSource.setURL("jdbc:postgresql://localhost:5432/stock_quote");
    dataSource.setUser("postgres");
    dataSource.setPassword("password");
    connection = dataSource.getConnection();

    QuoteDao quoteDao = new QuoteDao(connection);
    // Use the real HTTP helper (or a configured test version)
    quoteService = new QuoteService(quoteDao, new QuoteHttpHelper());
  }

  @AfterEach
  void tearDown() throws SQLException {
    if (connection != null && !connection.isClosed()) {
      connection.close();
    }
  }

  @Test
  void testFetchQuoteDataFromAPI() {
    String ticker = "GOOGL";
    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    assertTrue(result.isPresent());
    Quote quote = result.get();
    assertEquals(ticker, quote.getTicker());
  }
}
