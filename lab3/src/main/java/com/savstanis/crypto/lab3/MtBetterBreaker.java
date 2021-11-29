package com.savstanis.crypto.lab3;

import com.savstanis.crypto.lab3.generator.Generator;
import com.savstanis.crypto.lab3.generator.MersenneTwisterGenerator;

public class MtBetterBreaker {
    private static final String ACCOUNT_ID = "18333";

    public static void main(String[] args) {
//        testCopyGeneratorState(111);

        Generator generator = getWinningGenerator();
        var next =  getUnsignedInt(generator.next());
        System.out.println(CasinoClient.play(Constants.BETTER_MT_MODE, ACCOUNT_ID, 100, next));
    }

    private static Generator getWinningGenerator() {
        MersenneTwisterGenerator generator = new MersenneTwisterGenerator(0);
        for (int i = 0; i < 624; i++) {
            var gameRes = CasinoClient.play(Constants.BETTER_MT_MODE, ACCOUNT_ID, 1, 1);
            generator.untemperByIndex(i, (int) gameRes.getRealNumber());
        }

        return generator;
    }

    private static void testCopyGeneratorState(int seed) {
        MersenneTwisterGenerator initGenerator = new MersenneTwisterGenerator(seed);
        MersenneTwisterGenerator copiedGenerator = copyGeneratorState(initGenerator);

        var initNext = initGenerator.next();
        var copiedNext = copiedGenerator.next();
        System.out.println("Init next: " + initNext);
        System.out.println("Copy next: " + copiedNext);
        if (initNext == copiedNext) {
            System.out.println("Generator's state successfully copied");
        }
    }

    private static MersenneTwisterGenerator copyGeneratorState(MersenneTwisterGenerator generator) {
        MersenneTwisterGenerator copy = new MersenneTwisterGenerator(0);

        for (int i = 0; i < 624; i++) {
            var generated = generator.next();
            copy.untemperByIndex(i, generated);
        }

        return copy;
    }

    private static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }
}
