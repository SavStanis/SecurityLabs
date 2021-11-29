package com.savstanis.crypto.lab3.generator;

public class MersenneTwisterGenerator implements Generator {
    private static final int BYTES_POOL_SIZE = 624;
    private static final int PERIOD = 397;

    private static final int[] MAG01 = { 0x0, 0x9908b0df };

    private int[] mt;
    private int mti;

    public MersenneTwisterGenerator(int seed) {
        mt = new int[BYTES_POOL_SIZE];
        setSeed(seed);
    }

    public void setSeed(int seed) {
        long longMT = seed;
        mt[0]= (int) longMT;

        for (mti = 1; mti < BYTES_POOL_SIZE; ++mti) {
            longMT = (1812433253l * (longMT ^ (longMT >> 30)) + mti) & 0xffffffffL;
            mt[mti]= (int) longMT;
        }

    }

    public void untemperByIndex(int index, int y) {
        y ^= y >>> 18;
        y ^= (y << 15) & 0xEFC60000;
        int mask = 0x9d2c5680;

        for (int i = 0; i < 7; i++) {
            y ^= (y << 7) & mask;
        }

        y ^= y >>> 11;
        y ^= y >>> 11;
        y ^= y >>> 11;

        mt[index] = y;
    }

    @Override
    public int next() {
        int y;

        if (mti >= BYTES_POOL_SIZE) {
            int mtNext = mt[0];
            for (int k = 0; k < BYTES_POOL_SIZE - PERIOD; ++k) {
                int mtCurr = mtNext;
                mtNext = mt[k + 1];
                y = (mtCurr & 0x80000000) | (mtNext & 0x7fffffff);
                mt[k] = mt[k + PERIOD] ^ (y >>> 1) ^ MAG01[y & 0x1];
            }
            for (int k = BYTES_POOL_SIZE - PERIOD; k < BYTES_POOL_SIZE - 1; ++k) {
                int mtCurr = mtNext;
                mtNext = mt[k + 1];
                y = (mtCurr & 0x80000000) | (mtNext & 0x7fffffff);
                mt[k] = mt[k + (PERIOD - BYTES_POOL_SIZE)] ^ (y >>> 1) ^ MAG01[y & 0x1];
            }
            y = (mtNext & 0x80000000) | (mt[0] & 0x7fffffff);
            mt[BYTES_POOL_SIZE - 1] = mt[PERIOD - 1] ^ (y >>> 1) ^ MAG01[y & 0x1];

            mti = 0;
        }

        y = mt[mti++];

        y ^=  y >>> 11;
        y ^= (y <<   7) & 0x9d2c5680;
        y ^= (y <<  15) & 0xefc60000;
        y ^=  y >>> 18;

        return y;
    }
}
