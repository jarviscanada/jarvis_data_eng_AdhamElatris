package ca.jrvs.apps.grep;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface LambdaStreamExec {

  Stream<String> createStrStream(Stream<String> stream);

  Stream<String> toUpperCase(Stream<String> ... stream);

  Stream<String> filter(Stream<String> stringStream, String pattern);

  IntStream createIntStream(int[] arr);

  <E> List<E> toList(Stream<E> stream);

  List<Integer> toList(IntStream intStream);

  IntStream createIntStream(int start, int end);

  DoubleStream squareRootIntStream(IntStream intStream);

  IntStream getOdd(IntStream intStream);

  Consumer<String> getLambdaPrinter(String prefix, String suffix);

  void printMessages(String[] messages, Consumer<String> printer);

  void printOdd(IntStream intStream, Consumer<String> printer);

  Stream<Integer> flatNestedInt(Stream<List<Integer>> ints);
}
