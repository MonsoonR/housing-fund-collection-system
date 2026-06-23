<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料修改成功回单</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="panel receipt-panel">
        <div class="receipt-header">
            <div class="success-banner">办理成功</div>
            <h1>个人资料修改成功回单</h1>
            <p class="page-desc">个人资料修改已完成，请核对回单信息。</p>
            <div class="key-account">
                <div class="key-account-label">个人公积金账号</div>
                <div class="key-account-value"><c:out value="${receipt.perAccNum}"/></div>
            </div>
        </div>

        <div class="receipt-body">
            <div class="detail-grid">
                <div class="detail-item">
                    <div class="detail-label">姓名</div>
                    <div class="detail-value strong"><c:out value="${receipt.perName}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">证件号码</div>
                    <div class="detail-value"><c:out value="${receipt.idCard}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">单位公积金账号</div>
                    <div class="detail-value"><c:out value="${receipt.unitAccNum}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">单位名称</div>
                    <div class="detail-value"><c:out value="${receipt.unitName}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">是否执行强制变更</div>
                    <div class="detail-value"><c:out value="${receipt.forceChanged ? '是' : '否'}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">修改结果</div>
                    <div class="detail-value strong"><c:out value="${receipt.resultMessage}"/></div>
                </div>
                <c:if test="${receipt.forceChanged}">
                    <div class="detail-item">
                        <div class="detail-label">被占用个人公积金账号</div>
                        <div class="detail-value"><c:out value="${receipt.conflictPerAccNum}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">新建错误账户账号</div>
                        <div class="detail-value"><c:out value="${receipt.wrongAccountPerAccNum}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">原证件号码</div>
                        <div class="detail-value"><c:out value="${receipt.originalConflictIdCard}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">变更后的错误证件号码</div>
                        <div class="detail-value"><c:out value="${receipt.changedConflictIdCard}"/></div>
                    </div>
                </c:if>
                <div class="detail-item full">
                    <div class="detail-label">变更时间</div>
                    <div class="detail-value"><c:out value="${receipt.updateTime}"/></div>
                </div>
            </div>
        </div>

        <div class="actions receipt-actions">
            <a class="button" href="${pageContext.request.contextPath}/persons/edit">继续修改</a>
            <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
        </div>
    </section>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
