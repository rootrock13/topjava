package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        meal.setUser(ref);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            return get(meal.getId(), userId) != null ? em.merge(meal) : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return DataAccessUtils.singleResult(getList(userId, ((cb, root) -> Collections.singletonList(
                cb.equal(root.get("id"), id))), false));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getList(userId, (cb, root) -> Collections.emptyList(), true);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return getList(userId, ((cb, root) -> Collections.singletonList(
                cb.between(root.get("dateTime"), startDate, endDate))), true);
    }

    private List<Meal> getList(int userId, CriteriaHelper criteriaHelper, boolean ordered) {
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

    @FunctionalInterface
    private interface CriteriaHelper {
        List<Predicate> getPredicates(CriteriaBuilder cb, Root<Meal> root);
    }
}