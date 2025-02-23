package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.model.Quote;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

  private final QuoteDao dao;
  private final QuoteHttpHelper httpHelper;

  public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
    this.dao = dao;
    this.httpHelper = httpHelper;
  }

  /**
   * Fetches latest quote data from endpoint
   *
   * @param ticker Stock symbol
   * @return Latest quote information or empty optional if ticker symbol not found
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
    if (ticker == null || ticker.isBlank()) {
      logger.error("Invalid symbol");
      throw new IllegalArgumentException("Invalid input");
    } else {
      Optional<Quote> quote = Optional.ofNullable(httpHelper.fetchQuoteInfo(ticker));
      if (quote.isPresent()) {
        dao.save(quote.get());
        //logger.info("\nFetched successfully !");
        return Optional.of(quote.get());
      }
    }
    logger.error("\nError fetching quote, make sure symbol is correct");
    return Optional.empty();
  }
}