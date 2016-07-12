<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link href="<spring:url value="/resources/css/main.css"/>" rel="stylesheet">
</head>
<body>
<div id="payback-form">
    <h2>Enter transaction data:</h2>
    <spring:url value="/payback/confirm" var="formUrl"/>
    <form:form method="POST"
               action="${formUrl}"
               commandName="purchaseForm">
        <%--TODO #1 add errors field for 'creditCardNumber' field--%>
        <form:label path="creditCardNumber">Credit card number:</form:label>
        <form:input path="creditCardNumber"/>
        <%--TODO #2 add errors field for 'merchantNumber' field--%>
        <form:label path="merchantNumber">Merchant number:</form:label>
        <form:input path="merchantNumber"/>
        <%--TODO #3 add errors field for 'transactionValue' field--%>
        <form:label path="transactionValue">Transaction value:</form:label>
        <form:input path="transactionValue"/>

        <input type="submit" value="Get Payback!" id="submit" />
    </form:form>
</div>
</body>
</html>
