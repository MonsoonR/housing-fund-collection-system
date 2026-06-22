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
<main class="page">
    <section class="header">
        <h1>住房公积金归集业务系统</h1>
        <p>面向课程设计验收的归集业务子系统，集中提供系统参数维护、账户开户、资料变更与信息查询服务。</p>
    </section>

    <section class="modules" aria-label="功能模块">
        <section class="module-group">
            <h2>参数维护</h2>
            <div class="module-list">
                <article class="module">
                    <strong><a href="${pageContext.request.contextPath}/params">系统参数维护</a></strong>
                    <span>维护 TB001 系统参数，管理单位与个人账号序号。</span>
                </article>
            </div>
        </section>

        <section class="module-group">
            <h2>开户业务</h2>
            <div class="module-list">
                <article class="module">
                    <strong><a href="${pageContext.request.contextPath}/units/open">单位开户</a></strong>
                    <span>录入缴存单位资料，生成 12 位单位公积金账号。</span>
                </article>
                <article class="module">
                    <strong><a href="${pageContext.request.contextPath}/persons/open">个人开户</a></strong>
                    <span>查询单位后录入个人资料，生成个人公积金账号。</span>
                </article>
                <article class="module">
                    <strong><a href="${pageContext.request.contextPath}/persons/open#import">Excel 批量开户</a></strong>
                    <span>按固定列顺序导入个人开户数据并显示成功、失败结果。</span>
                </article>
            </div>
        </section>

        <section class="module-group">
            <h2>资料修改</h2>
            <div class="module-list">
                <article class="module">
                    <strong><a href="${pageContext.request.contextPath}/units/edit">单位资料修改</a></strong>
                    <span>按单位公积金账号查询后维护单位基本资料。</span>
                </article>
                <article class="module">
                    <strong><a href="${pageContext.request.contextPath}/persons/edit">个人资料修改</a></strong>
                    <span>按个人公积金账号查询后维护姓名和证件信息。</span>
                </article>
            </div>
        </section>

        <section class="module-group">
            <h2>信息查询</h2>
            <div class="module-list">
                <article class="module">
                    <strong><a href="${pageContext.request.contextPath}/units/query">单位信息查询</a></strong>
                    <span>按单位账号或单位名称查询缴存单位账户情况。</span>
                </article>
                <article class="module">
                    <strong><a href="${pageContext.request.contextPath}/persons/query">个人信息查询</a></strong>
                    <span>按个人账号或证件号码查询个人账户明细。</span>
                </article>
            </div>
        </section>
    </section>

    <p class="footer">运行环境建议：JDK 25 构建，Java 17 兼容字节码，Tomcat 9，MySQL 9.5。</p>
</main>
</body>
</html>
