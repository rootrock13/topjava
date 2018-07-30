package ru.javawebinar.topjava.service;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JDBC)
public class JdbcMealServiceTest extends MealServiceTest {
    @BeforeClass
    public static void setHeaderForResults() {
        results.append(String.format(header, JdbcMealServiceTest.class.getSimpleName()));
    }
}
