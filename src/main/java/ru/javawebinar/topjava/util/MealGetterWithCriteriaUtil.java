package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MealGetterWithCriteriaUtil {
    public List<Meal> getList(EntityManager em, int userId, MealCriteriaHelper criteriaHelper, boolean ordered) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Meal> criteriaQuery = cb.createQuery(Meal.class);
        Root<Meal> root = criteriaQuery.from(Meal.class);

        List<Predicate> predicates = new ArrayList<>();
        Predicate userIdCondition = cb.equal(root.get("user").get("id"), userId);
        predicates.add(userIdCondition);
        predicates.addAll(criteriaHelper.getPredicates(cb, root));

        criteriaQuery.select(root);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        if (ordered) {
            criteriaQuery.orderBy(cb.desc(root.get("dateTime")));
        }

        TypedQuery<Meal> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
