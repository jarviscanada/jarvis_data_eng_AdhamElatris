import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.model.Quote;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuoteHttpHelperTest {

  private QuoteHttpHelper quoteHttpHelper;

  @BeforeEach
  void setUp() {
    OkHttpClient okHttpClient = new OkHttpClient();
    String api = "1bad3f1958msh3c9487885639adcp13b034jsnfed42da3e913";
    quoteHttpHelper = new QuoteHttpHelper(api, okHttpClient);
  }

  @Test
  void testFetchQuoteInfoValidSymbol() {
    Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

    assertNotNull(quote);
    assertEquals("MSFT", quote.getTicker());
    assertTrue(quote.getOpen() >= 400.0);
    assertTrue(quote.getHigh() >= 400.597);
    assertTrue(quote.getLow() >= 350.5);
    assertTrue(quote.getPrice() >= 350.64);
    assertTrue(quote.getVolume() >= 20544998);
    assertNotNull(quote.getLatestTradingDay());
    assertTrue(quote.getPreviousClose() >= 400.43);
    assertNotNull(quote.getChange());
    assertNotNull(quote.getChangePercent());
  }
}