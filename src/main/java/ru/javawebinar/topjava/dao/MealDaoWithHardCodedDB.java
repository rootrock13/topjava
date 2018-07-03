package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoWithHardCodedDB implements MealDao {

    private static final Logger log = LoggerFactory.getLogger(MealDaoWithHardCodedDB.class);
    private AtomicInteger currentId;
    private Map<Integer, Meal> storage;

    public MealDaoWithHardCodedDB() {
        currentId = new AtomicInteger(0);
        storage = new ConcurrentHashMap<>();
        log.debug("fill storage");
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public Meal add(Meal meal) {
        meal.setId(currentId.incrementAndGet());
        storage.put(currentId.get(), meal);
        log.debug(String.format("save new meal with id = %d", meal.getId()));
        return meal;
    }

    @Override
    public Meal getById(int id) {
        log.debug(String.format("get meal with id = %d", id));
        return storage.get(id);
    }

    @Override
    public List<Meal> getAll() {
        log.debug("get mealList");
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Meal meal) {
        storage.put(meal.getId(), meal);
        log.debug(String.format("save updated meal with id = %d", meal.getId()));
    }

    @Override
    public void delete(int id) {
        log.debug(String.format("delete meal with id = %d", id));
        storage.remove(id);
    }
}
