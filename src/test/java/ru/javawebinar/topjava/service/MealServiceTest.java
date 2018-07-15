package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-repository.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.parse("2018-07-07T10:00"), "newMeal", 1000);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), newMeal, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4,
                USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateUserDateTimeCreate() throws Exception {
        service.create(new Meal(USER_MEAL_1.getDateTime(), "Duplicate", 100500), USER_ID);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_1.getId(), USER_ID);
        assertMatch(service.getAll(USER_ID), USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2);
    }

    @Test(expected = NotFoundException.class)
    public void illegalDelete() throws Exception {
        service.delete(ADMIN_MEAL_1.getId(), USER_ID);
    }

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_1.getId(), USER_ID);
        assertMatch(meal, USER_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void illegalGet() throws Exception {
        service.get(ADMIN_MEAL_1.getId(), USER_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> meals = service.getBetweenDates(LocalDate.parse("2018-07-05"),
                LocalDate.parse("2018-07-05"), USER_ID);
        assertMatch(meals, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.parse("2018-07-05T10:00"),
                LocalDateTime.parse("2018-07-05T13:00"), USER_ID);
        assertMatch(meals, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, ADMIN_MEAL_2, ADMIN_MEAL_1);

        all = service.getAll(USER_ID);
        assertMatch(all, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void update() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDescription("UpdatedDescription");
        updated.setDateTime(updated.getDateTime().plusHours(1));
        updated.setCalories(100500);
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_MEAL_1.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void illegalUpdate() {
        service.update(ADMIN_MEAL_1, USER_ID);
    }
}