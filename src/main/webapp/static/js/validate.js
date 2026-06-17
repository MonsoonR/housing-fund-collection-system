(function () {
    function trim(value) {
        return value == null ? "" : String(value).trim();
    }

    function isPositiveInteger(value) {
        return /^[1-9]\d*$/.test(value);
    }

    window.validateParamForm = function (form) {
        var seqname = trim(form.elements["seqname"].value);
        var seq = trim(form.elements["seq"].value);
        var maxseq = trim(form.elements["maxseq"].value);
        var seqDesc = trim(form.elements["seqDesc"].value);

        if (!seqname) {
            alert("键值信息不能为空");
            form.elements["seqname"].focus();
            return false;
        }
        if (!isPositiveInteger(seq)) {
            alert("当前序号必须为正整数");
            form.elements["seq"].focus();
            return false;
        }
        if (!isPositiveInteger(maxseq)) {
            alert("最大序号必须为正整数");
            form.elements["maxseq"].focus();
            return false;
        }
        if (Number(seq) > Number(maxseq)) {
            alert("当前序号不能大于最大序号");
            form.elements["seq"].focus();
            return false;
        }
        if (!seqDesc) {
            alert("描述不能为空");
            form.elements["seqDesc"].focus();
            return false;
        }
        return true;
    };

    window.validateUnitOpenForm = function (form) {
        var unitName = trim(form.elements["unitName"].value);
        var unitAddr = trim(form.elements["unitAddr"].value);
        var orgCode = trim(form.elements["orgCode"].value);
        var unitKind = trim(form.elements["unitKind"].value);
        var unitType = trim(form.elements["unitType"].value);
        var salaryDate = trim(form.elements["salaryDate"].value);
        var phone = trim(form.elements["phone"].value);
        var agentName = trim(form.elements["agentName"].value);
        var agentIdCard = trim(form.elements["agentIdCard"].value);
        var unitRatio = trim(form.elements["unitRatio"].value);
        var perRatio = trim(form.elements["perRatio"].value);

        if (!unitName) {
            alert("单位名称不能为空");
            form.elements["unitName"].focus();
            return false;
        }
        if (unitName.length > 50) {
            alert("单位名称不能超过50个字符");
            form.elements["unitName"].focus();
            return false;
        }
        if (!unitAddr) {
            alert("单位地址不能为空");
            form.elements["unitAddr"].focus();
            return false;
        }
        if (!/^[A-Za-z0-9]{9}$/.test(orgCode)) {
            alert("组织机构代码长度必须为9位");
            form.elements["orgCode"].focus();
            return false;
        }
        if (!unitKind) {
            alert("单位类别不能为空");
            form.elements["unitKind"].focus();
            return false;
        }
        if (!unitType) {
            alert("企业类型不能为空");
            form.elements["unitType"].focus();
            return false;
        }
        if (!/^(0[1-9]|[12][0-9]|3[01])$/.test(salaryDate)) {
            alert("发薪日期必须在01到31之间");
            form.elements["salaryDate"].focus();
            return false;
        }
        if (!phone) {
            alert("联系电话不能为空");
            form.elements["phone"].focus();
            return false;
        }
        if (!agentName) {
            alert("单位经办人不能为空");
            form.elements["agentName"].focus();
            return false;
        }
        if (!/^\d{17}[\dXx]$/.test(agentIdCard)) {
            alert("经办人身份证号码不正确");
            form.elements["agentIdCard"].focus();
            return false;
        }
        if (!isRatioInRange(unitRatio)) {
            alert("单位比例必须在0.050到0.120之间");
            form.elements["unitRatio"].focus();
            return false;
        }
        if (!isRatioInRange(perRatio)) {
            alert("个人比例必须在0.050到0.120之间");
            form.elements["perRatio"].focus();
            return false;
        }
        return true;
    };

    function isRatioInRange(value) {
        if (!/^0\.\d{3}$/.test(value)) {
            return false;
        }
        var ratio = Number(value);
        return ratio >= 0.050 && ratio <= 0.120;
    }
})();
