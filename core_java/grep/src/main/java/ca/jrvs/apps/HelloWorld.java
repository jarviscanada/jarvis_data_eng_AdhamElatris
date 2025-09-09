package ca.jrvs.apps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class HelloWorld {

  // Your program begins with a call to main().
  // Prints "Hello, World" to the terminal window.
  public static void main(String[] args) {
    List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

    names.forEach(name -> System.out.println(name));
  }
}
