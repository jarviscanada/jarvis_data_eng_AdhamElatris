package service_test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.model.Quote;
import ca.jrvs.apps.stockquote.service.QuoteService;

class QuoteService_UnitTest {

  @Mock
  private QuoteDao quoteDao;

  @Mock
  private QuoteHttpHelper quoteHttpHelper;

  @InjectMocks
  private QuoteService quoteService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFetchQuoteDataFromAPI_validTicker() {
    // Arrange
    String ticker = "AAPL";
    Quote quote = new Quote();
    quote.setTicker(ticker);
    quote.setPrice(150.0);
    when(quoteHttpHelper.fetchQuoteInfo(ticker)).thenReturn(quote);

    // Act
    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(ticker, result.get().getTicker());
    assertEquals(150.0, result.get().getPrice());
    verify(quoteDao, times(1)).save(quote);
  }

  @Test
  void testFetchQuoteDataFromAPI_invalidTicker() {
    // Arrange: invalid input should throw an exception
    String ticker = "";

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      quoteService.fetchQuoteDataFromAPI(ticker);
    });
    String expectedMessage = "Invalid input";
    assertTrue(exception.getMessage().contains(expectedMessage));
  }
}
