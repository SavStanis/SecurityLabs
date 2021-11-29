package com.savstanis.crypto.lab3;

import com.savstanis.crypto.lab3.generator.Generator;
import com.savstanis.crypto.lab3.generator.LcgGenerator;

public class LcgBreaker {
    private static final String ACCOUNT_ID = "8333";
    private static final Integer MAX_NUMBER_VALUE = Integer.MAX_VALUE;

    public static void main(String[] args) {
        Generator generator = getWinningGenerator();
        System.out.println(CasinoClient.play(Constants.LCG_MODE, ACCOUNT_ID, 500, generator.next()));
        System.out.println(CasinoClient.play(Constants.LCG_MODE, ACCOUNT_ID, 500, generator.next()));
        System.out.println(CasinoClient.play(Constants.LCG_MODE, ACCOUNT_ID, 500, generator.next()));
    }

    public static Generator getWinningGenerator() {
        var state0 = (int) CasinoClient.play(Constants.LCG_MODE, ACCOUNT_ID, 1, 1).getRealNumber();
        var state1 = (int) CasinoClient.play(Constants.LCG_MODE, ACCOUNT_ID, 1, 1).getRealNumber();
        var state2 = (int) CasinoClient.play(Constants.LCG_MODE, ACCOUNT_ID, 1, 1).getRealNumber();

        int a = 0;
        while ((state1 - state2) - a * (state0 - state1) % MAX_NUMBER_VALUE != 0) {
            a++;
        }

        int c = state1 - a * state0;

        return new LcgGenerator(state2, a, c);
    }
}
