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
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="panel conflict-panel">
        <div class="receipt-status warning-status">
            <div class="receipt-status-label">需要确认</div>
            <h1>个人资料修改冲突确认</h1>
        </div>

        <div class="conflict-body">
            <div class="warning-banner">该证件号码已被其他个人账户占用，请确认处理方式。</div>

            <div class="conflict-section">
                <h2>当前账户</h2>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">当前个人公积金账号</div>
                        <div class="detail-value strong"><c:out value="${conflict.currentPerAccNum}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">当前姓名</div>
                        <div class="detail-value"><c:out value="${conflict.currentPerName}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">修改后的姓名</div>
                        <div class="detail-value"><c:out value="${conflict.newPerName}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">修改后的证件号码</div>
                        <div class="detail-value strong"><c:out value="${conflict.newIdCard}"/></div>
                    </div>
                </div>
            </div>

            <div class="conflict-section">
                <h2>占用账户</h2>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">占用个人公积金账号</div>
                        <div class="detail-value strong"><c:out value="${conflict.occupiedPerAccNum}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">占用证件号码</div>
                        <div class="detail-value"><c:out value="${conflict.occupiedIdCard}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">占用姓名</div>
                        <div class="detail-value"><c:out value="${conflict.occupiedPerName}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">占用状态</div>
                        <div class="detail-value"><c:out value="${conflict.occupiedStatusText}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">占用单位公积金账号</div>
                        <div class="detail-value"><c:out value="${conflict.occupiedUnitAccNum}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">占用单位名称</div>
                        <div class="detail-value"><c:out value="${conflict.occupiedUnitName}"/></div>
                    </div>
                </div>
            </div>

            <div class="conflict-section">
                <h2>处理结果</h2>
                <p class="note">确认后，系统会新建错误账户保存占用账户原信息，错误账户证件号码为 9 + 原身份证号后 17 位；当前账户更新为正确证件号码。</p>
            </div>
        </div>

        <div class="actions receipt-actions conflict-actions">
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
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
