package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        service.delete(id, authUserId());
    }

    public Meal get(int id) {
        return service.get(id, authUserId());
    }

    public void update(Meal meal) {
        service.update(meal, authUserId());
    }

    public List<MealWithExceed> getAll() {
        return MealsUtil.getWithExceeded(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getAll(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        fromDate = fromDate == null ? LocalDate.MIN : fromDate;
        toDate = toDate == null ? LocalDate.MAX : toDate;
        fromTime = fromTime == null ? LocalTime.MIN : fromTime;
        toTime = toTime == null ? LocalTime.MAX : toTime;
        return service.getAll(authUserId(), authUserCaloriesPerDay(), fromDate, toDate, fromTime, toTime);
    }
}