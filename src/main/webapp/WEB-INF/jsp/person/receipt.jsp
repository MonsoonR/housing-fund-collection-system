<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人开户成功回单</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="panel">
        <h1>个人开户成功回单</h1>
        <p class="page-desc">办理结果已生成，请核对个人账号、姓名和证件号码。</p>

        <div class="receipt">
            <div class="row">
                <div class="label">个人公积金账号</div>
                <div class="value"><c:out value="${receipt.perAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">姓名</div>
                <div class="value"><c:out value="${receipt.perName}"/></div>
            </div>
            <div class="row">
                <div class="label">证件号码</div>
                <div class="value"><c:out value="${receipt.idCard}"/></div>
            </div>
            <div class="row">
                <div class="label">单位公积金账号</div>
                <div class="value"><c:out value="${receipt.unitAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">单位名称</div>
                <div class="value"><c:out value="${receipt.unitName}"/></div>
            </div>
            <div class="row">
                <div class="label">缴存基数</div>
                <div class="value"><c:out value="${receipt.baseNum}"/></div>
            </div>
            <div class="row">
                <div class="label">单位比例</div>
                <div class="value"><c:out value="${receipt.unitRatio}"/></div>
            </div>
            <div class="row">
                <div class="label">个人比例</div>
                <div class="value"><c:out value="${receipt.perRatio}"/></div>
            </div>
            <div class="row">
                <div class="label">单位月缴额</div>
                <div class="value"><c:out value="${receipt.unitMonthPay}"/></div>
            </div>
            <div class="row">
                <div class="label">个人月缴额</div>
                <div class="value"><c:out value="${receipt.perMonthPay}"/></div>
            </div>
            <div class="row">
                <div class="label">开户日期</div>
                <div class="value"><c:out value="${receipt.createTime}"/></div>
            </div>
        </div>

        <div class="actions">
            <a class="button" href="${pageContext.request.contextPath}/persons/open">继续开户</a>
            <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
        </div>
    </section>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
