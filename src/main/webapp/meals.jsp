<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <style type="text/css">
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
            border-width: 1px;
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
            border-width: 1px;
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

        h3 {
            font-family: Arial, sans-serif;
            color: #8e8e8e;
        }
    </style>
</head>
<body>
<h4><a href="index.html">home</a></h4>
<h3>MEAL LIST</h3>
<table class="tg">
    <tr>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Actions</th>
    </tr>
    <jsp:useBean id="mealList" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealWithExceed>"/>
    <c:forEach var="currentMeal" items="${mealList}">
        <tr style="color: ${currentMeal.exceed ? '#ff0000' : '#14AA0F'}">
            <td>
                <fmt:parseDate value="${ currentMeal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                               type="both"/>
                <fmt:formatDate pattern="yyyy.MM.dd HH:mm" value='${parsedDateTime}'/>
            </td>
            <td>${currentMeal.description}</td>
            <td>${currentMeal.calories}</td>
            <td><a href="meals?action=edit&mealId=${currentMeal.id}">update</a></td>
            <td><a href="meals?action=delete&mealId=${currentMeal.id}">delete</a></td>
        </tr>
    </c:forEach>
</table>
<br/>
<button onclick="location.href = 'meals?action=insert'">Add Meal</button>
<!--<p><a href="meals?action=insert">Add Meal</a></p>-->
</body>
</html>
