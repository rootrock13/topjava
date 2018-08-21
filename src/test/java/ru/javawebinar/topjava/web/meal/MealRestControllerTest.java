package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.contentJson;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testGetAll() throws Exception {
        TestUtil.print(mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(contentJson(MealsUtil.getWithExceeded(MEALS, SecurityUtil.authUserCaloriesPerDay()))));
    }

    @Test
    void testGetBetween() throws Exception {
        // filter data
        LocalTime fromTime = LocalTime.of(10, 30);
        LocalTime toTime = LocalTime.of(20, 0);
        LocalDate fromDate = LocalDate.of(2015, 5, 31);
        LocalDate toDate = LocalDate.of(2015, 5, 31);

        // prepare expected
        List<MealWithExceed> expected = prepareExpectedWithExceed(MEALS, fromDate, toDate, fromTime, toTime);

        // get actual and test
        String url = String.format("%sbetween?fromTime=%s&toTime=%s&fromDate=%s&toDate=%s",
                REST_URL, fromTime, toTime, fromDate, toDate);
        TestUtil.print(mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(contentJson(expected)));
    }

    @Test
    void testGetFiltered() throws Exception {
        // filter data
        LocalDateTime fromDateTime = LocalDateTime.of(2015, 6, 1, 14, 30);
        LocalDateTime toDateTime = LocalDateTime.of(2015, 6, 1, 22, 0);

        // prepare expected
        List<MealWithExceed> expected = prepareExpectedWithExceed(ADMIN_MEALS,
                fromDateTime.toLocalDate(), toDateTime.toLocalDate(), fromDateTime.toLocalTime(), toDateTime.toLocalTime());
        SecurityUtil.setAuthUserId(ADMIN_ID);

        // get actual and test
        String url = String.format("%sfilter?fromDateTime=%s&toDateTime=%s",
                REST_URL, fromDateTime, toDateTime);
        TestUtil.print(mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(contentJson(expected)));

        // restore defaults
        SecurityUtil.setAuthUserId(USER_ID);
    }

    private List<MealWithExceed> prepareExpectedWithExceed(List<Meal> meals,
                                                           LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        List<MealWithExceed> mealsWithExceed = MealsUtil.getFilteredWithExceeded(
                meals,
                SecurityUtil.authUserCaloriesPerDay(),
                fromTime,
                toTime);
        return mealsWithExceed.stream()
                .filter(m -> Util.isBetween(m.getDateTime().toLocalDate(),
                        Util.orElse(fromDate, LocalDate.MIN),
                        Util.orElse(toDate, LocalDate.MAX)))
                .collect(Collectors.toList());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setDescription(updated.getDescription() + " - updated");
        updated.setCalories(updated.getCalories() + 20);
        mockMvc.perform(
                put(REST_URL + MEAL1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated))
        )
                .andDo(print())
                .andExpect(status().isOk());
        assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void testCreateWithLocation() throws Exception {
        Meal expected = new Meal(LocalDateTime.now(), "новая еда", 500);
        ResultActions action = mockMvc.perform(
                post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(expected))
        )
                .andDo(print())
                .andExpect(status().isCreated());

        Meal returned = TestUtil.readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(mealService.getAll(USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }
}