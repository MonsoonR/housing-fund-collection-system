<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${systemName}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
</head>
<body>
<main class="page">
    <section class="header">
        <h1>${systemName}</h1>
        <p>面向住房公积金归集业务，提供账户开户、资料变更、系统参数维护与信息查询服务。</p>
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
            <span>新增个人账户并生成个人公积金账号</span>
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

    <p class="footer">运行环境建议：JDK 25 构建，Java 17 兼容字节码，Tomcat 9，MySQL 9.5。</p>
</main>
</body>
</html>
