<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人住房公积金开户回单</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />
    <div class="app-container">
        <jsp:include page="/WEB-INF/jsp/common/topbar.jsp" />
        <main class="main-content">
            <div class="page-header">
                <h1>业务回单</h1>
            </div>

            <div class="receipt-card">
                <div class="receipt-card-header">
                    <span>📄 个人公积金账户开户回单</span>
                </div>
                <div class="receipt-card-body">
                    <div class="receipt-status-banner">
                        <span>✓ 个人住房公积金账户建立成功，核缴计算通过</span>
                    </div>
                    <div class="receipt-card-title">个人住房公积金开户凭证</div>
                    
                    <div class="receipt-grid">
                        <div class="receipt-field">
                            <span class="receipt-label">个人公积金账号</span>
                            <span class="receipt-value highlight"><c:out value="${receipt.perAccNum}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">姓名</span>
                            <span class="receipt-value"><c:out value="${receipt.perName}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">证件号码</span>
                            <span class="receipt-value"><c:out value="${receipt.idCard}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">开户日期</span>
                            <span class="receipt-value"><c:out value="${receipt.createTime}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">所属单位账号</span>
                            <span class="receipt-value"><c:out value="${receipt.unitAccNum}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">所属单位名称</span>
                            <span class="receipt-value"><c:out value="${receipt.unitName}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">个人缴存基数</span>
                            <span class="receipt-value"><c:out value="${receipt.baseNum}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">单位/个人比例</span>
                            <span class="receipt-value"><c:out value="${receipt.unitRatio}"/> / <c:out value="${receipt.perRatio}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">单位月应缴额</span>
                            <span class="receipt-value"><c:out value="${receipt.unitMonthPay}"/> 元</span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">个人月应缴额</span>
                            <span class="receipt-value"><c:out value="${receipt.perMonthPay}"/> 元</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="actions">
                <a class="button" href="${pageContext.request.contextPath}/persons/open">继续开户</a>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </main>
    </div>
</div>
</body>
</html>
