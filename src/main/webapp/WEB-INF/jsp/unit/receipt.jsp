<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>单位开户回单</title>
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
                    <span>📄 住房公积金单位开户回单</span>
                </div>
                <div class="receipt-card-body">
                    <div class="receipt-status-banner">
                        <span>✓ 单位公积金账户已开户成功，缴存关系建立完成</span>
                    </div>
                    <div class="receipt-card-title">住房公积金单位开户凭证</div>
                    
                    <div class="receipt-grid">
                        <div class="receipt-field">
                            <span class="receipt-label">单位公积金账号</span>
                            <span class="receipt-value highlight"><c:out value="${receipt.unitAccNum}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">单位名称</span>
                            <span class="receipt-value"><c:out value="${receipt.unitName}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">组织机构代码</span>
                            <span class="receipt-value"><c:out value="${receipt.orgCode}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">开户日期</span>
                            <span class="receipt-value"><c:out value="${receipt.createDate}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">单位缴存比例</span>
                            <span class="receipt-value"><c:out value="${receipt.unitRatio}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">个人缴存比例</span>
                            <span class="receipt-value"><c:out value="${receipt.perRatio}"/></span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="actions">
                <a class="button" href="${pageContext.request.contextPath}/units/open">继续开户</a>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </main>
    </div>
</div>
</body>
</html>
