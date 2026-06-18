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
        if (!isValidIdCard(agentIdCard)) {
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

    window.validatePersonOpenForm = function (form) {
        var unitAccNum = trim(form.elements["unitAccNum"].value);
        var perName = trim(form.elements["perName"].value);
        var idType = trim(form.elements["idType"].value);
        var idCard = trim(form.elements["idCard"].value);
        var baseNum = trim(form.elements["baseNum"].value);

        if (!/^\d{12}$/.test(unitAccNum)) {
            alert("单位账号长度必须为12位");
            form.elements["unitAccNum"].focus();
            return false;
        }
        if (!perName) {
            alert("个人姓名不能为空");
            form.elements["perName"].focus();
            return false;
        }
        if (/^[\u4e00-\u9fa5]+$/.test(perName) && perName.length > 12) {
            alert("个人姓名不能超过12个汉字");
            form.elements["perName"].focus();
            return false;
        }
        if (perName.length > 50) {
            alert("个人姓名不能超过50个字符");
            form.elements["perName"].focus();
            return false;
        }
        if (idType !== "居民身份证") {
            alert("证件类型目前只支持居民身份证");
            form.elements["idType"].focus();
            return false;
        }
        if (!isValidIdCard(idCard)) {
            alert("身份证号不正确");
            form.elements["idCard"].focus();
            return false;
        }
        if (!/^\d+(\.\d{1,2})?$/.test(baseNum) || Number(baseNum) <= 0) {
            alert("缴存基数必须大于0");
            form.elements["baseNum"].focus();
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

    function isValidIdCard(value) {
        if (!/^\d{17}[\dXx]$/.test(value)) {
            return false;
        }
        var year = Number(value.substring(6, 10));
        var month = Number(value.substring(10, 12));
        var day = Number(value.substring(12, 14));
        var birthDate = new Date(year, month - 1, day);
        if (birthDate.getFullYear() !== year ||
                birthDate.getMonth() !== month - 1 ||
                birthDate.getDate() !== day ||
                birthDate > new Date()) {
            return false;
        }
        var weights = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
        var checkCodes = ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"];
        var sum = 0;
        for (var i = 0; i < weights.length; i++) {
            sum += Number(value.charAt(i)) * weights[i];
        }
        return checkCodes[sum % 11] === value.charAt(17).toUpperCase();
    }
})();
