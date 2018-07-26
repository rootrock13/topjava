package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    // Obtaining a CriteriaBuilder Instance in RequestBean
    // (https://docs.oracle.com/cd/E19798-01/821-1841/bnbpy/index.html)
    //
    // The CrtiteriaBuilder interface defines methods to create criteria query objects and create expressions
    // for modifying those query objects. RequestBean creates an instance of CriteriaBuilder
    // by using a @PostConstruct method, init:

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    private void init() {
        criteriaBuilder = em.getCriteriaBuilder();
    }

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
        return DataAccessUtils.singleResult(getList(userId, false, ((root) -> criteriaBuilder.equal(root.get("id"), id))));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getList(userId, true, (root) -> criteriaBuilder.and());
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return getList(userId, true, ((root) ->
                criteriaBuilder.between(root.get("dateTime"), startDate, endDate)));
    }

    private List<Meal> getList(int userId, boolean ordered, CriteriaHelper criteriaHelper) {
        CriteriaQuery<Meal> criteriaQuery = criteriaBuilder.createQuery(Meal.class);
        Root<Meal> root = criteriaQuery.from(Meal.class);

        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.equal(root.get("user").get("id"), userId),
                criteriaHelper.getPredicate(root)
        );
        if (ordered) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("dateTime")));
        }

        TypedQuery<Meal> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @FunctionalInterface
    private interface CriteriaHelper {
        Predicate getPredicate(Root<Meal> root);
    }
}