<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${mode eq 'create' ? '新增系统参数' : '修改系统参数'}"/></title>
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

        .alert {
            padding: 12px 14px;
            margin-bottom: 16px;
            border-radius: 4px;
            color: #991b1b;
            background: #fee2e2;
            border: 1px solid #fecaca;
            font-size: 14px;
        }

        .field {
            margin-bottom: 16px;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: 600;
            font-size: 14px;
        }

        input,
        textarea {
            width: 100%;
            padding: 10px 11px;
            border: 1px solid #d1d5db;
            border-radius: 4px;
            box-sizing: border-box;
            font-family: inherit;
            font-size: 14px;
        }

        input[readonly] {
            color: #4b5563;
            background: #f9fafb;
        }

        textarea {
            min-height: 84px;
            resize: vertical;
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
    </style>
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<main class="page">
    <section class="panel">
        <h1><c:out value="${mode eq 'create' ? '新增系统参数' : '修改系统参数'}"/></h1>

        <c:if test="${not empty error}">
            <div class="alert"><c:out value="${error}"/></div>
        </c:if>

        <c:choose>
            <c:when test="${mode eq 'create'}">
                <c:url var="formAction" value="/params"/>
            </c:when>
            <c:otherwise>
                <c:url var="formAction" value="/params/${systemParam.seqname}/update"/>
            </c:otherwise>
        </c:choose>

        <form method="post" action="${formAction}" onsubmit="return validateParamForm(this);">
            <div class="field">
                <label for="seqname">键值信息</label>
                <c:choose>
                    <c:when test="${mode eq 'create'}">
                        <input id="seqname" name="seqname" type="text" required maxlength="32"
                               value="${fn:escapeXml(systemParam.seqname)}">
                    </c:when>
                    <c:otherwise>
                        <input id="seqname" name="seqname" type="text" readonly maxlength="32"
                               value="${fn:escapeXml(systemParam.seqname)}">
                    </c:otherwise>
                </c:choose>
                <div class="tip">新增后不可修改。</div>
            </div>

            <div class="field">
                <label for="seq">当前序号</label>
                <input id="seq" name="seq" type="number" required min="1" step="1"
                       value="${fn:escapeXml(systemParam.seq)}">
            </div>

            <div class="field">
                <label for="maxseq">最大序号</label>
                <input id="maxseq" name="maxseq" type="number" required min="1" step="1"
                       value="${fn:escapeXml(systemParam.maxseq)}">
            </div>

            <div class="field">
                <label for="seqDesc">描述</label>
                <textarea id="seqDesc" name="seqDesc" required maxlength="200"><c:out value="${systemParam.seqDesc}"/></textarea>
            </div>

            <div class="field">
                <label for="freeuse1">备用1</label>
                <textarea id="freeuse1" name="freeuse1" maxlength="200"><c:out value="${systemParam.freeuse1}"/></textarea>
            </div>

            <div class="actions">
                <button class="button" type="submit">保存</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/params">返回列表</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
