package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class QuoteHttpHelper {


  private OkHttpClient client;
  private static String apiKey = "1bad3f1958msh3c9487885639adcp13b034jsnfed42da3e913";

  // Constructor
  public QuoteHttpHelper() {
    this.client = new OkHttpClient();
  }


  public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
    // Construct the URL for the API request
    String url = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+symbol+"&datatype=json";

    // Build the request
    Request request = new Request.Builder()
        .url(url)
        .header("X-RapidAPI-Key", apiKey)
        .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
        .build();

    try (Response response = client.newCall(request).execute()) {
      // Check if the response is successful
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected response: " + response);
      }

      String responseString = response.body().string();
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(responseString).get("Global Quote");

      if (rootNode == null || rootNode.isEmpty()) {
        throw new IllegalArgumentException("No data found for symbol: " + symbol);
      }

      // Create and return a new Quote object
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

  public static void main(String[] args) {
    QuoteHttpHelper quoteHttpHelper = new QuoteHttpHelper();

    Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");
    System.out.println(quote);


  }
}
