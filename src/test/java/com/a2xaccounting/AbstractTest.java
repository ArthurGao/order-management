package com.a2xaccounting;

import org.jeasy.random.EasyRandom;

public class AbstractTest {

    private EasyRandom generator;

    protected <T> T createObject(Class<T> c) {
        if (generator == null) {
            generator = new EasyRandom();
        }
        return generator.nextObject(c);
    }
}
