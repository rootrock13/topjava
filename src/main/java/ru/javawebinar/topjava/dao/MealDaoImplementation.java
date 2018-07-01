package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImplementation implements MealDao {

    private static MealDao instance;
    private static Map<Integer, Meal> storage;
    private static AtomicInteger currentId = new AtomicInteger(0);

    private MealDaoImplementation() {
        storage = new ConcurrentHashMap<>();
        init();
    }

    private synchronized void init() {
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    public static synchronized MealDao getInstance() {
        if (instance == null) {
            instance = new MealDaoImplementation();
        }
        return instance;
    }

    @Override
    public synchronized void add(Meal meal) {
        meal.setId(currentId.incrementAndGet());
        storage.put(currentId.get(), meal);
    }

    @Override
    public Meal getById(int id) {
         return storage.get(id);
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> meals = new CopyOnWriteArrayList<>(storage.values());
        meals.sort(new Comparator<Meal>() {
            @Override
            public int compare(Meal o1, Meal o2) {
                return o1.getDateTime().compareTo(o2.getDateTime());
            }
        });
        return meals;
    }

    @Override
    public synchronized void update(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public synchronized void delete(int id) {
        storage.remove(id);
    }
}
