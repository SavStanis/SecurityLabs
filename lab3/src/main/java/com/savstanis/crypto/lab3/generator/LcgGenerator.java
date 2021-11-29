package com.savstanis.crypto.lab3.generator;

public class LcgGenerator implements Generator {
    private int seed;

    private int a;
    private int c;
    private int m;

    public LcgGenerator() {
        seed = (int) (Math.random() * Integer.MAX_VALUE);
        m = Integer.MAX_VALUE;
        a = 1664525;
        c = 1013904223;
    }

    public LcgGenerator(int seed) {
        this.seed = seed;
        m = Integer.MAX_VALUE;
        a = 1664525;
        c = 1013904223;
    }

    public LcgGenerator(int seed, int a, int c) {
        this.seed = seed;
        this.a = a;
        this.c = c;
        this.m = Integer.MAX_VALUE;
    }

    public LcgGenerator(int seed, int a, int c, int m) {
        this.seed = seed;
        this.a = a;
        this.c = c;
        this.m = m;
    }

    @Override
    public int next() {
        seed = (a * seed + c) % m;
        return seed;
    }
}
