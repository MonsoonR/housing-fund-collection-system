<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>单位开户</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="panel">
        <h1>单位开户</h1>

        <c:if test="${not empty error}">
            <div class="alert"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/units/open"
              onsubmit="return validateUnitOpenForm(this);">
            <div class="form-section">
                <h2>单位基本信息</h2>
                <div class="grid">
                <div class="field full">
                    <label for="unitName">单位名称</label>
                    <input id="unitName" name="unitName" type="text" required maxlength="50"
                           value="${fn:escapeXml(unitOpenForm.unitName)}">
                </div>

                <div class="field full">
                    <label for="unitAddr">单位地址</label>
                    <input id="unitAddr" name="unitAddr" type="text" required maxlength="200"
                           value="${fn:escapeXml(unitOpenForm.unitAddr)}">
                </div>

                <div class="field">
                    <label for="orgCode">组织机构代码</label>
                    <input id="orgCode" name="orgCode" type="text" required maxlength="9" pattern="[A-Za-z0-9]{9}"
                           value="${fn:escapeXml(unitOpenForm.orgCode)}">
                    <div class="tip">长度 9 位，支持字母或数字。</div>
                </div>

                <div class="field">
                    <label for="salaryDate">发薪日期</label>
                    <input id="salaryDate" name="salaryDate" type="text" required maxlength="2"
                           pattern="0[1-9]|[12][0-9]|3[01]" value="${fn:escapeXml(unitOpenForm.salaryDate)}">
                    <div class="tip">请输入 01 到 31。</div>
                </div>

                <div class="field">
                    <label for="unitKind">单位类别</label>
                    <select id="unitKind" name="unitKind" required>
                        <option value="">请选择</option>
                        <option value="1" ${unitOpenForm.unitKind eq '1' ? 'selected' : ''}>企业</option>
                        <option value="2" ${unitOpenForm.unitKind eq '2' ? 'selected' : ''}>事业</option>
                        <option value="3" ${unitOpenForm.unitKind eq '3' ? 'selected' : ''}>机关</option>
                        <option value="4" ${unitOpenForm.unitKind eq '4' ? 'selected' : ''}>团体</option>
                        <option value="5" ${unitOpenForm.unitKind eq '5' ? 'selected' : ''}>其他</option>
                    </select>
                </div>

                <div class="field">
                    <label for="unitType">企业类型</label>
                    <select id="unitType" name="unitType" required>
                        <option value="">请选择</option>
                        <option value="110" ${unitOpenForm.unitType eq '110' ? 'selected' : ''}>国有经济</option>
                        <option value="120" ${unitOpenForm.unitType eq '120' ? 'selected' : ''}>集体经济</option>
                        <option value="130" ${unitOpenForm.unitType eq '130' ? 'selected' : ''}>股份合作企业</option>
                        <option value="140" ${unitOpenForm.unitType eq '140' ? 'selected' : ''}>联营企业</option>
                        <option value="150" ${unitOpenForm.unitType eq '150' ? 'selected' : ''}>有限责任公司</option>
                        <option value="160" ${unitOpenForm.unitType eq '160' ? 'selected' : ''}>股份有限公司</option>
                        <option value="170" ${unitOpenForm.unitType eq '170' ? 'selected' : ''}>私营企业</option>
                        <option value="190" ${unitOpenForm.unitType eq '190' ? 'selected' : ''}>其他企业</option>
                        <option value="200" ${unitOpenForm.unitType eq '200' ? 'selected' : ''}>港澳台商投资企业</option>
                        <option value="300" ${unitOpenForm.unitType eq '300' ? 'selected' : ''}>外商投资企业</option>
                        <option value="900" ${unitOpenForm.unitType eq '900' ? 'selected' : ''}>其他</option>
                    </select>
                </div>
                </div>
            </div>

            <div class="form-section">
                <h2>经办人信息</h2>
                <div class="grid">
                <div class="field">
                    <label for="phone">联系电话</label>
                    <input id="phone" name="phone" type="text" required maxlength="30"
                           value="${fn:escapeXml(unitOpenForm.phone)}">
                </div>

                <div class="field">
                    <label for="agentName">单位经办人</label>
                    <input id="agentName" name="agentName" type="text" required maxlength="50"
                           value="${fn:escapeXml(unitOpenForm.agentName)}">
                </div>

                <div class="field full">
                    <label for="agentIdCard">经办人身份证号码</label>
                    <input id="agentIdCard" name="agentIdCard" type="text" required maxlength="18"
                           pattern="[0-9]{17}[0-9Xx]" value="${fn:escapeXml(unitOpenForm.agentIdCard)}">
                </div>
                </div>
            </div>

            <div class="form-section">
                <h2>缴存比例</h2>
                <div class="grid">
                <div class="field">
                    <label for="unitRatio">单位比例</label>
                    <input id="unitRatio" name="unitRatio" type="text" required maxlength="5"
                           pattern="0\.(0[5-9][0-9]|1[01][0-9]|120)" value="${fn:escapeXml(unitOpenForm.unitRatio)}">
                    <div class="tip">范围 0.050 到 0.120。</div>
                </div>

                <div class="field">
                    <label for="perRatio">个人比例</label>
                    <input id="perRatio" name="perRatio" type="text" required maxlength="5"
                           pattern="0\.(0[5-9][0-9]|1[01][0-9]|120)" value="${fn:escapeXml(unitOpenForm.perRatio)}">
                    <div class="tip">范围 0.050 到 0.120。</div>
                </div>
                </div>
            </div>

            <div class="form-section">
                <h2>备注信息</h2>
                <div class="grid">
                <div class="field full">
                    <label for="remark">备注</label>
                    <textarea id="remark" name="remark" maxlength="200"><c:out value="${unitOpenForm.remark}"/></textarea>
                </div>
                </div>
            </div>

            <div class="actions">
                <button class="button" type="submit">提交开户</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </form>
    </section>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
