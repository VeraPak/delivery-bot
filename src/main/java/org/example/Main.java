package org.example;

import java.util.*;

public class Main {
    final static int countOfThreads = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static void main(String[] args) {

        long startTs = System.currentTimeMillis(); // start time

        for (int i = 0; i < countOfThreads; i++) {
            new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = (int) route.chars().filter(c -> c == 'R').count();

                synchronized(sizeToFreq) {
                    if(sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else {
                        sizeToFreq.put(countR, 1);
                    }
                }
            }).start();
        }

        sizeToFreq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(max -> System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", max.getKey(), max.getValue()));

        System.out.println("Другие размеры:");
        sizeToFreq.forEach((key, value) -> System.out.printf("- %d (%d раз)\n", key, value));

        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");
    }
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}