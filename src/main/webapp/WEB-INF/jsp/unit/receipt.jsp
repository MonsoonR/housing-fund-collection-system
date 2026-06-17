<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>单位开户回执</title>
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
            grid-template-columns: 180px 1fr;
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
        <h1>单位开户成功</h1>

        <div class="receipt">
            <div class="row">
                <div class="label">单位账号</div>
                <div class="value"><c:out value="${receipt.unitAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">单位名称</div>
                <div class="value"><c:out value="${receipt.unitName}"/></div>
            </div>
            <div class="row">
                <div class="label">组织机构代码</div>
                <div class="value"><c:out value="${receipt.orgCode}"/></div>
            </div>
            <div class="row">
                <div class="label">开户日期</div>
                <div class="value"><c:out value="${receipt.createDate}"/></div>
            </div>
            <div class="row">
                <div class="label">单位比例</div>
                <div class="value"><c:out value="${receipt.unitRatio}"/></div>
            </div>
            <div class="row">
                <div class="label">个人比例</div>
                <div class="value"><c:out value="${receipt.perRatio}"/></div>
            </div>
        </div>

        <div class="actions">
            <a class="button" href="${pageContext.request.contextPath}/units/open">继续开户</a>
            <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
        </div>
    </section>
</main>
</body>
</html>
