<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="app-shell">
    <header class="app-topbar">
        <a class="app-title" href="${pageContext.request.contextPath}/index">住房公积金归集业务系统</a>
    </header>
    <div class="app-body">
        <nav class="app-nav" aria-label="业务导航">
            <a href="${pageContext.request.contextPath}/params">系统参数维护</a>
            <a href="${pageContext.request.contextPath}/units/open">单位开户</a>
            <a href="${pageContext.request.contextPath}/persons/open">个人开户</a>
            <a href="${pageContext.request.contextPath}/units/edit">单位资料修改</a>
            <a href="${pageContext.request.contextPath}/persons/edit">个人资料修改</a>
            <a href="${pageContext.request.contextPath}/units/query">单位信息查询</a>
            <a href="${pageContext.request.contextPath}/persons/query">个人信息查询</a>
        </nav>
        <main class="main-content">
