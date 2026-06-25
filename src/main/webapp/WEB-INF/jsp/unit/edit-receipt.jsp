<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>单位资料修改成功回单</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="panel receipt-panel">
        <div class="receipt-status">
            <div class="receipt-status-label">办理结果</div>
            <h1>单位资料修改办理成功</h1>
        </div>

        <div class="receipt-header">
            <div class="key-account">
                <div class="key-account-label">单位公积金账号</div>
                <div class="key-account-value"><c:out value="${receipt.unitAccNum}"/></div>
            </div>
        </div>

        <div class="receipt-body">
            <h2 class="receipt-section-title">修改信息</h2>
            <div class="detail-grid">
                <div class="detail-item full">
                    <div class="detail-label">单位名称</div>
                    <div class="detail-value strong"><c:out value="${receipt.unitName}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">组织机构代码</div>
                    <div class="detail-value"><c:out value="${receipt.orgCode}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">联系电话</div>
                    <div class="detail-value"><c:out value="${receipt.phone}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">单位经办人</div>
                    <div class="detail-value"><c:out value="${receipt.agentName}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">修改结果</div>
                    <div class="detail-value strong"><c:out value="${receipt.resultMessage}"/></div>
                </div>
                <div class="detail-item full">
                    <div class="detail-label">变更时间</div>
                    <div class="detail-value"><c:out value="${receipt.updateTime}"/></div>
                </div>
            </div>
        </div>

        <div class="actions receipt-actions">
            <a class="button" href="${pageContext.request.contextPath}/units/edit">继续修改</a>
            <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
        </div>
    </section>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
