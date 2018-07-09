<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
        .tg {
            border-collapse: collapse;
            border-spacing: 0;
            border-color: #ccc;
        }
        .tg td {
            font-family: Arial, sans-serif;
            font-size: 14px;
            padding: 10px 5px;
            border-style: solid;
            border-width: 0px;
            overflow: hidden;
            word-break: normal;
            border-color: #ccc;
            background-color: #fff;
        }
        .tg th {
            font-family: Arial, sans-serif;
            font-size: 14px;
            font-weight: normal;
            padding: 10px 5px;
            border-style: solid;
            border-width: 0px;
            overflow: hidden;
            word-break: normal;
            border-color: #ccc;
            color: #333;
            background-color: #f0f0f0;
        }
        .tg .tg-4eph {
            background-color: #f9f9f9
        }
        a {
            text-decoration: none;
            font-family: Arial, sans-serif
        }
    </style>
</head>
<body>
<section>
    <a href="index.html">logout</a>
    <h2>Meals</h2>
    <c:set var="filterParameters" value="fromDate=${fromDate}&toDate=${toDate}&fromTime=${fromTime}&toTime=${toTime}"/>
    <a href="meals?action=create&${filterParameters}">Add Meal</a>
    <hr/>
    <form method="GET" action='meals' name="filterForm">
        <table class="tg">
            <tr>
                <td>From date</td>
                <td><input type="date" name="fromDate" value="${fromDate}"/></td>
                <td>From time</td>
                <td><input type="time" name="fromTime" value="${fromTime}"/></td>
            </tr>
            <tr>
                <td>To date</td>
                <td><input type="date" name="toDate" value="${toDate}"/></td>
                <td>To time</td>
                <td><input type="time" name="toTime" value="${toTime}"/></td>
            </tr>
        </table>
        <br/>

        <input type="submit" value="Filter"/>
        <button type="reset" onclick="location.href = 'meals'">Cancel</button>
    </form>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}&${filterParameters}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}&${filterParameters}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>