import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.stockquote.model.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuoteHttpHelperTest {

  private QuoteHttpHelper quoteHttpHelper;

  @BeforeEach
  void setUp() {
    quoteHttpHelper = new QuoteHttpHelper();
  }

  @Test
  void testFetchQuoteInfoValidSymbol() {
    Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

    assertNotNull(quote);
    assertEquals("MSFT", quote.getTicker());
    assertTrue(quote.getOpen() >= 408.0);
    assertTrue(quote.getHigh() >= 410.597);
    assertTrue(quote.getLow() >= 406.5);
    assertTrue(quote.getPrice() >= 409.64);
    assertTrue(quote.getVolume() >= 20544998);
    assertNotNull(quote.getLatestTradingDay());
    assertTrue(quote.getPreviousClose() >= 408.43);
    assertNotNull(quote.getChange());
    assertNotNull(quote.getChangePercent());
  }
}
