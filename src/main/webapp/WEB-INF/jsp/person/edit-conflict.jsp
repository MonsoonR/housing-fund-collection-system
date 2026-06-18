<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料修改冲突确认</title>
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

        h1,
        h2 {
            margin: 0 0 18px;
            font-weight: 600;
        }

        h1 {
            font-size: 24px;
        }

        h2 {
            font-size: 18px;
        }

        .alert {
            padding: 12px 14px;
            margin-bottom: 16px;
            border-radius: 4px;
            color: #92400e;
            background: #fef3c7;
            border: 1px solid #fde68a;
            font-size: 14px;
        }

        .receipt {
            margin-bottom: 20px;
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

        .note {
            margin: 0 0 20px;
            line-height: 1.7;
            color: #4b5563;
            font-size: 14px;
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

        @media (max-width: 720px) {
            .row {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<main class="page">
    <section class="panel">
        <h1>身份证号占用确认</h1>

        <div class="alert">该身份证号已被其他个人账户占用，是否强制变更？</div>

        <h2>当前修改信息</h2>
        <div class="receipt">
            <div class="row">
                <div class="label">当前个人账号</div>
                <div class="value"><c:out value="${conflict.currentPerAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">当前姓名</div>
                <div class="value"><c:out value="${conflict.currentPerName}"/></div>
            </div>
            <div class="row">
                <div class="label">修改后的姓名</div>
                <div class="value"><c:out value="${conflict.newPerName}"/></div>
            </div>
            <div class="row">
                <div class="label">修改后的身份证号</div>
                <div class="value"><c:out value="${conflict.newIdCard}"/></div>
            </div>
        </div>

        <h2>占用账户信息</h2>
        <div class="receipt">
            <div class="row">
                <div class="label">占用个人账号</div>
                <div class="value"><c:out value="${conflict.occupiedPerAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">占用身份证号</div>
                <div class="value"><c:out value="${conflict.occupiedIdCard}"/></div>
            </div>
            <div class="row">
                <div class="label">占用姓名</div>
                <div class="value"><c:out value="${conflict.occupiedPerName}"/></div>
            </div>
            <div class="row">
                <div class="label">占用状态</div>
                <div class="value"><c:out value="${conflict.occupiedStatusText}"/></div>
            </div>
            <div class="row">
                <div class="label">占用单位账号</div>
                <div class="value"><c:out value="${conflict.occupiedUnitAccNum}"/></div>
            </div>
            <div class="row">
                <div class="label">占用单位名称</div>
                <div class="value"><c:out value="${conflict.occupiedUnitName}"/></div>
            </div>
        </div>

        <p class="note">确认后，占用账户身份证号首位将改为 9，其余位数保持不变。</p>

        <div class="actions">
            <form method="post" action="${pageContext.request.contextPath}/persons/edit/force">
                <input type="hidden" name="perAccNum" value="${fn:escapeXml(personEditForm.perAccNum)}">
                <input type="hidden" name="unitAccNum" value="${fn:escapeXml(personEditForm.unitAccNum)}">
                <input type="hidden" name="unitName" value="${fn:escapeXml(personEditForm.unitName)}">
                <input type="hidden" name="perName" value="${fn:escapeXml(personEditForm.perName)}">
                <input type="hidden" name="idType" value="${fn:escapeXml(personEditForm.idType)}">
                <input type="hidden" name="idCard" value="${fn:escapeXml(personEditForm.idCard)}">
                <button class="button" type="submit">确认强制变更</button>
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
</main>
</body>
</html>
