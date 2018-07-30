package ru.javawebinar.topjava.service;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @BeforeClass
    public static void setHeaderForResults() {
        results.append(String.format(header, DataJpaUserServiceTest.class.getSimpleName()));
    }
}
