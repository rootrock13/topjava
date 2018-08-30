<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <h2><spring:message code="${meal.isNew() ? 'meal.addTitle' : 'meal.editTitle'}"/></h2>
    <hr>

    <form:form method="post" modelAttribute="meal" action="${pageContext.request.contextPath}/meals/save">
        <!-- ID -->
        <form:hidden path="id"/>
        <!-- DateTime -->
        <dl>
            <dt><form:label path="dateTime">DateTime:</form:label></dt>
            <dd><form:input path="dateTime" type="datetime-local" required="required"/></dd>
        </dl>
        <dl>
            <dt><form:label path="description">Description:</form:label></dt>
            <dd><form:input path="description" size="40" required="required"/></dd>
        </dl>
        <dl>
            <dt><form:label path="calories">Calories:</form:label></dt>
            <dd><form:input path="calories" required="required"/></dd>
        </dl>

        <input type="submit" value=<spring:message code="common.save"/>>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>

    </form:form>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
