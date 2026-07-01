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
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="page-heading form-page-heading">
        <div class="page-heading-main">
            <h1><c:out value="${mode eq 'create' ? '新增系统参数' : '修改系统参数'}"/></h1>
            <p>维护系统参数键值、当前序号、最大序号和参数说明。</p>
        </div>
    </section>

    <section class="panel form-panel">
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
            <div class="form-section">
                <h2>参数信息</h2>
                <div class="grid">
                    <div class="field">
                        <label for="seqname">键值信息<span class="required">*</span></label>
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
                        <label for="seq">当前序号<span class="required">*</span></label>
                        <input id="seq" name="seq" type="number" required min="1" step="1"
                               value="${fn:escapeXml(systemParam.seq)}">
                    </div>

                    <div class="field">
                        <label for="maxseq">最大序号<span class="required">*</span></label>
                        <input id="maxseq" name="maxseq" type="number" required min="1" step="1"
                               value="${fn:escapeXml(systemParam.maxseq)}">
                    </div>

                    <div class="field full">
                        <label for="seqDesc">描述<span class="required">*</span></label>
                        <textarea id="seqDesc" name="seqDesc" required maxlength="200"><c:out value="${systemParam.seqDesc}"/></textarea>
                    </div>

                    <div class="field full">
                        <label for="freeuse1">备用1</label>
                        <textarea id="freeuse1" name="freeuse1" maxlength="200"><c:out value="${systemParam.freeuse1}"/></textarea>
                    </div>
                </div>
            </div>

            <div class="form-action-bar">
                <button class="button" type="submit">保存</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/params">返回列表</a>
            </div>
        </form>
    </section>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
