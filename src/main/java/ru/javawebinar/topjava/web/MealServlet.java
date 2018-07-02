package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoWithHardCodedDB;
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
    private static final MealDao mealDao = new MealDaoWithHardCodedDB();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        action = action != null ? action : "list";
        String forward = "";

        switch (action) {
            case "list":
                log.debug("action:list");
                forward = LIST_MEAL;
                List<MealWithExceed> mealWithExceeds = MealsUtil.getFilteredWithExceeded(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
                request.setAttribute("mealList", mealWithExceeds);
                break;
            case "insert":
                log.debug("action:insert");
                forward = INSERT_OR_EDIT;
                request.setAttribute("meal", new Meal(-1, LocalDateTime.now(), "", 0));
                break;
            case "edit":
                log.debug(String.format("action:update meal with id = %s - forward to meal edit/insert page", request.getParameter("mealId")));
                forward = INSERT_OR_EDIT;
                Meal meal = mealDao.getById(Integer.parseInt(request.getParameter("mealId")));
                request.setAttribute("meal", meal);
                break;
            case "delete":
                log.debug(String.format("action:delete meal with id = %s", request.getParameter("mealId")));
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                mealDao.delete(mealId);
                log.debug("redirected to mealList");
                response.sendRedirect("meals");
                break;
        }

        if (!action.equals("delete")) {
            log.debug(String.format("forward to %s", forward));
            request.getRequestDispatcher(forward).forward(request, response);
        }
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
            log.debug(String.format("save updated meal with id = %d", mealId));
            mealDao.update(new Meal(mealId, localDateTime, description, calories));
        } else {
            log.debug("save new meal");
            mealDao.add(new Meal(mealId, localDateTime, description, calories));
        }

        log.debug("redirected to mealList");
        response.sendRedirect("meals");
    }
}
