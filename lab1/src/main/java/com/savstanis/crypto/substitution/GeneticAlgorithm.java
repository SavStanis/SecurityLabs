package com.savstanis.crypto.substitution;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithm {
    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int INIT_POPULATION_SIZE = 5000;
    private int GENERATION_NUMBER = 100;

    List<Pair<String, Integer>> popularNgrams;

    public String run(String cipheredText) {
        popularNgrams = calculateMostPopularNgrams();
        var population = generatePopulation();
        var fittestInNewGeneration = "";
        for (int generationCount = 0; generationCount < GENERATION_NUMBER; generationCount++) {
            List<String> tournamentWinners = new ArrayList<>();

            for ( int i = 0; i < 1000; i++) {
                tournamentWinners.add(tournament(population, cipheredText, 20));
            }

            List<String> newGeneration = new ArrayList<>();
            newGeneration.add(getFittestKey(population, cipheredText));
            for (int i = 0; i < 1000; i += 2) {
                Pair<String, String> children = crossover(tournamentWinners.get(i), tournamentWinners.get(i + 1));
                newGeneration.add(children.getLeft());
                newGeneration.add(children.getRight());
            }

            for (int i = 0; i < newGeneration.size(); i++) {
                if (Math.random() < 0.5) {
//                    System.out.println("mutation");
                    String mutated = mutation(newGeneration.get(i));
                    if (getFitness(mutated, cipheredText) > getFitness(newGeneration.get(i), cipheredText)) {
                        newGeneration.set(i, mutated);
//                        System.out.println("Good mutation");
//                        System.out.println(tryKey(mutated, cipheredText));
                    }
                }
            }

            fittestInNewGeneration = getFittestKey(newGeneration, cipheredText);
//            if (generationCount % 20 == 0) {
//                System.out.println("Generation: " + generationCount);
//                System.out.println("Fittest key: " + fittestInNewGeneration);
//                System.out.println("Try: " + tryKey(fittestInNewGeneration, cipheredText));
//            }

            population = newGeneration;
        }

        return tryKey(fittestInNewGeneration, cipheredText);
    }

    private String mutation(String key) {
        int mutationPos1 = (int) (Math.random() * 26);
        int mutationPos2 = (int) (Math.random() * 26);
        while (mutationPos1 == mutationPos2) {
            mutationPos2 = (int) (Math.random() * 26);
        }

        char c1 = key.charAt(mutationPos1);
        char c2 = key.charAt(mutationPos2);
        key = key.replace(c2, '"');
        key = key.replace(c1, c2);
        key = key.replace('"', c1);

        return key;
    }

    private Pair<String, String> crossover(String parent1, String parent2) {
        Set<Integer> crossover = new HashSet<>();
        Set<Character> used1 = new HashSet<>();
        Set<Character> used2 = new HashSet<>();


        for (int i = 0; i < 13; i++) {
            crossover.add((int) (Math.random() * 26));
        }
        char[] child1 = new char[26];
        char[] child2 = new char[26];

        for (int i = 0; i < 26; i++) {
            if (crossover.contains(i)) {
                child1[i] = parent1.charAt(i);
                used1.add(parent1.charAt(i));
            } else  {
                child2[i] = parent2.charAt(i);
                used2.add(parent2.charAt(i));
            }
        }

        Deque<Character> last1 = new ArrayDeque<>();
        Deque<Character> last2 = new ArrayDeque<>();

        for (int i = 0 ; i < parent1.length(); i++) {
            if (!used1.contains(parent2.charAt(i))) {
                last1.addLast(parent2.charAt(i));
            }

            if (!used2.contains(parent1.charAt(i))) {
                last2.addLast(parent1.charAt(i));
            }
        }


        for (int i = 0; i < 26; i++) {
            if (!crossover.contains(i)) {
                child1[i] = last1.pollFirst();
            } else  {
                child2[i] = last2.pollFirst();
            }
        }

        return Pair.of(String.valueOf(child1), String.valueOf(child2));
    }

    private String tournament(List<String> keys, String cipheredText, int tournamentSize) {
        List<String> tournament = new ArrayList<>();

        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * keys.size());
            tournament.add(i, keys.get(randomId));
        }
        return getFittestKey(tournament, cipheredText);
    }

    private List<String> generatePopulation() {
        List<String> population = new ArrayList<>();

        for (int i = 0; i < INIT_POPULATION_SIZE; i++) {
            StringBuffer stringBuffer = new StringBuffer();
            Set<Integer> used = new HashSet<>();

            int randomId = (int) (Math.random() * 26);

            for (int j = 0; j < 26; j ++) {
                while (used.contains(randomId)) {
                    randomId = (int) (Math.random() * 26);
                }

                stringBuffer.append(ALPHABET.charAt(randomId));
                used.add(randomId);
            }


            population.add(stringBuffer.toString());
        }

        return population;
    }

    private String getFittestKey(List<String> keys, String cipheredText) {
        int maxFitness = 0;
        String bestKey = keys.get(0);

        for (var key : keys) {
            int fitness = getFitness(key, cipheredText);
            if (fitness > maxFitness) {
                maxFitness = fitness;
                bestKey = key;
            }
        }
        return bestKey;
    }

    private int getFitness(String key, String cipheredText) {
        String possibleText = tryKey(key, cipheredText);

        int fitness = 0;

        for (var ngram: popularNgrams) {
            int counts = StringUtils.countMatches(possibleText, ngram.getLeft());
            if (counts > 0) {
                fitness += counts * ngram.getRight();
            }
        }

        return fitness;
    }

    public String tryKey(String key, String cipheredText) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < cipheredText.length(); i++) {
            char c = cipheredText.charAt(i);
            stringBuilder.append(ALPHABET.charAt(key.indexOf(c)));
        }

        return stringBuilder.toString();
    }




    private static List<Pair<String, Integer>> calculateMostPopularNgrams() {
        String str = "";
        try {
            BufferedReader Rb = new BufferedReader(new FileReader("substitution_example_2.txt"));
             str = Rb.lines().collect(Collectors.joining(" "));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        str = str.toUpperCase();
        str = str.replace(" ", "");

        Map<String, Integer> ngramCounters = new HashMap<>();

        for (int i = 2; i < 5; i++) {

            for (int j = 0; j < i; j++) {
                for (int k = j; k < str.length(); k += i) {
                    if (k + i >= str.length()) {
                        continue;
                    }
                    String ngram = str.substring(k, k + i);

                    int counter = Optional.ofNullable(ngramCounters.get(ngram)).orElse(0);
                    ngramCounters.put(ngram, ++counter);
                }
            }
        }

        List<Pair<String, Integer>> ngramsList = new ArrayList<>();

        ngramCounters.keySet().forEach(key -> ngramsList.add(Pair.of(key, ngramCounters.get(key))));

        ngramsList.sort((ngram1, ngram2) -> ngram2.getRight() - ngram1.getRight());


        return ngramsList.stream().filter((ngram) -> ngram.getRight() > 10).collect(Collectors.toList());
    }


}
