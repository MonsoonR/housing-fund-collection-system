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
    <section class="panel">
        <h1>单位信息查询</h1>
        <p class="page-desc">按单位公积金账号精确查询，或按单位名称模糊查询。</p>

        <c:if test="${not empty error}">
            <div class="alert"><c:out value="${error}"/></div>
        </c:if>

        <form class="filter-form" method="post" action="${pageContext.request.contextPath}/units/query"
              onsubmit="return validateUnitQueryForm(this);">
            <div class="grid">
                <div class="field">
                    <label for="unitAccNum">单位公积金账号</label>
                    <input id="unitAccNum" name="unitAccNum" type="text" maxlength="12"
                           pattern="[0-9]{12}" value="${fn:escapeXml(unitQueryForm.unitAccNum)}">
                    <div class="tip">输入单位公积金账号时优先精确查询。</div>
                </div>

                <div class="field">
                    <label for="unitName">单位名称</label>
                    <input id="unitName" name="unitName" type="text" maxlength="50"
                           value="${fn:escapeXml(unitQueryForm.unitName)}">
                    <div class="tip">未输入单位公积金账号时按名称模糊查询；结果中单位名称可点击或双击显示单位情况。</div>
                </div>
            </div>

            <div class="actions">
                <button class="button" type="submit">查询</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </form>

        <c:if test="${searched and empty queryResults}">
            <div class="empty">未查询到数据</div>
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
                        <th class="num">公积金余额</th>
                        <th class="num">单位比例</th>
                        <th class="num">个人比例</th>
                        <th class="num">合计比例</th>
                        <th>最后汇缴月</th>
                        <th class="num">单位月缴额</th>
                        <th class="num">个人月缴额</th>
                        <th class="num">合计月缴额</th>
                        <th class="num">单位人数</th>
                        <th>账户状态</th>
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
                            <td class="num"><c:out value="${unit.balance}"/></td>
                            <td class="num"><c:out value="${unit.unitRatio}"/></td>
                            <td class="num"><c:out value="${unit.perRatio}"/></td>
                            <td class="num"><c:out value="${unit.totalRatio}"/></td>
                            <td><c:out value="${unit.lastPayMonth}"/></td>
                            <td class="num"><c:out value="${unit.unitMonthPay}"/></td>
                            <td class="num"><c:out value="${unit.perMonthPay}"/></td>
                            <td class="num"><c:out value="${unit.totalMonthPay}"/></td>
                            <td class="num"><c:out value="${unit.persNum}"/></td>
                            <td><span class="status-label"><c:out value="${unit.accStateText}"/></span></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </section>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
