package com.savstanis.crypto.lab3;

import com.savstanis.crypto.lab3.generator.Generator;
import com.savstanis.crypto.lab3.generator.MersenneTwisterGenerator;


public class MtBreaker {
    private static final String ACCOUNT_ID = "222";
    private static final long SECONDS_IN_HOUR = 3600;

    public static void main(String[] args) {
        Generator generator = getWinningGenerator();
        System.out.println(CasinoClient.play(Constants.MT_MODE, ACCOUNT_ID, 500, getUnsignedInt(generator.next())));
    }

    private static Generator getWinningGenerator() {
        var state = CasinoClient.play(Constants.MT_MODE, ACCOUNT_ID, 1, 1);
        var initTime = (state.getAccount().getDeletionTime().getTime() / 1000) - SECONDS_IN_HOUR - 40;

        long casinoValue = state.getRealNumber();

        for (int i = 0; i < 80; i++ ) {
            Generator generator = new MersenneTwisterGenerator((int) (initTime + i));

            // for cases when account is not newly created
            for (int j = 0; j < 700; j ++) {
                var generated = generator.next();
                if (getUnsignedInt(generated) == casinoValue) {
                    return generator;
                }
            }

        }

        return null;
    }

    private static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }
}
