<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>单位信息查询</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, "Microsoft YaHei", sans-serif;
            color: #1f2937;
            background: #f3f4f6;
        }

        .page {
            max-width: 1120px;
            margin: 36px auto;
            padding: 0 24px;
        }

        .panel {
            padding: 24px 28px;
            background: #ffffff;
            border: 1px solid #e5e7eb;
            border-radius: 6px;
        }

        h1 {
            margin: 0 0 20px;
            font-size: 24px;
            font-weight: 600;
        }

        .alert {
            padding: 12px 14px;
            margin-bottom: 16px;
            border-radius: 4px;
            color: #991b1b;
            background: #fee2e2;
            border: 1px solid #fecaca;
            font-size: 14px;
        }

        .empty {
            padding: 12px 14px;
            margin-top: 18px;
            border-radius: 4px;
            color: #374151;
            background: #f9fafb;
            border: 1px solid #e5e7eb;
            font-size: 14px;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 16px;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: 600;
            font-size: 14px;
        }

        input {
            width: 100%;
            padding: 10px 11px;
            border: 1px solid #d1d5db;
            border-radius: 4px;
            box-sizing: border-box;
            font-family: inherit;
            font-size: 14px;
            background: #ffffff;
        }

        .tip {
            margin-top: 5px;
            color: #6b7280;
            font-size: 12px;
        }

        .actions {
            display: flex;
            gap: 10px;
            margin-top: 22px;
        }

        .button {
            display: inline-block;
            padding: 10px 16px;
            border: 1px solid #0f766e;
            border-radius: 4px;
            color: #ffffff;
            background: #0f766e;
            font-size: 14px;
            text-decoration: none;
            cursor: pointer;
        }

        .button.secondary {
            color: #0f766e;
            background: #ffffff;
        }

        .table-wrap {
            margin-top: 22px;
            overflow-x: auto;
            border: 1px solid #e5e7eb;
            border-radius: 6px;
        }

        table {
            width: 100%;
            min-width: 1080px;
            border-collapse: collapse;
            background: #ffffff;
        }

        th,
        td {
            padding: 11px 12px;
            border-bottom: 1px solid #e5e7eb;
            text-align: left;
            font-size: 13px;
            white-space: nowrap;
        }

        th {
            color: #374151;
            background: #f9fafb;
            font-weight: 600;
        }

        tr:last-child td {
            border-bottom: 0;
        }

        @media (max-width: 720px) {
            .grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<main class="page">
    <section class="panel">
        <h1>单位信息查询</h1>

        <c:if test="${not empty error}">
            <div class="alert"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/units/query"
              onsubmit="return validateUnitQueryForm(this);">
            <div class="grid">
                <div class="field">
                    <label for="unitAccNum">单位账号</label>
                    <input id="unitAccNum" name="unitAccNum" type="text" maxlength="12"
                           pattern="[0-9]{12}" value="${fn:escapeXml(unitQueryForm.unitAccNum)}">
                    <div class="tip">输入单位账号时优先精确查询。</div>
                </div>

                <div class="field">
                    <label for="unitName">单位名称</label>
                    <input id="unitName" name="unitName" type="text" maxlength="50"
                           value="${fn:escapeXml(unitQueryForm.unitName)}">
                    <div class="tip">未输入单位账号时按名称模糊查询。</div>
                </div>
            </div>

            <div class="actions">
                <button class="button" type="submit">查询</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </form>

        <c:if test="${searched and empty queryResults}">
            <div class="empty">未查询到单位信息</div>
        </c:if>

        <c:if test="${not empty queryResults}">
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>单位名称</th>
                        <th>单位账号</th>
                        <th>单位地址</th>
                        <th>经办人姓名</th>
                        <th>联系电话</th>
                        <th>公积金余额</th>
                        <th>单位比例</th>
                        <th>个人比例</th>
                        <th>合计比例</th>
                        <th>最后汇缴月</th>
                        <th>单位月缴额</th>
                        <th>个人月缴额</th>
                        <th>合计月缴额</th>
                        <th>单位人数</th>
                        <th>账户状态</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="unit" items="${queryResults}">
                        <tr>
                            <td><c:out value="${unit.unitName}"/></td>
                            <td><c:out value="${unit.unitAccNum}"/></td>
                            <td><c:out value="${unit.unitAddr}"/></td>
                            <td><c:out value="${unit.agentName}"/></td>
                            <td><c:out value="${unit.phone}"/></td>
                            <td><c:out value="${unit.balance}"/></td>
                            <td><c:out value="${unit.unitRatio}"/></td>
                            <td><c:out value="${unit.perRatio}"/></td>
                            <td><c:out value="${unit.totalRatio}"/></td>
                            <td><c:out value="${unit.lastPayMonth}"/></td>
                            <td><c:out value="${unit.unitMonthPay}"/></td>
                            <td><c:out value="${unit.perMonthPay}"/></td>
                            <td><c:out value="${unit.totalMonthPay}"/></td>
                            <td><c:out value="${unit.persNum}"/></td>
                            <td><c:out value="${unit.accStateText}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </section>
</main>
</body>
</html>
