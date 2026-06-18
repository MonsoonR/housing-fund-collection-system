<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人开户</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, "Microsoft YaHei", sans-serif;
            color: #1f2937;
            background: #f3f4f6;
        }

        .page {
            max-width: 860px;
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

        .grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 16px;
        }

        .field.full {
            grid-column: 1 / -1;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: 600;
            font-size: 14px;
        }

        input,
        select,
        textarea {
            width: 100%;
            padding: 10px 11px;
            border: 1px solid #d1d5db;
            border-radius: 4px;
            box-sizing: border-box;
            font-family: inherit;
            font-size: 14px;
            background: #ffffff;
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
        <h1>个人开户</h1>

        <c:if test="${not empty error}">
            <div class="alert"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/persons/open"
              onsubmit="return validatePersonOpenForm(this);">
            <div class="grid">
                <div class="field">
                    <label for="unitAccNum">单位账号</label>
                    <input id="unitAccNum" name="unitAccNum" type="text" required maxlength="12"
                           pattern="[0-9]{12}" value="${fn:escapeXml(personOpenForm.unitAccNum)}">
                    <div class="tip">请输入 12 位单位账号。</div>
                </div>

                <div class="field">
                    <label for="perName">姓名</label>
                    <input id="perName" name="perName" type="text" required maxlength="50"
                           value="${fn:escapeXml(personOpenForm.perName)}">
                </div>

                <div class="field">
                    <label for="idType">证件类型</label>
                    <select id="idType" name="idType" required>
                        <option value="居民身份证"
                                ${personOpenForm.idType eq '居民身份证' ? 'selected' : ''}>居民身份证</option>
                    </select>
                </div>

                <div class="field">
                    <label for="idCard">证件号码</label>
                    <input id="idCard" name="idCard" type="text" required maxlength="18"
                           pattern="[0-9]{17}[0-9Xx]" value="${fn:escapeXml(personOpenForm.idCard)}">
                    <div class="tip">当前仅支持 18 位居民身份证。</div>
                </div>

                <div class="field">
                    <label for="baseNum">缴存基数</label>
                    <input id="baseNum" name="baseNum" type="text" required maxlength="12"
                           pattern="[0-9]+(\.[0-9]{1,2})?" value="${fn:escapeXml(personOpenForm.baseNum)}">
                    <div class="tip">必须大于 0，最多保留 2 位小数。</div>
                </div>

                <div class="field">
                    <label for="phone">联系电话</label>
                    <input id="phone" name="phone" type="text" maxlength="30"
                           value="${fn:escapeXml(personOpenForm.phone)}">
                </div>

                <div class="field full">
                    <label for="address">联系地址</label>
                    <textarea id="address" name="address" maxlength="200"><c:out value="${personOpenForm.address}"/></textarea>
                </div>
            </div>

            <div class="actions">
                <button class="button" type="submit">提交开户</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
