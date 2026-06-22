<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料修改</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<main class="page">
    <section class="panel">
        <h1>个人资料修改</h1>

        <c:if test="${not empty error}">
            <div class="alert"><c:out value="${error}"/></div>
        </c:if>

        <form method="get" action="${pageContext.request.contextPath}/persons/edit/form"
              onsubmit="return validatePersonEditSearchForm(this);">
            <div class="grid">
                <div class="field">
                    <label for="searchPerAccNum">个人公积金账号</label>
                    <input id="searchPerAccNum" name="perAccNum" type="text" required maxlength="12"
                           pattern="[0-9]{12}" value="${fn:escapeXml(personEditForm.perAccNum)}">
                    <div class="tip">请输入 12 位个人公积金账号。</div>
                </div>
            </div>

            <div class="actions">
                <button class="button" type="submit">查询个人账户</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </form>
    </section>

    <c:if test="${personLoaded}">
        <section class="panel">
            <h2>修改资料</h2>

            <form method="post" action="${pageContext.request.contextPath}/persons/edit"
                  onsubmit="return validatePersonEditForm(this);">
                <input type="hidden" name="originalPerName" value="${fn:escapeXml(personEditForm.perName)}">
                <input type="hidden" name="originalIdType" value="${fn:escapeXml(personEditForm.idType)}">
                <input type="hidden" name="originalIdCard" value="${fn:escapeXml(personEditForm.idCard)}">

                <section class="form-section">
                    <h2>账户信息</h2>
                    <div class="grid">
                        <div class="field">
                            <label for="editPerAccNum">个人公积金账号</label>
                            <input id="editPerAccNum" name="perAccNum" type="text" readonly
                                   value="${fn:escapeXml(personEditForm.perAccNum)}">
                        </div>

                        <div class="field">
                            <label for="unitAccNum">单位公积金账号</label>
                            <input id="unitAccNum" name="unitAccNum" type="text" readonly
                                   value="${fn:escapeXml(personEditForm.unitAccNum)}">
                        </div>

                        <div class="field full">
                            <label for="unitName">单位名称</label>
                            <input id="unitName" name="unitName" type="text" readonly
                                   value="${fn:escapeXml(personEditForm.unitName)}">
                        </div>
                    </div>
                </section>

                <section class="form-section">
                    <h2>个人基本信息</h2>
                    <div class="grid">
                        <div class="field">
                            <label for="perName">姓名</label>
                            <input id="perName" name="perName" type="text" required maxlength="12"
                                   value="${fn:escapeXml(personEditForm.perName)}">
                        </div>

                        <div class="field">
                            <label for="idType">证件类型</label>
                            <select id="idType" name="idType" required>
                                <option value="01身份证" ${personEditForm.idType eq '01身份证' ? 'selected' : ''}>01身份证</option>
                            </select>
                        </div>

                        <div class="field full">
                            <label for="idCard">证件号码</label>
                            <input id="idCard" name="idCard" type="text" required maxlength="18"
                                   pattern="[0-9]{17}[0-9Xx]" value="${fn:escapeXml(personEditForm.idCard)}">
                        </div>
                    </div>
                </section>

                <div class="actions">
                    <button class="button" type="submit">提交修改</button>
                    <a class="button secondary" href="${pageContext.request.contextPath}/persons/edit">重新查询</a>
                </div>
            </form>
        </section>
    </c:if>
</main>
</body>
</html>
