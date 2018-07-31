package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    Meal getOneByIdAndUserId(int id, int userId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:user_id")
    Meal getOneByIdAndUserIdFetched(@Param("id") int id, @Param("user_id") int userId);

    List<Meal> findByUserIdOrderByDateTimeDesc(int userId);

    List<Meal> findAllByUserIdAndDateTimeBetweenOrderByDateTimeDesc(int userId, LocalDateTime startDate, LocalDateTime endDate);

    /*
    // methods with Query:

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
    int delete(@Param("id") int id, @Param("user_id") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:user_id ORDER BY m.dateTime DESC")
    List<Meal> getAll(@Param("user_id") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:user_id and m.id=:id")
    List<Meal> get(@Param("id") int id, @Param("user_id") int userId);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT m FROM Meal m " +
            "WHERE m.user.id=:user_id AND m.dateTime BETWEEN :start_date AND :end_date ORDER BY m.dateTime DESC")
    List<Meal> getBetween(@Param("start_date") LocalDateTime startDate,
                          @Param("end_date") LocalDateTime endDate,
                          @Param("user_id") int userId);
    */
}
