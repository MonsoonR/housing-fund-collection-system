<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>系统参数维护</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, "Microsoft YaHei", sans-serif;
            color: #1f2937;
            background: #f3f4f6;
        }

        .page {
            max-width: 1100px;
            margin: 36px auto;
            padding: 0 24px;
        }

        .header,
        .toolbar,
        .table-wrap {
            background: #ffffff;
            border: 1px solid #e5e7eb;
            border-radius: 6px;
        }

        .header {
            padding: 22px 26px;
            margin-bottom: 16px;
        }

        .header h1 {
            margin: 0 0 8px;
            font-size: 24px;
            font-weight: 600;
        }

        .header a {
            color: #0f766e;
            text-decoration: none;
            font-size: 14px;
        }

        .toolbar {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
            justify-content: space-between;
            padding: 16px;
            margin-bottom: 16px;
        }

        .search {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            align-items: center;
        }

        input[type="text"] {
            width: 240px;
            max-width: 100%;
            padding: 9px 10px;
            border: 1px solid #d1d5db;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }

        .button {
            display: inline-block;
            padding: 9px 14px;
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

        .button.danger {
            border-color: #b91c1c;
            background: #b91c1c;
        }

        .alert {
            padding: 12px 14px;
            margin-bottom: 16px;
            border-radius: 4px;
            font-size: 14px;
        }

        .alert.success {
            color: #065f46;
            background: #d1fae5;
            border: 1px solid #a7f3d0;
        }

        .alert.error {
            color: #991b1b;
            background: #fee2e2;
            border: 1px solid #fecaca;
        }

        .table-wrap {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: #ffffff;
        }

        th,
        td {
            padding: 12px 14px;
            border-bottom: 1px solid #e5e7eb;
            text-align: left;
            white-space: nowrap;
            font-size: 14px;
        }

        th {
            color: #374151;
            background: #f9fafb;
            font-weight: 600;
        }

        td.desc,
        td.freeuse {
            white-space: normal;
            min-width: 160px;
        }

        .actions {
            display: flex;
            gap: 8px;
            align-items: center;
        }

        .actions form {
            margin: 0;
        }

        .empty {
            padding: 24px;
            color: #6b7280;
            text-align: center;
        }
    </style>
</head>
<body>
<main class="page">
    <section class="header">
        <h1>系统参数维护</h1>
        <a href="${pageContext.request.contextPath}/index">返回首页</a>
    </section>

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
</body>
</html>
