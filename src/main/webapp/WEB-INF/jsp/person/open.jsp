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

        .panel + .panel {
            margin-top: 18px;
        }

        h1,
        h2 {
            margin: 0 0 20px;
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
            color: #991b1b;
            background: #fee2e2;
            border: 1px solid #fecaca;
            font-size: 14px;
        }

        .notice {
            padding: 12px 14px;
            margin-bottom: 16px;
            border-radius: 4px;
            color: #065f46;
            background: #d1fae5;
            border: 1px solid #a7f3d0;
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
        select {
            width: 100%;
            padding: 10px 11px;
            border: 1px solid #d1d5db;
            border-radius: 4px;
            box-sizing: border-box;
            font-family: inherit;
            font-size: 14px;
            background: #ffffff;
        }

        input[readonly] {
            color: #4b5563;
            background: #f9fafb;
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

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 12px;
            font-size: 14px;
        }

        th,
        td {
            padding: 10px 12px;
            border: 1px solid #e5e7eb;
            text-align: left;
        }

        th {
            background: #f9fafb;
            font-weight: 600;
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

        <c:if test="${not empty importResult}">
            <div class="notice">
                批量导入结果：成功 <c:out value="${importResult.successCount}"/> 条，
                失败 <c:out value="${importResult.failureCount}"/> 条。
            </div>
            <c:if test="${importResult.failureCount gt 0}">
                <table>
                    <thead>
                    <tr>
                        <th>Excel 行号</th>
                        <th>姓名</th>
                        <th>证件号码</th>
                        <th>失败原因</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="failure" items="${importResult.failures}">
                        <tr>
                            <td><c:out value="${failure.rowNumber}"/></td>
                            <td><c:out value="${failure.perName}"/></td>
                            <td><c:out value="${failure.idCard}"/></td>
                            <td><c:out value="${failure.message}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </c:if>

        <form method="get" action="${pageContext.request.contextPath}/persons/open/unit"
              onsubmit="return validatePersonOpenUnitForm(this);">
            <div class="grid">
                <div class="field">
                    <label for="searchUnitAccNum">单位公积金账号</label>
                    <input id="searchUnitAccNum" name="unitAccNum" type="text" required maxlength="12"
                           pattern="[0-9]{12}" value="${fn:escapeXml(personOpenForm.unitAccNum)}">
                    <div class="tip">请输入已开户且正常状态的 12 位单位公积金账号。</div>
                </div>
            </div>

            <div class="actions">
                <button class="button" type="submit">查询单位</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </form>
    </section>

    <section class="panel">
        <h2>Excel 批量导入个人开户</h2>
        <form method="post" action="${pageContext.request.contextPath}/persons/open/import"
              enctype="multipart/form-data">
            <div class="grid">
                <div class="field full">
                    <label for="excelFile">Excel 文件</label>
                    <input id="excelFile" name="excelFile" type="file" required accept=".xls,.xlsx">
                    <div class="tip">首行表头，列顺序为：单位账号、姓名、证件类型、证件号码、缴存基数、单位比例、个人比例。</div>
                </div>
            </div>
            <div class="actions">
                <button class="button" type="submit">导入开户</button>
            </div>
        </form>
    </section>

    <c:if test="${unitLoaded}">
        <section class="panel">
            <h2>录入个人开户信息</h2>

            <form method="post" action="${pageContext.request.contextPath}/persons/open"
                  onsubmit="return validatePersonOpenForm(this);">
                <div class="grid">
                    <div class="field">
                        <label for="unitAccNum">单位公积金账号</label>
                        <input id="unitAccNum" name="unitAccNum" type="text" readonly
                               value="${fn:escapeXml(personOpenForm.unitAccNum)}">
                    </div>

                    <div class="field">
                        <label for="unitName">单位名称</label>
                        <input id="unitName" name="unitName" type="text" readonly
                               value="${fn:escapeXml(personOpenForm.unitName)}">
                    </div>

                    <div class="field">
                        <label for="unitRatio">单位比例</label>
                        <input id="unitRatio" name="unitRatio" type="text" readonly
                               value="${fn:escapeXml(personOpenForm.unitRatio)}">
                    </div>

                    <div class="field">
                        <label for="perRatio">个人比例</label>
                        <input id="perRatio" name="perRatio" type="text" readonly
                               value="${fn:escapeXml(personOpenForm.perRatio)}">
                    </div>

                    <div class="field">
                        <label for="perName">姓名</label>
                        <input id="perName" name="perName" type="text" required maxlength="12"
                               value="${fn:escapeXml(personOpenForm.perName)}">
                    </div>

                    <div class="field">
                        <label for="idType">证件类型</label>
                        <select id="idType" name="idType" required>
                            <option value="01身份证"
                                    ${personOpenForm.idType eq '01身份证' ? 'selected' : ''}>01身份证</option>
                        </select>
                    </div>

                    <div class="field">
                        <label for="idCard">证件号码</label>
                        <input id="idCard" name="idCard" type="text" required maxlength="18"
                               pattern="[0-9]{17}[0-9Xx]" value="${fn:escapeXml(personOpenForm.idCard)}">
                        <div class="tip">当前仅支持 18 位居民身份证号码。</div>
                    </div>

                    <div class="field">
                        <label for="baseNum">缴存基数</label>
                        <input id="baseNum" name="baseNum" type="text" required maxlength="12"
                               pattern="[0-9]+(\.[0-9]{1,2})?" value="${fn:escapeXml(personOpenForm.baseNum)}">
                        <div class="tip">必须大于 0，最多保留 2 位小数。</div>
                    </div>
                </div>

                <div class="actions">
                    <button class="button" type="submit">提交开户</button>
                    <a class="button secondary" href="${pageContext.request.contextPath}/persons/open">重新查询单位</a>
                </div>
            </form>
        </section>
    </c:if>
</main>
</body>
</html>
