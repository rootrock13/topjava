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

    private static AtomicInteger currentId = new AtomicInteger(0);
    private static Map<Integer, Meal> storage = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(MealDaoWithHardCodedDB.class);

    static {
        addToStorage(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        addToStorage(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        addToStorage(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        addToStorage(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        addToStorage(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        addToStorage(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        log.debug("fill storage");
    }

    private static Meal addToStorage(Meal meal) {
        meal.setId(currentId.incrementAndGet());
        storage.put(currentId.get(), meal);
        return meal;
    }

    @Override
    public Meal add(Meal meal) {
        Meal savedMeal = addToStorage(meal);
        log.debug(String.format("save meal with id = %d", savedMeal.getId()));
        return savedMeal;
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
