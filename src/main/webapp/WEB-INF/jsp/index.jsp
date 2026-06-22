<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>住房公积金归集业务系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="header">
        <h1>住房公积金归集业务系统</h1>
        <p>面向课程设计任务书中的归集业务，提供参数维护、开户、资料修改和信息查询。</p>
    </section>

    <section class="modules" aria-label="功能模块">
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/params">系统参数维护</a></strong>
            <span>维护 TB001 系统参数</span>
        </article>
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/units/open">单位开户</a></strong>
            <span>新增缴存单位并生成单位公积金账号</span>
        </article>
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/persons/open">个人开户</a></strong>
            <span>支持个人手工开户和 Excel 批量开户</span>
        </article>
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/units/edit">单位资料修改</a></strong>
            <span>按单位公积金账号查询并修改单位资料</span>
        </article>
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/persons/edit">个人资料修改</a></strong>
            <span>按个人公积金账号查询并修改个人资料</span>
        </article>
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/units/query">单位信息查询</a></strong>
            <span>按单位公积金账号或单位名称查询单位信息</span>
        </article>
        <article class="module">
            <strong><a href="${pageContext.request.contextPath}/persons/query">个人信息查询</a></strong>
            <span>按个人公积金账号或证件号码查询个人信息</span>
        </article>
    </section>

    <p class="footer">左侧导航按任务书业务模块组织，适合验收演示和报告截图。</p>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
