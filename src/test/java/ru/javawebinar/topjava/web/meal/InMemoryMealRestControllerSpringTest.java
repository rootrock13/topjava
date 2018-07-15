package ru.javawebinar.topjava.web.meal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-repository-test.xml"
})
@RunWith(SpringRunner.class)
public class InMemoryMealRestControllerSpringTest {

    @Autowired
    private MealRestController controller;

    @Autowired
    private InMemoryMealRepositoryImpl repository;

    @BeforeClass
    public static void beforeClass() {
        SecurityUtil.setAuthUserId(ADMIN_ID);
    }

    @Before
    public void setUp() throws Exception {
        repository.init();
    }

    @Test
    public void testDelete() throws Exception {
        controller.delete(ADMIN_MEAL_1.getId());
        Collection<MealWithExceed> mealWithExceeds = controller.getAll();
        Assert.assertEquals(mealWithExceeds.size(), 1);
        Assert.assertEquals(mealWithExceeds.iterator().next().getId(), ADMIN_MEAL_2.getId());
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        controller.delete(USER_MEAL_1.getId());
    }
}
