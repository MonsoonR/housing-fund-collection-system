<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="app-shell">
    <header class="app-header">
        <a class="app-brand" href="${pageContext.request.contextPath}/index">住房公积金归集业务系统</a>
        <a class="app-home-link" href="${pageContext.request.contextPath}/index">返回首页</a>
    </header>
    <c:set var="currentPath" value="${pageContext.request.requestURI}${pageContext.request.servletPath}"/>
    <div class="app-layout">
        <nav class="app-sidebar" aria-label="业务导航">
            <div class="sidebar-title">业务模块</div>
            <a class="nav-link ${(fn:contains(currentPath, '/params') or fn:contains(currentPath, '/param/')) ? 'is-active' : ''}"
               href="${pageContext.request.contextPath}/params">系统参数维护</a>
            <a class="nav-link ${(fn:contains(currentPath, '/units/open') or fn:contains(currentPath, '/unit/open')) ? 'is-active' : ''}"
               href="${pageContext.request.contextPath}/units/open">单位开户</a>
            <a class="nav-link ${(fn:contains(currentPath, '/persons/open') or fn:contains(currentPath, '/person/open')) ? 'is-active' : ''}"
               href="${pageContext.request.contextPath}/persons/open">个人开户</a>
            <a class="nav-link ${(fn:contains(currentPath, '/units/edit') or fn:contains(currentPath, '/unit/edit')) ? 'is-active' : ''}"
               href="${pageContext.request.contextPath}/units/edit">单位资料修改</a>
            <a class="nav-link ${(fn:contains(currentPath, '/persons/edit') or fn:contains(currentPath, '/person/edit')) ? 'is-active' : ''}"
               href="${pageContext.request.contextPath}/persons/edit">个人资料修改</a>
            <a class="nav-link ${(fn:contains(currentPath, '/units/query') or fn:contains(currentPath, '/unit/query')) ? 'is-active' : ''}"
               href="${pageContext.request.contextPath}/units/query">单位信息查询</a>
            <a class="nav-link ${(fn:contains(currentPath, '/persons/query') or fn:contains(currentPath, '/person/query')) ? 'is-active' : ''}"
               href="${pageContext.request.contextPath}/persons/query">个人信息查询</a>
        </nav>
        <main class="main-content">
