<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>系统参数维护</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
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

            <c:if test="${not empty success}">
                <div class="alert success"><c:out value="${success}"/></div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error"><c:out value="${error}"/></div>
            </c:if>

    <section class="toolbar">
        <form class="search" method="get" action="${pageContext.request.contextPath}/params">
            <label for="seqname">键值信息</label>
            <input id="seqname" name="seqname" type="text" maxlength="32"
                   value="${fn:escapeXml(querySeqname)}" placeholder="支持模糊查询">
            <button class="button secondary" type="submit">查询</button>
            <a class="button secondary" href="${pageContext.request.contextPath}/params">全部</a>
        </form>
        <a class="button" href="${pageContext.request.contextPath}/params/new">新增参数</a>
    </section>

    <section class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>键值信息</th>
                <th>当前序号</th>
                <th>最大序号</th>
                <th>描述</th>
                <th>备用1</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${params}" var="item">
                <tr>
                    <td><c:out value="${item.seqname}"/></td>
                    <td><c:out value="${item.seq}"/></td>
                    <td><c:out value="${item.maxseq}"/></td>
                    <td class="desc"><c:out value="${item.seqDesc}"/></td>
                    <td class="freeuse"><c:out value="${item.freeuse1}"/></td>
                    <td>
                        <div class="actions">
                            <a class="button secondary"
                               href="${pageContext.request.contextPath}/params/${item.seqname}/edit">修改</a>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/params/${item.seqname}/delete"
                                  onsubmit="return confirm('确认删除参数 ${item.seqname}？');">
                                <button class="button danger" type="submit">删除</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty params}">
                <tr>
                    <td class="empty" colspan="6">暂无系统参数</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </section>
        </main>
    </div>
</div>
</body>
</html>
