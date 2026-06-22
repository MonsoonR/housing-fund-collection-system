<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料变更回单</title>
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
                    <span>📄 个人资料修改变更回单</span>
                </div>
                <div class="receipt-card-body">
                    <div class="receipt-status-banner">
                        <span>✓ 个人账户基本资料已修改完毕并与中心数据库同步</span>
                    </div>
                    <div class="receipt-card-title">个人账户资料变更凭证</div>
                    
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
                            <span class="receipt-label">变更时间</span>
                            <span class="receipt-value"><c:out value="${receipt.updateTime}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">所在单位公积金账号</span>
                            <span class="receipt-value"><c:out value="${receipt.unitAccNum}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">所在单位名称</span>
                            <span class="receipt-value"><c:out value="${receipt.unitName}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">是否执行强制变更</span>
                            <span class="receipt-value"><c:out value="${receipt.forceChanged ? '是 (强制剥离占用账户)' : '否'}"/></span>
                        </div>
                        <div class="receipt-field">
                            <span class="receipt-label">办理柜员</span>
                            <span class="receipt-value">柜面操作岗</span>
                        </div>

                        <c:if test="${receipt.forceChanged}">
                            <div class="receipt-field full" style="border-top:1px dashed var(--border-color); padding-top:10px; margin-top:10px;">
                                <span class="receipt-label" style="color:var(--warning); font-weight:700;">[强制变更详情]</span>
                            </div>
                            <div class="receipt-field">
                                <span class="receipt-label">原冲突个人账号</span>
                                <span class="receipt-value"><c:out value="${receipt.conflictPerAccNum}"/></span>
                            </div>
                            <div class="receipt-field">
                                <span class="receipt-label">原冲突账户原证件</span>
                                <span class="receipt-value"><c:out value="${receipt.originalConflictIdCard}"/></span>
                            </div>
                            <div class="receipt-field">
                                <span class="receipt-label">新建错误托管账号</span>
                                <span class="receipt-value"><c:out value="${receipt.wrongAccountPerAccNum}"/></span>
                            </div>
                            <div class="receipt-field">
                                <span class="receipt-label">冲突账号变更后新证件</span>
                                <span class="receipt-value"><c:out value="${receipt.changedConflictIdCard}"/></span>
                            </div>
                        </c:if>

                        <div class="receipt-field full">
                            <span class="receipt-label">柜面处理结果说明</span>
                            <span class="receipt-value"><c:out value="${receipt.resultMessage}"/></span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="actions">
                <a class="button" href="${pageContext.request.contextPath}/persons/edit">继续修改</a>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </main>
    </div>
</div>
</body>
</html>
