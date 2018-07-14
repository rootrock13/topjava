package ru.javawebinar.topjava.web.meal;

import org.junit.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Arrays;
import java.util.Collection;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class InMemoryMealRestControllerTest {
    private static ConfigurableApplicationContext appCtx;
    private static MealRestController controller;

    @BeforeClass
    public static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext("spring/test-spring-app.xml");
        System.out.println("\n" + Arrays.toString(appCtx.getBeanDefinitionNames()) + "\n");
        controller = appCtx.getBean(MealRestController.class);
    }

    @AfterClass
    public static void afterClass() {
        appCtx.close();
    }

    @Before
    public void setUp() throws Exception {
        // re-initialize
        InMemoryMealRepositoryImpl repository = appCtx.getBean(InMemoryMealRepositoryImpl.class);
        repository.init();
        SecurityUtil.setAuthUserId(USER_ID);
    }

    @Test
    public void testDelete() throws Exception {
        controller.delete(USER_MEAL_1.getId());
        Collection<MealWithExceed> mealWithExceeds = controller.getAll();
        Assert.assertEquals(mealWithExceeds.size(), 5);

        controller.delete(USER_MEAL_2.getId());
        controller.delete(USER_MEAL_3.getId());
        controller.delete(USER_MEAL_4.getId());
        controller.delete(USER_MEAL_5.getId());
        mealWithExceeds = controller.getAll();
        Assert.assertEquals(mealWithExceeds.size(), 1);
        Assert.assertEquals(mealWithExceeds.iterator().next().getId(), USER_MEAL_6.getId());

        SecurityUtil.setAuthUserId(ADMIN_ID);
        mealWithExceeds = controller.getAll();
        Assert.assertEquals(mealWithExceeds.size(), 2);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        controller.delete(ADMIN_MEAL_1.getId());
    }
}