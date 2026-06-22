<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="uri" value="${pageContext.request.requestURI}" />
<div class="app-sidebar">
    <div class="sidebar-brand">
        <a href="${pageContext.request.contextPath}/index" class="brand-link">
            <span class="brand-logo">🏦</span>
            <span class="brand-text">公积金筹集系统</span>
        </a>
    </div>
    <ul class="sidebar-menu">
        <li class="menu-header">系统配置</li>
        <li class="${fn:contains(uri, '/params') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/params">
                <span class="menu-icon">⚙️</span>
                <span class="menu-text">系统参数维护</span>
            </a>
        </li>
        <li class="menu-header">业务办理</li>
        <li class="${fn:contains(uri, '/units/open') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/units/open">
                <span class="menu-icon">🏢</span>
                <span class="menu-text">单位开户</span>
            </a>
        </li>
        <li class="${fn:contains(uri, '/persons/open') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/persons/open">
                <span class="menu-icon">👤</span>
                <span class="menu-text">个人开户</span>
            </a>
        </li>
        <li class="menu-header">资料修改</li>
        <li class="${fn:contains(uri, '/units/edit') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/units/edit">
                <span class="menu-icon">📝</span>
                <span class="menu-text">单位资料修改</span>
            </a>
        </li>
        <li class="${fn:contains(uri, '/persons/edit') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/persons/edit">
                <span class="menu-icon">✍️</span>
                <span class="menu-text">个人资料修改</span>
            </a>
        </li>
        <li class="menu-header">信息查询</li>
        <li class="${fn:contains(uri, '/units/query') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/units/query">
                <span class="menu-icon">🔍</span>
                <span class="menu-text">单位信息查询</span>
            </a>
        </li>
        <li class="${fn:contains(uri, '/persons/query') ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/persons/query">
                <span class="menu-icon">🔎</span>
                <span class="menu-text">个人信息查询</span>
            </a>
        </li>
    </ul>
</div>
