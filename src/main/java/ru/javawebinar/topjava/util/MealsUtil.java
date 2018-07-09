package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MealsUtil {
    public static final List<Meal> MEALS_OF_USER_1 = Arrays.asList(
            new Meal(LocalDateTime.of(2018, Month.JULY, 5, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2018, Month.JULY, 5, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2018, Month.JULY, 5, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2018, Month.JULY, 6, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2018, Month.JULY, 6, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2018, Month.JULY, 6, 20, 0), "Ужин", 510)
    );
    public static final List<Meal> MEALS_OF_USER_2 = Arrays.asList(
            new Meal(LocalDateTime.of(2018, Month.JULY, 5, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2018, Month.JULY, 5, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2018, Month.JULY, 5, 17, 30), "Ужин", 400),
            new Meal(LocalDateTime.of(2018, Month.JULY, 6, 10, 0), "Завтрак", 900),
            new Meal(LocalDateTime.of(2018, Month.JULY, 6, 13, 0), "Обед", 550),
            new Meal(LocalDateTime.of(2018, Month.JULY, 6, 15, 0), "Полдник", 200),
            new Meal(LocalDateTime.of(2018, Month.JULY, 6, 20, 0), "Ужин", 450)
    );

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealWithExceed> getWithExceeded(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExceeded(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return getFilteredWithExceeded(meals, caloriesPerDay, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }

    private static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

    public static MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }
}