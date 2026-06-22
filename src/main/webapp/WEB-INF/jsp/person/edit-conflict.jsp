<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料修改冲突确认</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />
    <div class="app-container">
        <jsp:include page="/WEB-INF/jsp/common/topbar.jsp" />
        <main class="main-content">
            <div class="page-header">
                <h1>冲突确认与风险评估</h1>
            </div>

            <section class="panel">
                <div class="conflict-banner">
                    <span>⚠️ 警示：该证件号码已被其他个人账户占用！请进行业务审核。</span>
                </div>

                <h3 class="form-section-title">当前目标账户 (待修改)</h3>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>个人公积金账号</th>
                            <th>当前姓名</th>
                            <th>修改后的姓名</th>
                            <th style="color:var(--brand);">拟变更证件号码</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td><c:out value="${conflict.currentPerAccNum}"/></td>
                            <td><c:out value="${conflict.currentPerName}"/></td>
                            <td><c:out value="${conflict.newPerName}"/></td>
                            <td style="font-weight: 700; color:var(--brand);"><c:out value="${conflict.newIdCard}"/></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <h3 class="form-section-title">被占用账户 (冲突源)</h3>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>占用个人公积金账号</th>
                            <th>占用姓名</th>
                            <th>证件号码</th>
                            <th>所属单位</th>
                            <th>账户状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td><c:out value="${conflict.occupiedPerAccNum}"/></td>
                            <td><c:out value="${conflict.occupiedPerName}"/></td>
                            <td><c:out value="${conflict.occupiedIdCard}"/></td>
                            <td>
                                [<c:out value="${conflict.occupiedUnitAccNum}"/>] 
                                <c:out value="${conflict.occupiedUnitName}"/>
                            </td>
                            <td>
                                <span class="badge ${conflict.occupiedStatusText eq '正常' ? 'badge-success' : 'badge-warning'}">
                                    <c:out value="${conflict.occupiedStatusText}"/>
                                </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="note">
                    <strong>强制变更后的结果说明：</strong><br>
                    1. 系统将剥离原有占用账户的证件号码（原证件号码首位被强制替换为 8）。<br>
                    2. 同时将新建一个证件号码首位为 9 的错误临时账户保存该占用账户的历史冗余信息。<br>
                    3. 本操作会直接涉及两个独立自然人公积金账号的证件真实性修改，属于<strong>高风险金融柜面业务</strong>，请仔细审验相关资料及身份证件原件！
                </div>

                <div class="actions">
                    <form method="post" action="${pageContext.request.contextPath}/persons/edit/force" style="display:inline-block;">
                        <input type="hidden" name="perAccNum" value="${fn:escapeXml(personEditForm.perAccNum)}">
                        <input type="hidden" name="unitAccNum" value="${fn:escapeXml(personEditForm.unitAccNum)}">
                        <input type="hidden" name="unitName" value="${fn:escapeXml(personEditForm.unitName)}">
                        <input type="hidden" name="perName" value="${fn:escapeXml(personEditForm.perName)}">
                        <input type="hidden" name="idType" value="${fn:escapeXml(personEditForm.idType)}">
                        <input type="hidden" name="idCard" value="${fn:escapeXml(personEditForm.idCard)}">
                        <button class="button danger" type="submit">确认强制变更 (高风险)</button>
                    </form>

                    <form method="post" action="${pageContext.request.contextPath}/persons/edit/back" style="display:inline-block;">
                        <input type="hidden" name="perAccNum" value="${fn:escapeXml(personEditForm.perAccNum)}">
                        <input type="hidden" name="unitAccNum" value="${fn:escapeXml(personEditForm.unitAccNum)}">
                        <input type="hidden" name="unitName" value="${fn:escapeXml(personEditForm.unitName)}">
                        <input type="hidden" name="perName" value="${fn:escapeXml(personEditForm.perName)}">
                        <input type="hidden" name="idType" value="${fn:escapeXml(personEditForm.idType)}">
                        <input type="hidden" name="idCard" value="${fn:escapeXml(personEditForm.idCard)}">
                        <button class="button secondary" type="submit">返回修改</button>
                    </form>
                </div>
            </section>
        </main>
    </div>
</div>
</body>
</html>
