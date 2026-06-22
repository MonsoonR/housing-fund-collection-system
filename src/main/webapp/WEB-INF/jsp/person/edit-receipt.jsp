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
    <section class="panel">
        <h1>个人资料修改成功回单</h1>

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
                <div class="label">是否执行强制变更</div>
                <div class="value"><c:out value="${receipt.forceChanged ? '是' : '否'}"/></div>
            </div>
            <c:if test="${receipt.forceChanged}">
                <div class="row">
                    <div class="label">被占用个人公积金账号</div>
                    <div class="value"><c:out value="${receipt.conflictPerAccNum}"/></div>
                </div>
                <div class="row">
                    <div class="label">新建错误账户账号</div>
                    <div class="value"><c:out value="${receipt.wrongAccountPerAccNum}"/></div>
                </div>
                <div class="row">
                    <div class="label">原证件号码</div>
                    <div class="value"><c:out value="${receipt.originalConflictIdCard}"/></div>
                </div>
                <div class="row">
                    <div class="label">变更后的错误证件号码</div>
                    <div class="value"><c:out value="${receipt.changedConflictIdCard}"/></div>
                </div>
            </c:if>
            <div class="row">
                <div class="label">修改结果</div>
                <div class="value"><c:out value="${receipt.resultMessage}"/></div>
            </div>
            <div class="row">
                <div class="label">变更时间</div>
                <div class="value"><c:out value="${receipt.updateTime}"/></div>
            </div>
        </div>

        <div class="actions">
            <a class="button" href="${pageContext.request.contextPath}/persons/edit">继续修改</a>
            <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
        </div>
    </section>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
