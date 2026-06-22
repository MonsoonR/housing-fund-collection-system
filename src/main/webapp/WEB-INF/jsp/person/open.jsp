<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人开户</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="page-title">
        <h1>个人开户</h1>
        <p>先校验缴存单位，再办理个人手工开户；也可通过 Excel 批量开户。</p>
    </section>

    <c:if test="${not empty error}">
        <div class="alert"><c:out value="${error}"/></div>
    </c:if>

    <c:if test="${not empty importResult}">
        <div class="import-result">
            <h2>Excel 批量导入结果</h2>
            <div class="import-summary">
                <div class="summary-item success">
                    成功条数
                    <strong><c:out value="${importResult.successCount}"/></strong>
                </div>
                <div class="summary-item failure">
                    失败条数
                    <strong><c:out value="${importResult.failureCount}"/></strong>
                </div>
            </div>
            <c:if test="${importResult.failureCount eq 0}">
                <div class="notice">全部记录导入成功。</div>
            </c:if>
            <c:if test="${importResult.failureCount gt 0}">
                <div class="table-scroll">
                    <table class="data-table">
                        <thead>
                        <tr>
                            <th class="num">Excel 行号</th>
                            <th>姓名</th>
                            <th>证件号码</th>
                            <th>失败原因</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="failure" items="${importResult.failures}">
                            <tr>
                                <td class="num"><c:out value="${failure.rowNumber}"/></td>
                                <td><c:out value="${failure.perName}"/></td>
                                <td><c:out value="${failure.idCard}"/></td>
                                <td class="reason"><c:out value="${failure.message}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>
    </c:if>

    <div class="split-panels">
        <section class="content-panel compact-panel">
            <h2>单位查询</h2>
            <form method="get" action="${pageContext.request.contextPath}/persons/open/unit"
                  onsubmit="return validatePersonOpenUnitForm(this);">
                <div class="form-grid">
                    <div class="field full">
                        <label for="searchUnitAccNum">单位公积金账号<span class="required">*</span></label>
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

        <section class="content-panel compact-panel">
            <h2>Excel 批量导入个人开户</h2>
            <form method="post" action="${pageContext.request.contextPath}/persons/open/import"
                  enctype="multipart/form-data">
                <div class="form-grid">
                    <div class="field full">
                        <label for="excelFile">Excel 文件<span class="required">*</span></label>
                        <input id="excelFile" name="excelFile" type="file" required accept=".xls,.xlsx">
                        <div class="tip">首行表头，列顺序为：单位账号、姓名、证件类型、证件号码、缴存基数、单位比例、个人比例。</div>
                    </div>
                </div>
                <div class="actions">
                    <button class="button" type="submit">导入开户</button>
                </div>
            </form>
        </section>
    </div>

    <c:if test="${unitLoaded}">
        <section class="content-panel">
            <h2>个人开户信息录入</h2>

            <form method="post" action="${pageContext.request.contextPath}/persons/open"
                  onsubmit="return validatePersonOpenForm(this);">
                <div class="form-section">
                    <h2>缴存单位信息</h2>
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
                    </div>
                </div>

                <div class="form-section">
                    <h2>个人基本信息</h2>
                    <div class="grid">
                    <div class="field">
                        <label for="perName">姓名<span class="required">*</span></label>
                        <input id="perName" name="perName" type="text" required maxlength="12"
                               value="${fn:escapeXml(personOpenForm.perName)}">
                    </div>

                    <div class="field">
                        <label for="idType">证件类型<span class="required">*</span></label>
                        <select id="idType" name="idType" required>
                            <option value="01身份证"
                                    ${personOpenForm.idType eq '01身份证' ? 'selected' : ''}>01身份证</option>
                        </select>
                    </div>

                    <div class="field">
                        <label for="idCard">证件号码<span class="required">*</span></label>
                        <input id="idCard" name="idCard" type="text" required maxlength="18"
                               pattern="[0-9]{17}[0-9Xx]" value="${fn:escapeXml(personOpenForm.idCard)}">
                        <div class="tip">当前仅支持 18 位居民身份证号码。</div>
                    </div>
                    </div>
                </div>

                <div class="form-section">
                    <h2>缴存信息</h2>
                    <div class="grid">
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
                        <label for="baseNum">缴存基数<span class="required">*</span></label>
                        <input id="baseNum" name="baseNum" type="text" required maxlength="12"
                               pattern="[0-9]+(\.[0-9]{1,2})?" value="${fn:escapeXml(personOpenForm.baseNum)}">
                        <div class="tip">必须大于 0，最多保留 2 位小数。</div>
                    </div>
                    </div>
                </div>

                <div class="actions">
                    <button class="button" type="submit">提交开户</button>
                    <a class="button secondary" href="${pageContext.request.contextPath}/persons/open">重新查询单位</a>
                </div>
            </form>
        </section>
    </c:if>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
