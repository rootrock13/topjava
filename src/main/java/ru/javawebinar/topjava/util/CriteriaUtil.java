package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class CriteriaUtil {
    public List<Meal> getList(EntityManager em, CriteriaHelper criteriaHelper) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Meal> criteriaQuery = cb.createQuery(Meal.class);
        Root<Meal> root = criteriaQuery.from(Meal.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaHelper.getPredicate(cb, root).toArray(new Predicate[0]));
        criteriaQuery.orderBy(cb.desc(root.get("dateTime")));

        TypedQuery<Meal> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
