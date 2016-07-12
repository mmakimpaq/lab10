<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="<spring:url value="/resources/css/main.css"/>" rel="stylesheet">
</head>
<body>
<table style="border: 1px black solid">
    <caption><spring:message code="paybacks.label"/> </caption>
    <thead>
    <tr>
        <th></th>
        <th><spring:message code="number.label"/></th>
        <th><spring:message code="accountName.label"/></th>
        <th><spring:message code="mechantName.label"/></th>
        <th><spring:message code="amount.label"/></th>
        <th><spring:message code="date.label"/></th>
    </tr>
    </thead>
    <tbody>
       <c:forEach items="${paybacks}" var="payback" varStatus="counter">
           <tr>
               <td>${counter.count}</td>
               <td>${payback.number}</td>
               <td>${payback.account.name}</td>
               <td>${payback.merchant.name}</td>
               <td>${payback.amount}</td>
               <!-- TODO #1 add SpEL evalutions for `date` field -->
               <td><spring:eval expression="payback.date"/></td>
           </tr>
       </c:forEach>
    </tbody>

    <a href="<spring:url value='/payback/new'/>">Add new payback</a>
</table>
</body>
</html>
