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
<div class="app-layout">
    <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />
    <div class="app-container">
        <jsp:include page="/WEB-INF/jsp/common/topbar.jsp" />
        <main class="main-content">
            <div class="page-header">
                <h1>个人信息查询</h1>
            </div>

            <section class="panel">
                <c:if test="${not empty error}">
                    <div class="alert error"><c:out value="${error}"/></div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/persons/query"
                      onsubmit="return validatePersonQueryForm(this);">
                    <div class="grid">
                        <div class="field">
                            <label for="perAccNum">个人公积金账号</label>
                            <input id="perAccNum" name="perAccNum" type="text" maxlength="12"
                                   pattern="[0-9]{12}" value="${fn:escapeXml(personQueryForm.perAccNum)}">
                            <div class="tip">优先输入 12 位个人账号进行精确匹配。</div>
                        </div>

                        <div class="field">
                            <label for="idCard">证件号码</label>
                            <input id="idCard" name="idCard" type="text" maxlength="18"
                                   pattern="[0-9]{17}[0-9Xx]" value="${fn:escapeXml(personQueryForm.idCard)}">
                            <div class="tip">当未输入个人账号时，按 18 位身份证号码进行检索。</div>
                        </div>
                    </div>

                    <div class="actions">
                        <button class="button" type="submit">查询</button>
                        <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
                    </div>
                </form>

                <c:if test="${searched and empty queryResult}">
                    <div class="empty">未查询到符合条件的个人账户信息</div>
                </c:if>

                <c:if test="${not empty queryResult}">
                    <h3 class="form-section-title" style="margin-top:24px;">查询结果明细</h3>
                    <div class="receipt">
                        <div class="row">
                            <div class="label">姓名</div>
                            <div class="value" style="font-weight:700;"><c:out value="${queryResult.perName}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">个人公积金账号</div>
                            <div class="value" style="font-weight:700; color:var(--brand);"><c:out value="${queryResult.perAccNum}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">证件号码</div>
                            <div class="value"><c:out value="${queryResult.idCard}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">缴存单位全称</div>
                            <div class="value"><c:out value="${queryResult.unitName}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">缴存单位公积金账号</div>
                            <div class="value"><c:out value="${queryResult.unitAccNum}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">公积金余额</div>
                            <div class="value" style="font-weight:700; color:var(--success);"><c:out value="${queryResult.perBalance}"/> 元</div>
                        </div>
                        <div class="row">
                            <div class="label">最后汇缴月</div>
                            <div class="value"><c:out value="${queryResult.lastPayMonth}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">单位比例</div>
                            <div class="value"><c:out value="${queryResult.unitRatio}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">个人比例</div>
                            <div class="value"><c:out value="${queryResult.perRatio}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">合计比例</div>
                            <div class="value"><c:out value="${queryResult.totalRatio}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">单位月缴额</div>
                            <div class="value"><c:out value="${queryResult.unitMonthPay}"/> 元</div>
                        </div>
                        <div class="row">
                            <div class="label">个人月缴额</div>
                            <div class="value"><c:out value="${queryResult.perMonthPay}"/> 元</div>
                        </div>
                        <div class="row">
                            <div class="label">合计月缴额</div>
                            <div class="value" style="font-weight:700;"><c:out value="${queryResult.totalMonthPay}"/> 元</div>
                        </div>
                        <div class="row">
                            <div class="label">开户日期</div>
                            <div class="value"><c:out value="${queryResult.createTime}"/></div>
                        </div>
                        <div class="row">
                            <div class="label">个人账户状态</div>
                            <div class="value">
                                <span class="badge ${queryResult.statusText eq '正常' ? 'badge-success' : 'badge-warning'}">
                                    <c:out value="${queryResult.statusText}"/>
                                </span>
                            </div>
                        </div>
                    </div>
                </c:if>
            </section>
        </main>
    </div>
</div>
</body>
</html>
