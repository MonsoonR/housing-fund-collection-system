<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人信息查询</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="page-heading">
        <div class="page-heading-main">
            <h1>个人信息查询</h1>
            <p class="page-desc">按个人公积金账号或证件号码查询个人账户信息。</p>
        </div>
        <div class="page-heading-actions">
            <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
        </div>
    </section>

    <c:if test="${not empty error}">
        <div class="alert wide-panel"><c:out value="${error}"/></div>
    </c:if>

    <form class="list-toolbar" method="post" action="${pageContext.request.contextPath}/persons/query"
          onsubmit="return validatePersonQueryForm(this);">
        <div class="toolbar-left search">
            <label for="perAccNum">个人公积金账号</label>
            <input id="perAccNum" name="perAccNum" type="text" maxlength="12"
                   pattern="[0-9]{12}" value="${fn:escapeXml(personQueryForm.perAccNum)}">

            <label for="idCard">证件号码</label>
            <input id="idCard" name="idCard" type="text" maxlength="18"
                   pattern="[0-9]{17}[0-9Xx]" value="${fn:escapeXml(personQueryForm.idCard)}">

            <button class="button" type="submit">查询</button>
        </div>
        <div class="toolbar-right">
            <span class="section-note">账号优先精确查询，未输入账号时按证件号码查询。</span>
        </div>
    </form>

    <c:if test="${searched and empty queryResult}">
        <div class="empty wide-panel">未查询到数据</div>
    </c:if>

    <c:if test="${not empty queryResult}">
        <section class="result-card">
            <div class="panel-heading">
                <h2>查询结果</h2>
                <p class="page-desc">个人账户与缴存信息。</p>
            </div>
            <div class="detail-grid">
                <div class="detail-item">
                    <div class="detail-label">缴存单位全称</div>
                    <div class="detail-value"><c:out value="${queryResult.unitName}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">缴存单位账号</div>
                    <div class="detail-value"><c:out value="${queryResult.unitAccNum}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">姓名</div>
                    <div class="detail-value strong"><c:out value="${queryResult.perName}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">个人账号</div>
                    <div class="detail-value strong"><c:out value="${queryResult.perAccNum}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">证件号码</div>
                    <div class="detail-value"><c:out value="${queryResult.idCard}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">余额</div>
                    <div class="detail-value num"><c:out value="${queryResult.perBalance}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">开户日期</div>
                    <div class="detail-value"><c:out value="${queryResult.createTime}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">最后汇缴月</div>
                    <div class="detail-value"><c:out value="${queryResult.lastPayMonth}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">缴存比例（单位）</div>
                    <div class="detail-value num"><c:out value="${queryResult.unitRatio}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">缴存比例（个人）</div>
                    <div class="detail-value num"><c:out value="${queryResult.perRatio}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">缴存比例（合计）</div>
                    <div class="detail-value num"><c:out value="${queryResult.totalRatio}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">单位月汇缴金额</div>
                    <div class="detail-value num"><c:out value="${queryResult.unitMonthPay}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">个人月汇缴金额</div>
                    <div class="detail-value num"><c:out value="${queryResult.perMonthPay}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">合计月汇缴金额</div>
                    <div class="detail-value num"><c:out value="${queryResult.totalMonthPay}"/></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">个人账户状态</div>
                    <div class="detail-value"><span class="status-label"><c:out value="${queryResult.statusText}"/></span></div>
                </div>
            </div>
        </section>
    </c:if>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
