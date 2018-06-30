package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );


        List<UserMealWithExceed> userMealWithExceedsByCycle =
                getFilteredWithExceeded(mealList,
                        LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        List<UserMealWithExceed> userMealWithExceedsByStreams =
                getFilteredWithExceededByStreams(mealList,
                        LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        System.out.println("userMealWithExceeds (by cycle): ");
        userMealWithExceedsByCycle.forEach(System.out::println);

        System.out.println("\nuserMealWithExceeds (by streams): ");
        userMealWithExceedsByStreams.forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, StatisticsItem> statistics = new HashMap<>();
        List<UserMealWithExceed> resultList = new ArrayList<>();
        for (UserMeal meal : mealList) {
            fillStatistics(meal, caloriesPerDay, statistics);
            if (TimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
                resultList.add(createMealWithExceed(meal, statistics));
            }
        }
        return resultList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByStreams(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, StatisticsItem> statistics = new HashMap<>();
        return mealList.stream()
                .peek(meal -> fillStatistics(meal, caloriesPerDay, statistics))
                .filter(meal -> TimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .map(userMeal -> createMealWithExceed(userMeal, statistics))
                .collect(Collectors.toList());
    }

    private static UserMealWithExceed createMealWithExceed(UserMeal meal, Map<LocalDate, StatisticsItem> statistics) {
        return new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), statistics.get(meal.getDate()).isExceed());
    }

    private static void fillStatistics(UserMeal meal, int caloriesPerDay, Map<LocalDate, StatisticsItem> statistics) {
        LocalDate date = meal.getDate();
        StatisticsItem item = statistics.getOrDefault(date, new StatisticsItem(caloriesPerDay));
        item.addCalories(meal.getCalories());
        statistics.put(date, item);
    }

    private static class StatisticsItem {
        private int caloriesPerDay;
        private int calories;
        private AtomicBoolean exceed;

        private StatisticsItem(int caloriesPerDay) {
            this.caloriesPerDay = caloriesPerDay;
            this.exceed = new AtomicBoolean(false);
        }

        private void addCalories(int calories) {
            this.calories += calories;
            checkExceed();
        }

        private AtomicBoolean isExceed() {
            return exceed;
        }

        private void checkExceed() {
            exceed.set(calories > caloriesPerDay);
        }
    }
}
