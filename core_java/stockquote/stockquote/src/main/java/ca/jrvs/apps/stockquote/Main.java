package ca.jrvs.apps.stockquote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

  public static void main(String[] args) {

    String apiKey = "1bad3f1958msh3c9487885639adcp13b034jsnfed42da3e913";
    String symbol = "MSFT";

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(
            "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol
                + "&datatype=json"))
        .header("X-RapidAPI-Key", apiKey)
        .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
    try {
      HttpResponse<String> response = HttpClient.newHttpClient()
          .send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}