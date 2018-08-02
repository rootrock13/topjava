package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TimingRules {

    private static final Logger log = LoggerFactory.getLogger("result");

    private static StringBuilder results = new StringBuilder();
    private static Long estimated = 0L;

    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public static final Stopwatch STOPWATCH = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            long currentEstimated = TimeUnit.NANOSECONDS.toMillis(nanos);
            estimated += currentEstimated;
            String result = String.format("\n%-25s %7d", description.getMethodName(), currentEstimated);
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    public static final TestRule SUMMARY = (base, description) -> new Statement() {
        @Override
        public void evaluate() throws Throwable {
            results.setLength(0);
            estimated = 0L;
            base.evaluate();
            String testRunnerClassName = description.getDisplayName();
            log.info("\n---------------------------------" +
                    "\nTests by " + testRunnerClassName.substring(testRunnerClassName.lastIndexOf(".") + 1) +
                    "\nDB profile: " + Profiles.getActiveDbProfile() +
                    "\n---------------------------------" +
                    "\nTest                 Duration, ms" +
                    "\n---------------------------------" +
                    results +
                    "\n---------------------------------" +
                    String.format("\nOverall estimated time: %9d", estimated) +
                    "\n---------------------------------"
            );
        }
    };
}
