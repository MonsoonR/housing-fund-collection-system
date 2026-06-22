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
<div class="app-layout">
    <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />
    <div class="app-container">
        <jsp:include page="/WEB-INF/jsp/common/topbar.jsp" />
        <main class="main-content">
            <div class="page-header">
                <h1>单位信息查询</h1>
            </div>

            <section class="panel">
                <c:if test="${not empty error}">
                    <div class="alert error"><c:out value="${error}"/></div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/units/query"
                      onsubmit="return validateUnitQueryForm(this);">
                    <div class="grid">
                        <div class="field">
                            <label for="unitAccNum">单位公积金账号</label>
                            <input id="unitAccNum" name="unitAccNum" type="text" maxlength="12"
                                   pattern="[0-9]{12}" value="${fn:escapeXml(unitQueryForm.unitAccNum)}">
                            <div class="tip">优先按 12 位账号精确查询。</div>
                        </div>

                        <div class="field">
                            <label for="unitName">单位名称</label>
                            <input id="unitName" name="unitName" type="text" maxlength="50"
                                   value="${fn:escapeXml(unitQueryForm.unitName)}">
                            <div class="tip">支持名称模糊查询。结果中单位名称可双击快速定位详情。</div>
                        </div>
                    </div>

                    <div class="actions">
                        <button class="button" type="submit">查询</button>
                        <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
                    </div>
                </form>

                <c:if test="${searched and empty queryResults}">
                    <div class="empty">未查询到任何符合条件的单位信息</div>
                </c:if>

                <c:if test="${not empty queryResults}">
                    <div class="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>单位名称</th>
                                <th>单位公积金账号</th>
                                <th>单位地址</th>
                                <th>经办人姓名</th>
                                <th>联系电话</th>
                                <th class="text-right">公积金余额</th>
                                <th class="text-right">单位比例</th>
                                <th class="text-right">个人比例</th>
                                <th class="text-right">合计比例</th>
                                <th class="text-center">最后汇缴月</th>
                                <th class="text-right">单位月缴额</th>
                                <th class="text-right">个人月缴额</th>
                                <th class="text-right">合计月缴额</th>
                                <th class="text-right">单位人数</th>
                                <th class="text-center">账户状态</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="unit" items="${queryResults}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/units/query?unitAccNum=${unit.unitAccNum}"
                                           title="点击或双击显示单位情况"
                                           ondblclick="window.location.href=this.href; return false;">
                                            <c:out value="${unit.unitName}"/>
                                        </a>
                                    </td>
                                    <td><c:out value="${unit.unitAccNum}"/></td>
                                    <td><c:out value="${unit.unitAddr}"/></td>
                                    <td><c:out value="${unit.agentName}"/></td>
                                    <td><c:out value="${unit.phone}"/></td>
                                    <td class="text-right"><c:out value="${unit.balance}"/></td>
                                    <td class="text-right"><c:out value="${unit.unitRatio}"/></td>
                                    <td class="text-right"><c:out value="${unit.perRatio}"/></td>
                                    <td class="text-right"><c:out value="${unit.totalRatio}"/></td>
                                    <td class="text-center"><c:out value="${unit.lastPayMonth}"/></td>
                                    <td class="text-right"><c:out value="${unit.unitMonthPay}"/></td>
                                    <td class="text-right"><c:out value="${unit.perMonthPay}"/></td>
                                    <td class="text-right"><c:out value="${unit.totalMonthPay}"/></td>
                                    <td class="text-right"><c:out value="${unit.persNum}"/></td>
                                    <td class="text-center">
                                        <span class="badge ${unit.accStateText eq '正常' ? 'badge-success' : 'badge-warning'}">
                                            <c:out value="${unit.accStateText}"/>
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </section>
        </main>
    </div>
</div>
</body>
</html>
