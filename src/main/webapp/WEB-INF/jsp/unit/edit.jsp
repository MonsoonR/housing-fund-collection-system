<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>单位资料修改</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <script src="${pageContext.request.contextPath}/static/js/validate.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/app-shell-start.jsp"/>
    <section class="panel">
        <h1>单位资料修改</h1>
        <p class="page-desc">按单位公积金账号查询后，修改允许变更的单位基础资料。</p>

        <c:if test="${not empty error}">
            <div class="alert"><c:out value="${error}"/></div>
        </c:if>

        <form method="get" action="${pageContext.request.contextPath}/units/edit/form"
              onsubmit="return validateUnitEditSearchForm(this);">
            <div class="grid">
                <div class="field">
                    <label for="searchUnitAccNum">单位公积金账号<span class="required">*</span></label>
                    <input id="searchUnitAccNum" name="unitAccNum" type="text" required maxlength="12"
                           pattern="[0-9]{12}" value="${fn:escapeXml(unitEditForm.unitAccNum)}">
                    <div class="tip">请输入 12 位单位公积金账号。</div>
                </div>
            </div>

            <div class="actions">
                <button class="button" type="submit">查询单位</button>
                <a class="button secondary" href="${pageContext.request.contextPath}/index">返回首页</a>
            </div>
        </form>
    </section>

    <c:if test="${unitLoaded}">
        <section class="panel">
            <h2>修改资料</h2>

            <form method="post" action="${pageContext.request.contextPath}/units/edit"
                  onsubmit="return validateUnitEditForm(this);">
                <input type="hidden" name="originalUnitName" value="${fn:escapeXml(unitEditForm.unitName)}">
                <input type="hidden" name="originalUnitAddr" value="${fn:escapeXml(unitEditForm.unitAddr)}">
                <input type="hidden" name="originalOrgCode" value="${fn:escapeXml(unitEditForm.orgCode)}">
                <input type="hidden" name="originalUnitKind" value="${fn:escapeXml(unitEditForm.unitKind)}">
                <input type="hidden" name="originalUnitType" value="${fn:escapeXml(unitEditForm.unitType)}">
                <input type="hidden" name="originalSalaryDate" value="${fn:escapeXml(unitEditForm.salaryDate)}">
                <input type="hidden" name="originalPhone" value="${fn:escapeXml(unitEditForm.phone)}">
                <input type="hidden" name="originalAgentName" value="${fn:escapeXml(unitEditForm.agentName)}">
                <input type="hidden" name="originalAgentIdCard" value="${fn:escapeXml(unitEditForm.agentIdCard)}">
                <input type="hidden" name="originalRemark" value="${fn:escapeXml(unitEditForm.remark)}">

                <div class="form-section">
                    <h2>单位基本信息</h2>
                    <div class="grid">
                    <div class="field">
                        <label for="editUnitAccNum">单位公积金账号</label>
                        <input id="editUnitAccNum" name="unitAccNum" type="text" readonly
                               value="${fn:escapeXml(unitEditForm.unitAccNum)}">
                    </div>

                    <div class="field">
                        <label for="orgCode">组织机构代码<span class="required">*</span></label>
                        <input id="orgCode" name="orgCode" type="text" required maxlength="9" pattern="[A-Za-z0-9]{9}"
                               value="${fn:escapeXml(unitEditForm.orgCode)}">
                        <div class="tip">长度 9 位，支持字母或数字。</div>
                    </div>

                    <div class="field full">
                        <label for="unitName">单位名称<span class="required">*</span></label>
                        <input id="unitName" name="unitName" type="text" required maxlength="50"
                               value="${fn:escapeXml(unitEditForm.unitName)}">
                    </div>

                    <div class="field full">
                        <label for="unitAddr">单位地址<span class="required">*</span></label>
                        <input id="unitAddr" name="unitAddr" type="text" required maxlength="200"
                               value="${fn:escapeXml(unitEditForm.unitAddr)}">
                    </div>

                    <div class="field">
                        <label for="unitKind">单位类别<span class="required">*</span></label>
                        <select id="unitKind" name="unitKind" required>
                            <option value="">请选择</option>
                            <option value="1" ${unitEditForm.unitKind eq '1' ? 'selected' : ''}>企业</option>
                            <option value="2" ${unitEditForm.unitKind eq '2' ? 'selected' : ''}>事业</option>
                            <option value="3" ${unitEditForm.unitKind eq '3' ? 'selected' : ''}>机关</option>
                            <option value="4" ${unitEditForm.unitKind eq '4' ? 'selected' : ''}>团体</option>
                            <option value="5" ${unitEditForm.unitKind eq '5' ? 'selected' : ''}>其他</option>
                        </select>
                    </div>

                    <div class="field">
                        <label for="unitType">企业类型<span class="required">*</span></label>
                        <select id="unitType" name="unitType" required>
                            <option value="">请选择</option>
                            <option value="110" ${unitEditForm.unitType eq '110' ? 'selected' : ''}>国有经济</option>
                            <option value="120" ${unitEditForm.unitType eq '120' ? 'selected' : ''}>集体经济</option>
                            <option value="130" ${unitEditForm.unitType eq '130' ? 'selected' : ''}>股份合作企业</option>
                            <option value="140" ${unitEditForm.unitType eq '140' ? 'selected' : ''}>联营企业</option>
                            <option value="150" ${unitEditForm.unitType eq '150' ? 'selected' : ''}>有限责任公司</option>
                            <option value="160" ${unitEditForm.unitType eq '160' ? 'selected' : ''}>股份有限公司</option>
                            <option value="170" ${unitEditForm.unitType eq '170' ? 'selected' : ''}>私营企业</option>
                            <option value="190" ${unitEditForm.unitType eq '190' ? 'selected' : ''}>其他企业</option>
                            <option value="200" ${unitEditForm.unitType eq '200' ? 'selected' : ''}>港澳台商投资企业</option>
                            <option value="300" ${unitEditForm.unitType eq '300' ? 'selected' : ''}>外商投资企业</option>
                            <option value="900" ${unitEditForm.unitType eq '900' ? 'selected' : ''}>其他</option>
                        </select>
                    </div>

                    <div class="field">
                        <label for="salaryDate">发薪日期<span class="required">*</span></label>
                        <input id="salaryDate" name="salaryDate" type="text" required maxlength="2"
                               pattern="0[1-9]|[12][0-9]|3[01]"
                               value="${fn:escapeXml(unitEditForm.salaryDate)}">
                        <div class="tip">请输入 01 到 31。</div>
                    </div>
                    </div>
                </div>

                <div class="form-section">
                    <h2>经办人信息</h2>
                    <div class="grid">
                    <div class="field">
                        <label for="phone">联系电话<span class="required">*</span></label>
                        <input id="phone" name="phone" type="text" required maxlength="30"
                               value="${fn:escapeXml(unitEditForm.phone)}">
                    </div>

                    <div class="field">
                        <label for="agentName">单位经办人<span class="required">*</span></label>
                        <input id="agentName" name="agentName" type="text" required maxlength="50"
                               value="${fn:escapeXml(unitEditForm.agentName)}">
                    </div>

                    <div class="field">
                        <label for="agentIdCard">经办人身份证号码<span class="required">*</span></label>
                        <input id="agentIdCard" name="agentIdCard" type="text" required maxlength="18"
                               pattern="[0-9]{17}[0-9Xx]" value="${fn:escapeXml(unitEditForm.agentIdCard)}">
                    </div>
                    </div>
                </div>

                <div class="form-section">
                    <h2>备注信息</h2>
                    <div class="grid">
                    <div class="field full">
                        <label for="remark">备注</label>
                        <textarea id="remark" name="remark" maxlength="200"><c:out value="${unitEditForm.remark}"/></textarea>
                    </div>
                    </div>
                </div>

                <div class="actions">
                    <button class="button" type="submit">提交修改</button>
                    <a class="button secondary" href="${pageContext.request.contextPath}/units/edit">重新查询</a>
                </div>
            </form>
        </section>
    </c:if>
<jsp:include page="/WEB-INF/jsp/common/app-shell-end.jsp"/>
</body>
</html>
