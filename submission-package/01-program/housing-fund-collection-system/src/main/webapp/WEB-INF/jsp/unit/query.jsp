<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>单位信息查询</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="page-heading">
        <div class="page-heading-main">
            <h1>单位信息查询</h1>
            <p>按单位公积金账号精确查询，或按单位名称模糊查询。</p>
        </div>
        <div class="page-heading-actions">
            <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
        </div>
    </section>

    <c:if test="${not empty error}">
        <div class="alert"><c:out value="${error}"/></div>
    </c:if>

    <section class="list-toolbar">
        <form class="search" method="post" action="${pageContext.request.contextPath}/units/query"
              onsubmit="return validateUnitQueryForm(this);">
            <label for="unitAccNum">单位公积金账号</label>
            <input id="unitAccNum" name="unitAccNum" type="text" maxlength="12"
                   pattern="[0-9]{12}" value="${fn:escapeXml(unitQueryForm.unitAccNum)}"
                   placeholder="优先精确查询">
            <label for="unitName">单位名称</label>
            <input id="unitName" name="unitName" type="text" maxlength="50"
                   value="${fn:escapeXml(unitQueryForm.unitName)}" placeholder="支持模糊查询">
            <div class="toolbar-left">
                <button class="button" type="submit">查询</button>
            </div>
        </form>
    </section>

    <c:if test="${searched and empty queryResults}">
        <div class="empty">未查询到数据</div>
    </c:if>

    <c:if test="${not empty queryResults and not empty unitQueryForm.unitAccNum}">
        <c:forEach var="unit" items="${queryResults}">
            <section class="result-card">
                <div class="panel-heading">
                    <h2>查询结果</h2>
                    <p class="page-desc">单位账户与缴存汇总信息。</p>
                </div>

                <h3 class="detail-section-title">基础信息</h3>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">单位名称</div>
                        <div class="detail-value strong"><c:out value="${unit.unitName}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">单位公积金账号</div>
                        <div class="detail-value strong"><c:out value="${unit.unitAccNum}"/></div>
                    </div>
                    <div class="detail-item full">
                        <div class="detail-label">单位地址</div>
                        <div class="detail-value"><c:out value="${unit.unitAddr}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">经办人姓名</div>
                        <div class="detail-value"><c:out value="${unit.agentName}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">联系电话</div>
                        <div class="detail-value"><c:out value="${unit.phone}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">单位人数</div>
                        <div class="detail-value num"><c:out value="${unit.persNum}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">账户状态</div>
                        <div class="detail-value"><span class="status-label"><c:out value="${unit.accStateText}"/></span></div>
                    </div>
                </div>

                <h3 class="detail-section-title">缴存比例</h3>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">缴存比例（单位）</div>
                        <div class="detail-value num"><c:out value="${unit.unitRatio}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">缴存比例（个人）</div>
                        <div class="detail-value num"><c:out value="${unit.perRatio}"/></div>
                    </div>
                    <div class="detail-item full">
                        <div class="detail-label">缴存比例（合计）</div>
                        <div class="detail-value num"><c:out value="${unit.totalRatio}"/></div>
                    </div>
                </div>

                <h3 class="detail-section-title">金额信息</h3>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">公积金余额</div>
                        <div class="detail-value num"><c:out value="${unit.balance}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">最后汇缴月</div>
                        <div class="detail-value"><c:out value="${unit.lastPayMonth}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">月汇缴金额（单位）</div>
                        <div class="detail-value num"><c:out value="${unit.unitMonthPay}"/></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">月汇缴金额（个人）</div>
                        <div class="detail-value num"><c:out value="${unit.perMonthPay}"/></div>
                    </div>
                    <div class="detail-item full">
                        <div class="detail-label">月汇缴金额（合计）</div>
                        <div class="detail-value num"><c:out value="${unit.totalMonthPay}"/></div>
                    </div>
                </div>
            </section>
        </c:forEach>
    </c:if>

    <c:if test="${not empty queryResults and empty unitQueryForm.unitAccNum and not empty unitQueryForm.unitName}">
        <section class="result-card">
            <div class="panel-heading">
                <h2>模糊查询结果</h2>
                <p class="page-desc">点击单位名称或查看详情进入单位账号精确查询。</p>
            </div>
            <div class="compact-result-list">
                <c:forEach var="unit" items="${queryResults}">
                    <div class="compact-result-row">
                        <div class="compact-result-main">
                            <a class="compact-result-title"
                               href="${pageContext.request.contextPath}/units/query?unitAccNum=${unit.unitAccNum}"
                               title="点击显示单位详情">
                                <c:out value="${unit.unitName}"/>
                            </a>
                            <div class="compact-result-sub"><c:out value="${unit.unitAccNum}"/></div>
                        </div>
                        <div class="compact-result-field">
                            <span>单位地址</span>
                            <strong><c:out value="${unit.unitAddr}"/></strong>
                        </div>
                        <div class="compact-result-field">
                            <span>经办人姓名</span>
                            <strong><c:out value="${unit.agentName}"/></strong>
                        </div>
                        <div class="compact-result-field">
                            <span>联系电话</span>
                            <strong><c:out value="${unit.phone}"/></strong>
                        </div>
                        <div class="compact-result-field">
                            <span>人数</span>
                            <strong><c:out value="${unit.persNum}"/></strong>
                        </div>
                        <div class="compact-result-action">
                            <a class="button small secondary"
                               href="${pageContext.request.contextPath}/units/query?unitAccNum=${unit.unitAccNum}">
                                查看详情
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:if>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
