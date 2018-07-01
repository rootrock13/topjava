<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal Update</title>
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

        h3 {
            font-family: Arial, sans-serif;
            color: #8e8e8e;
        }
    </style>
</head>
<body>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<h4><a style="font-family: Arial, sans-serif" href="index.html">home</a></h4>
<h3>${meal.id != -1 ? 'Update Meal' : 'Add Meal'}</h3>
<form method="POST" action='meals' name="frmAddMeal">
    <table class="tg">
        <c:if test="${meal.id != -1}">
            <tr>
                <td>Meal ID</td>
                <td><label><input type="text" readonly="readonly" name="mealId" value="${meal.id}"/></label></td>
            </tr>
        </c:if>
        <tr>
            <td>Date/Time</td>
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <td><label><input type="text" name="dateTime"
                              value="<fmt:formatDate pattern="yyyy.MM.dd HH:mm" value='${parsedDateTime}' />"/></label>
            </td>
        </tr>
        <tr>
            <td>Description</td>
            <td><label><input type="text" name="description" value="${meal.description}"/></label></td>
        </tr>
        <tr>
            <td>Calories</td>
            <td><label><input type="text" name="calories" value="${meal.calories}"/></label></td>
        </tr>
    </table>
    <br/>

    <input type="submit" value="${meal.id != -1 ? 'Save updates' : 'Save'}"/>
    <button type="reset" onclick="location.href = 'meals?action=list'">Cancel</button>
</form>
</body>
</html>
