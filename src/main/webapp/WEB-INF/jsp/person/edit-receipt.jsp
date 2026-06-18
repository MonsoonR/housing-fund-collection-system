<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料变更回单</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, "Microsoft YaHei", sans-serif;
            color: #1f2937;
            background: #f3f4f6;
        }

        .page {
            max-width: 760px;
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

        .receipt {
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
        }

        .button.secondary {
            color: #0f766e;
            background: #ffffff;
        }
    </style>
</head>
<body>
<main class="page">
    <section class="panel">
        <h1>个人资料变更回单</h1>

        <div class="receipt">
            <div class="row">
                <div class="label">个人公积金账号</div>
                <div class="value"><c:out value="${receipt.perAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">姓名</div>
                <div class="value"><c:out value="${receipt.perName}"/></div>
            </div>
            <div class="row">
                <div class="label">证件号码</div>
                <div class="value"><c:out value="${receipt.idCard}"/></div>
            </div>
            <div class="row">
                <div class="label">单位公积金账号</div>
                <div class="value"><c:out value="${receipt.unitAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">单位名称</div>
                <div class="value"><c:out value="${receipt.unitName}"/></div>
            </div>
            <div class="row">
                <div class="label">是否执行强制变更</div>
                <div class="value"><c:out value="${receipt.forceChanged ? '是' : '否'}"/></div>
            </div>
            <c:if test="${receipt.forceChanged}">
                <div class="row">
                    <div class="label">被占用个人公积金账号</div>
                    <div class="value"><c:out value="${receipt.conflictPerAccNum}"/></div>
                </div>
                <div class="row">
                    <div class="label">新建错误账户账号</div>
                    <div class="value"><c:out value="${receipt.wrongAccountPerAccNum}"/></div>
                </div>
                <div class="row">
                    <div class="label">原证件号码</div>
                    <div class="value"><c:out value="${receipt.originalConflictIdCard}"/></div>
                </div>
                <div class="row">
                    <div class="label">变更后的错误证件号码</div>
                    <div class="value"><c:out value="${receipt.changedConflictIdCard}"/></div>
                </div>
            </c:if>
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
            <a class="button" href="${pageContext.request.contextPath}/persons/edit">继续修改</a>
            <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
        </div>
    </section>
</main>
</body>
</html>
