package org.example;

import java.util.*;

public class Main {
    final static int countOfThreads = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {

        long startTs = System.currentTimeMillis(); // start time

        Thread threadOfMax = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sizeToFreq.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .ifPresent(max -> System.out.printf("Самое частое количество текущих повторений %d (встретилось %d раз)\n", max.getKey(), max.getValue()));
                }
            }
        });
        threadOfMax.start();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < countOfThreads; i++) {
            Thread newThread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = (int) route.chars().filter(c -> c == 'R').count();

                synchronized(sizeToFreq) {
                    if(sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else {
                        sizeToFreq.put(countR, 1);
                    }
                    sizeToFreq.notify();
                }
            });
            threads.add(newThread);
        }
        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }

        threadOfMax.interrupt();

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