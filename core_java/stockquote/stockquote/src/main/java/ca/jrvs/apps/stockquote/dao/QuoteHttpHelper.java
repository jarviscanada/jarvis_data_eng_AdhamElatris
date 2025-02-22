package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.model.Quote;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteHttpHelper {


  private static final String apiKey = "1bad3f1958msh3c9487885639adcp13b034jsnfed42da3e913";
  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private final OkHttpClient client;

  public QuoteHttpHelper() {
    this.client = new OkHttpClient();
  }

  public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
    String url = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol
        + "&datatype=json";

    Request request = new Request.Builder()
        .url(url)
        .header("X-RapidAPI-Key", apiKey)
        .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected response: " + response);
      }

      String responseString = response.body().string();
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(responseString).get("Global Quote");

      if (rootNode == null || rootNode.isEmpty()) {
        logger.error("\nNo data found for symbol: " + symbol);
        return null;
        // throw new IllegalArgumentException("No data found for symbol: " + symbol);
      }

      Quote quote = new Quote(
          rootNode.get("01. symbol").asText(),
          rootNode.get("02. open").asDouble(),
          rootNode.get("03. high").asDouble(),
          rootNode.get("04. low").asDouble(),
          rootNode.get("05. price").asDouble(),
          rootNode.get("06. volume").asInt(),
          new Date(),
          rootNode.get("08. previous close").asDouble(),
          rootNode.get("09. change").asDouble(),
          rootNode.get("10. change percent").asText(),
          new Timestamp(System.currentTimeMillis())
      );

      return quote;

    } catch (IOException e) {
      throw new IllegalArgumentException("Error fetching stock data: " + e.getMessage());
    }
  }
}
