package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static ConfigurableApplicationContext springContext;
    private static MealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = springContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        springContext.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (meal.isNew()) {
            log.info("Create {}", meal);
            controller.create(meal);
        } else {
            log.info("Update {}", meal);
            controller.update(meal);
        }
        response.sendRedirect("meals" + getFiltering(request));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // filtering
        LocalDate fromDate = DateTimeUtil.toLocalDate(request.getParameter("fromDate"));
        LocalDate toDate = DateTimeUtil.toLocalDate(request.getParameter("toDate"));
        LocalTime fromTime = DateTimeUtil.toLocalTime(request.getParameter("fromTime"));
        LocalTime toTime = DateTimeUtil.toLocalTime(request.getParameter("toTime"));
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        request.setAttribute("fromTime", fromTime);
        request.setAttribute("toTime", toTime);

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals" + getFiltering(request));
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                if (fromDate == null && toDate == null && fromTime == null && toTime == null) {
                    log.info("getAll");
                    request.setAttribute("meals", controller.getAll());
                } else {
                    log.info("getBetweenDateAndTime {} {}, {} {}", fromDate, toDate, fromTime, toTime);
                    request.setAttribute("meals", controller.getBetweenDateAndTime(fromDate, toDate, fromTime, toTime));
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private String getFiltering(HttpServletRequest request) {
        return String.format("?fromDate=%s&toDate=%s&fromTime=%s&toTime=%s",
                request.getParameter("fromDate"),
                request.getParameter("toDate"),
                request.getParameter("fromTime"),
                request.getParameter("toTime"));
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
