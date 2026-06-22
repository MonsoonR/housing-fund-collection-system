<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>单位资料变更回单</title>
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
                    <span>📄 单位资料修改变更回单</span>
                </div>
                <div class="receipt-card-body">
                    <div class="receipt-status-banner">
                        <span>✓ 单位资料已成功变更并完成入库校验</span>
                    </div>
                    <div class="receipt-card-title">住房公积金账户资料变更凭证</div>
                    
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
                            <span class="receipt-label">联系电话</span>
                            <span class="receipt-value"><c:out value="${receipt.phone}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">单位经办人</span>
                            <span class="receipt-value"><c:out value="${receipt.agentName}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">变更时间</span>
                            <span class="receipt-value"><c:out value="${receipt.updateTime}"/></span>
                        </div>
                        <div class="receipt-field full">
                            <span class="receipt-label">修改结果说明</span>
                            <span class="receipt-value"><c:out value="${receipt.resultMessage}"/></span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="actions">
                <a class="button" href="${pageContext.request.contextPath}/units/edit">继续修改</a>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </main>
    </div>
</div>
</body>
</html>
