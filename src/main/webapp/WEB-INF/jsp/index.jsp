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
    <section class="home-hero">
        <h1>住房公积金归集业务系统</h1>
        <p>提供系统参数维护、账户开户、资料修改和信息查询等归集业务办理。</p>
    </section>

    <div class="home-groups" aria-label="业务入口">
        <section class="module-group">
            <div class="module-group-title">
                <h2>系统配置</h2>
            </div>
            <div class="module-items">
                <div class="module-item">
                    <div class="module-name">系统参数维护</div>
                    <div class="module-desc">维护账号序号、参数说明和备用信息。</div>
                    <div class="object-tag">TB001</div>
                    <div class="module-action"><a class="button small secondary" href="${pageContext.request.contextPath}/params">进入</a></div>
                </div>
            </div>
        </section>

        <section class="module-group">
            <div class="module-group-title">
                <h2>账户业务</h2>
            </div>
            <div class="module-items">
                <div class="module-item">
                    <div class="module-name">单位开户</div>
                    <div class="module-desc">录入缴存单位资料，生成单位公积金账号。</div>
                    <div class="object-tag">TB002</div>
                    <div class="module-action"><a class="button small secondary" href="${pageContext.request.contextPath}/units/open">进入</a></div>
                </div>
                <div class="module-item">
                    <div class="module-name">个人开户</div>
                    <div class="module-desc">包含手工开户与 Excel 批量开户。</div>
                    <div class="object-tag">TB003</div>
                    <div class="module-action"><a class="button small secondary" href="${pageContext.request.contextPath}/persons/open">进入</a></div>
                </div>
            </div>
        </section>

        <section class="module-group">
            <div class="module-group-title">
                <h2>资料与查询</h2>
            </div>
            <div class="module-items">
                <div class="module-item">
                    <div class="module-name">单位资料修改</div>
                    <div class="module-desc">查询单位账号并维护单位基础资料。</div>
                    <div class="object-tag">TB002</div>
                    <div class="module-action"><a class="button small secondary" href="${pageContext.request.contextPath}/units/edit">进入</a></div>
                </div>
                <div class="module-item">
                    <div class="module-name">个人资料修改</div>
                    <div class="module-desc">维护个人姓名、证件类型和证件号码。</div>
                    <div class="object-tag">TB003</div>
                    <div class="module-action"><a class="button small secondary" href="${pageContext.request.contextPath}/persons/edit">进入</a></div>
                </div>
                <div class="module-item">
                    <div class="module-name">单位信息查询</div>
                    <div class="module-desc">按单位账号或名称查询单位开户信息。</div>
                    <div class="object-tag">TB002</div>
                    <div class="module-action"><a class="button small secondary" href="${pageContext.request.contextPath}/units/query">进入</a></div>
                </div>
                <div class="module-item">
                    <div class="module-name">个人信息查询</div>
                    <div class="module-desc">按个人账号或证件号码查询个人账户信息。</div>
                    <div class="object-tag">TB003</div>
                    <div class="module-action"><a class="button small secondary" href="${pageContext.request.contextPath}/persons/query">进入</a></div>
                </div>
            </div>
        </section>
    </div>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
