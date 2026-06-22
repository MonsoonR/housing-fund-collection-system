<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>住房公积金归集业务系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="page-title">
        <h1>住房公积金归集业务系统</h1>
        <p>提供系统参数维护、账户开户、资料修改和信息查询等归集业务办理。</p>
    </section>

    <section class="content-panel">
        <div class="table-scroll">
            <table class="data-table home-module-table">
                <thead>
                <tr>
                    <th>业务模块</th>
                    <th>业务对象</th>
                    <th>主要功能</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>系统参数维护</td>
                    <td>TB001</td>
                    <td>维护账号序号、参数说明和备用信息</td>
                    <td><a class="btn btn-secondary" href="${pageContext.request.contextPath}/params">进入</a></td>
                </tr>
                <tr>
                    <td>单位开户</td>
                    <td>TB002</td>
                    <td>新增缴存单位账户，生成单位公积金账号</td>
                    <td><a class="btn btn-secondary" href="${pageContext.request.contextPath}/units/open">进入</a></td>
                </tr>
                <tr>
                    <td>个人开户</td>
                    <td>TB003</td>
                    <td>手工开户与 Excel 批量开户</td>
                    <td><a class="btn btn-secondary" href="${pageContext.request.contextPath}/persons/open">进入</a></td>
                </tr>
                <tr>
                    <td>单位资料修改</td>
                    <td>TB002</td>
                    <td>查询单位账号并修改单位基础资料</td>
                    <td><a class="btn btn-secondary" href="${pageContext.request.contextPath}/units/edit">进入</a></td>
                </tr>
                <tr>
                    <td>个人资料修改</td>
                    <td>TB003</td>
                    <td>修改个人姓名、证件类型和证件号码</td>
                    <td><a class="btn btn-secondary" href="${pageContext.request.contextPath}/persons/edit">进入</a></td>
                </tr>
                <tr>
                    <td>单位信息查询</td>
                    <td>TB002</td>
                    <td>按单位账号或名称查询单位信息</td>
                    <td><a class="btn btn-secondary" href="${pageContext.request.contextPath}/units/query">进入</a></td>
                </tr>
                <tr>
                    <td>个人信息查询</td>
                    <td>TB003</td>
                    <td>按个人账号或证件号码查询个人信息</td>
                    <td><a class="btn btn-secondary" href="${pageContext.request.contextPath}/persons/query">进入</a></td>
                </tr>
                </tbody>
            </table>
        </div>
    </section>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
