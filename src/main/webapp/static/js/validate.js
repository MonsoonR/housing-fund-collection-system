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
})();
