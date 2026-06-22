<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${mode eq 'create' ? '新增系统参数' : '修改系统参数'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />
    <div class="app-container">
        <jsp:include page="/WEB-INF/jsp/common/topbar.jsp" />
        <main class="main-content">
            <div class="page-header">
                <h1>系统参数维护</h1>
            </div>

            <section class="panel">
                <h2><c:out value="${mode eq 'create' ? '新增系统参数' : '修改系统参数'}"/></h2>

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
            <section class="form-section">
                <h2>参数信息</h2>
                <div class="grid">
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

                    <div class="field full">
                        <label for="seqDesc">描述</label>
                        <textarea id="seqDesc" name="seqDesc" required maxlength="200"><c:out value="${systemParam.seqDesc}"/></textarea>
                    </div>

                    <div class="field full">
                        <label for="freeuse1">备用1</label>
                        <textarea id="freeuse1" name="freeuse1" maxlength="200"><c:out value="${systemParam.freeuse1}"/></textarea>
                    </div>
                </div>
            </section>

            <div class="actions">
                <button class="button" type="submit">保存</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/params">返回列表</a>
            </div>
        </form>
            </section>
        </main>
    </div>
</div>
</body>
</html>
