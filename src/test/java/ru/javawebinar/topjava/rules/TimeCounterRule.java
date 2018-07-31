package ru.javawebinar.topjava.rules;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public class TimeCounterRule extends Stopwatch {

    private final StringBuilder results;
    private final Logger log;

    public TimeCounterRule(StringBuilder results, Logger log) {
        this.results = results;
        this.log = log;
    }

    @Override
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    protected void finished(long nanos, Description description) {
        String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        results.append(result);
        log.info(result + " ms\n");
    }
}
