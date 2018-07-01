package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImplementation;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String LIST_MEAL = "/meals.jsp";
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final MealDao mealDao = MealDaoImplementation.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        action = action != null ? action : "list";
        String forward = "";

        if (action.equals("insert")) {
            request.setAttribute("meal", new Meal(-1, LocalDateTime.now(), "", 0));
            forward = INSERT_OR_EDIT;
        } else {
            if (action.equals("edit")) {
                forward = INSERT_OR_EDIT;
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = mealDao.getById(mealId);
                request.setAttribute("meal", meal);
                log.debug(String.format("update meal with id = %d", mealId));
                log.debug("forward to meal edit/insert page");

            } else {
                if (action.equals("delete")) {
                    int mealId = Integer.parseInt(request.getParameter("mealId"));
                    mealDao.delete(mealId);
                    log.debug(String.format("delete meal with id = %d", mealId));
                }
                forward = LIST_MEAL;
                List<MealWithExceed> mealWithExceeds = MealsUtil.getFilteredWithExceeded(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
                request.setAttribute("mealList", mealWithExceeds);
                log.debug("forward to mealList");
            }
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"), TimeUtil.formatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String stringMealID = request.getParameter("mealId");
        int mealId = -1;
        if (stringMealID != null) {
            mealId = Integer.parseInt(stringMealID);
            mealDao.update(new Meal(mealId, localDateTime, description, calories));
        } else {
            mealDao.add(new Meal(mealId, localDateTime, description, calories));
        }

        response.sendRedirect("meals?action=list");
    }
}
