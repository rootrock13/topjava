package ru.javawebinar.topjava.service;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {
    @BeforeClass
    public static void setHeaderForResults() {
        results.append(String.format(header, DataJpaMealServiceTest.class.getSimpleName()));
    }
}
