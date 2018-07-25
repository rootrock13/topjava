package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@FunctionalInterface
public interface CriteriaHelper {
    List<Predicate> getPredicate(CriteriaBuilder cb, Root<Meal> root);
}
