<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人信息查询</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, "Microsoft YaHei", sans-serif;
            color: #1f2937;
            background: #f3f4f6;
        }

        .page {
            max-width: 900px;
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

        .receipt {
            margin-top: 22px;
            border: 1px solid #e5e7eb;
            border-radius: 6px;
            overflow: hidden;
        }

        .row {
            display: grid;
            grid-template-columns: 190px 1fr;
            border-bottom: 1px solid #e5e7eb;
        }

        .row:last-child {
            border-bottom: 0;
        }

        .label,
        .value {
            padding: 12px 14px;
            font-size: 14px;
        }

        .label {
            color: #374151;
            background: #f9fafb;
            font-weight: 600;
        }

        @media (max-width: 720px) {
            .grid,
            .row {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<main class="page">
    <section class="panel">
        <h1>个人信息查询</h1>

        <c:if test="${not empty error}">
            <div class="alert"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/persons/query"
              onsubmit="return validatePersonQueryForm(this);">
            <div class="grid">
                <div class="field">
                    <label for="perAccNum">个人公积金账号</label>
                    <input id="perAccNum" name="perAccNum" type="text" maxlength="12"
                           pattern="[0-9]{12}" value="${fn:escapeXml(personQueryForm.perAccNum)}">
                    <div class="tip">输入个人公积金账号时优先精确查询。</div>
                </div>

                <div class="field">
                    <label for="idCard">证件号码</label>
                    <input id="idCard" name="idCard" type="text" maxlength="18"
                           pattern="[0-9]{17}[0-9Xx]" value="${fn:escapeXml(personQueryForm.idCard)}">
                    <div class="tip">未输入个人公积金账号时按证件号码查询。</div>
                </div>
            </div>

            <div class="actions">
                <button class="button" type="submit">查询</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </form>

        <c:if test="${searched and empty queryResult}">
            <div class="empty">未查询到个人信息</div>
        </c:if>

        <c:if test="${not empty queryResult}">
            <div class="receipt">
                <div class="row">
                    <div class="label">缴存单位全称</div>
                    <div class="value"><c:out value="${queryResult.unitName}"/></div>
                </div>
                <div class="row">
                    <div class="label">缴存单位公积金账号</div>
                    <div class="value"><c:out value="${queryResult.unitAccNum}"/></div>
                </div>
                <div class="row">
                    <div class="label">姓名</div>
                    <div class="value"><c:out value="${queryResult.perName}"/></div>
                </div>
                <div class="row">
                    <div class="label">个人公积金账号</div>
                    <div class="value"><c:out value="${queryResult.perAccNum}"/></div>
                </div>
                <div class="row">
                    <div class="label">证件号码</div>
                    <div class="value"><c:out value="${queryResult.idCard}"/></div>
                </div>
                <div class="row">
                    <div class="label">余额</div>
                    <div class="value"><c:out value="${queryResult.perBalance}"/></div>
                </div>
                <div class="row">
                    <div class="label">开户日期</div>
                    <div class="value"><c:out value="${queryResult.createTime}"/></div>
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
                    <div class="value"><c:out value="${queryResult.unitMonthPay}"/></div>
                </div>
                <div class="row">
                    <div class="label">个人月缴额</div>
                    <div class="value"><c:out value="${queryResult.perMonthPay}"/></div>
                </div>
                <div class="row">
                    <div class="label">合计月缴额</div>
                    <div class="value"><c:out value="${queryResult.totalMonthPay}"/></div>
                </div>
                <div class="row">
                    <div class="label">个人账户状态</div>
                    <div class="value"><c:out value="${queryResult.statusText}"/></div>
                </div>
            </div>
        </c:if>
    </section>
</main>
</body>
</html>
