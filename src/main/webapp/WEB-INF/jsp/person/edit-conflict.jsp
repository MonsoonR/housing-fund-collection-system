<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料修改冲突确认</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
</head>
<body>
<main class="page">
    <section class="panel danger-zone">
        <h1>证件号码占用确认</h1>

        <div class="alert warning">该证件号码已被其他个人账户占用，是否强制变更？</div>

        <h2>当前修改信息</h2>
        <div class="receipt">
            <div class="row">
                <div class="label">当前个人公积金账号</div>
                <div class="value"><c:out value="${conflict.currentPerAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">当前姓名</div>
                <div class="value"><c:out value="${conflict.currentPerName}"/></div>
            </div>
            <div class="row">
                <div class="label">修改后的姓名</div>
                <div class="value"><c:out value="${conflict.newPerName}"/></div>
            </div>
            <div class="row">
                <div class="label">修改后的证件号码</div>
                <div class="value"><c:out value="${conflict.newIdCard}"/></div>
            </div>
        </div>

        <h2>占用账户信息</h2>
        <div class="receipt">
            <div class="row">
                <div class="label">占用个人公积金账号</div>
                <div class="value"><c:out value="${conflict.occupiedPerAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">占用证件号码</div>
                <div class="value"><c:out value="${conflict.occupiedIdCard}"/></div>
            </div>
            <div class="row">
                <div class="label">占用姓名</div>
                <div class="value"><c:out value="${conflict.occupiedPerName}"/></div>
            </div>
            <div class="row">
                <div class="label">占用状态</div>
                <div class="value"><c:out value="${conflict.occupiedStatusText}"/></div>
            </div>
            <div class="row">
                <div class="label">占用单位公积金账号</div>
                <div class="value"><c:out value="${conflict.occupiedUnitAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">占用单位名称</div>
                <div class="value"><c:out value="${conflict.occupiedUnitName}"/></div>
            </div>
        </div>

        <p class="note">确认后，系统会新建错误账户保存占用账户原信息，错误账户证件号码首位为 9；原占用账户证件号码首位释放为 8。</p>

        <div class="actions">
            <form method="post" action="${pageContext.request.contextPath}/persons/edit/force">
                <input type="hidden" name="perAccNum" value="${fn:escapeXml(personEditForm.perAccNum)}">
                <input type="hidden" name="unitAccNum" value="${fn:escapeXml(personEditForm.unitAccNum)}">
                <input type="hidden" name="unitName" value="${fn:escapeXml(personEditForm.unitName)}">
                <input type="hidden" name="perName" value="${fn:escapeXml(personEditForm.perName)}">
                <input type="hidden" name="idType" value="${fn:escapeXml(personEditForm.idType)}">
                <input type="hidden" name="idCard" value="${fn:escapeXml(personEditForm.idCard)}">
                <button class="button danger" type="submit">确认强制变更</button>
            </form>

            <form method="post" action="${pageContext.request.contextPath}/persons/edit/back">
                <input type="hidden" name="perAccNum" value="${fn:escapeXml(personEditForm.perAccNum)}">
                <input type="hidden" name="unitAccNum" value="${fn:escapeXml(personEditForm.unitAccNum)}">
                <input type="hidden" name="unitName" value="${fn:escapeXml(personEditForm.unitName)}">
                <input type="hidden" name="perName" value="${fn:escapeXml(personEditForm.perName)}">
                <input type="hidden" name="idType" value="${fn:escapeXml(personEditForm.idType)}">
                <input type="hidden" name="idCard" value="${fn:escapeXml(personEditForm.idCard)}">
                <button class="button secondary" type="submit">返回修改</button>
            </form>
        </div>
    </section>
</main>
</body>
</html>
