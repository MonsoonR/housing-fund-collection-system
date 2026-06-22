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
    <section class="panel">
        <h1>单位资料修改成功回单</h1>

        <div class="receipt">
            <div class="row">
                <div class="label">单位公积金账号</div>
                <div class="value"><c:out value="${receipt.unitAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">单位名称</div>
                <div class="value"><c:out value="${receipt.unitName}"/></div>
            </div>
            <div class="row">
                <div class="label">组织机构代码</div>
                <div class="value"><c:out value="${receipt.orgCode}"/></div>
            </div>
            <div class="row">
                <div class="label">联系电话</div>
                <div class="value"><c:out value="${receipt.phone}"/></div>
            </div>
            <div class="row">
                <div class="label">单位经办人</div>
                <div class="value"><c:out value="${receipt.agentName}"/></div>
            </div>
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
            <a class="button" href="${pageContext.request.contextPath}/units/edit">继续修改</a>
            <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
        </div>
    </section>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
