package ru.javawebinar.topjava.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import ru.javawebinar.topjava.Profiles;

public class ResultsClassRule implements TestRule {

    private final StringBuilder results;
    private final Logger log;

    public ResultsClassRule(StringBuilder results, Logger log) {
        this.results = results;
        this.log = log;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                results.setLength(0);
                base.evaluate();
                String testRunnerClassName = description.getDisplayName();
                log.info("\n---------------------------------" +
                        "\nTests by " + testRunnerClassName.substring(testRunnerClassName.lastIndexOf(".") + 1) +
                        "\nDB profile: " + Profiles.getActiveDbProfile() +
                        "\n---------------------------------" +
                        "\nTest                 Duration, ms" +
                        "\n---------------------------------" +
                        results +
                        "\n---------------------------------");
            }
        };
    }
}