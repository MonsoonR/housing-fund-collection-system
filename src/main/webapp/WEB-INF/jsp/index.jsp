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
<div class="app-layout">
    <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />
    <div class="app-container">
        <jsp:include page="/WEB-INF/jsp/common/topbar.jsp" />
        <main class="main-content">
            <div class="worktable-summary">
                <h1>欢迎使用 ${systemName}</h1>
                <p>面向住房公积金筹集业务，提供高效、规范的柜面业务办理与配置管理服务。</p>
            </div>

            <section class="menu-group">
                <div class="menu-group-title">系统配置</div>
                <div class="menu-items">
                    <a href="${pageContext.request.contextPath}/params" class="menu-item-card">
                        <span class="menu-item-title">⚙️ 系统参数维护</span>
                        <span class="menu-item-desc">维护 TB001 系统参数，控制账号序号生成上限及各业务开关。</span>
                    </a>
                </div>
            </section>

            <section class="menu-group">
                <div class="menu-group-title">账户开户</div>
                <div class="menu-items">
                    <a href="${pageContext.request.contextPath}/units/open" class="menu-item-card">
                        <span class="menu-item-title">🏢 单位开户</span>
                        <span class="menu-item-desc">新增公积金缴存单位，维护缴存比例等，生成 12 位唯一单位公积金账号。</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/persons/open" class="menu-item-card">
                        <span class="menu-item-title">👤 个人开户</span>
                        <span class="menu-item-desc">新增公积金个人账户，关联对应单位基本资料，生成 12 位个人账号。</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/persons/open#import" class="menu-item-card">
                        <span class="menu-item-title">📊 Excel 批量开户</span>
                        <span class="menu-item-desc">通过 Excel 模版批量录入个人开户数据并进行导入，查看导入结果明细。</span>
                    </a>
                </div>
            </section>

            <section class="menu-group">
                <div class="menu-group-title">资料修改</div>
                <div class="menu-items">
                    <a href="${pageContext.request.contextPath}/units/edit" class="menu-item-card">
                        <span class="menu-item-title">📝 单位资料修改</span>
                        <span class="menu-item-desc">按单位公积金账号查询后修改联系电话、地址、发薪日、经办人等关键资料。</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/persons/edit" class="menu-item-card">
                        <span class="menu-item-title">✍️ 个人资料修改</span>
                        <span class="menu-item-desc">按个人账号查询并变更个人基本资料，支持身份证占用强制冲突确认。</span>
                    </a>
                </div>
            </section>

            <section class="menu-group">
                <div class="menu-group-title">信息查询</div>
                <div class="menu-items">
                    <a href="${pageContext.request.contextPath}/units/query" class="menu-item-card">
                        <span class="menu-item-title">🔍 单位信息查询</span>
                        <span class="menu-item-desc">按单位公积金账号或名称模糊查询，多维度展示单位及缴存概览。</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/persons/query" class="menu-item-card">
                        <span class="menu-item-title">🔎 个人信息查询</span>
                        <span class="menu-item-desc">按个人账号或证件号码精确查询个人信息，展示账户明细。</span>
                    </a>
                </div>
            </section>

            <p class="footer">系统运行环境建议：JDK 17/25，Tomcat 9，MySQL 9.5，集成 Java EE Web 标准。</p>
        </main>
    </div>
</div>
</body>
</html>
