package com.gmail.sizeeq.lotto.domain.numbergenerator;

import java.util.Set;

public class sdfsdfg {

    static RandomNumberGenerable generator = new RandomGenerator();

    public static void main(String[] args) {
        Set<Integer> integers = generator.generateSixNumbers();
        Set<Integer> integers2 = generator.generateSixNumbers();
        Set<Integer> integers3 = generator.generateSixNumbers();
        Set<Integer> integers4 = generator.generateSixNumbers();
        Set<Integer> integers5 = generator.generateSixNumbers();
        Set<Integer> integers6 = generator.generateSixNumbers();
        Set<Integer> integers7 = generator.generateSixNumbers();
        System.out.println(integers);
        System.out.println(integers2);
        System.out.println(integers3);
        System.out.println(integers4);
        System.out.println(integers5);
        System.out.println(integers6);
        System.out.println(integers7);
    }
}
