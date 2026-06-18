package com.housingfund.collection.controller;

import com.housingfund.collection.entity.UnitBasicInfo;
import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.service.UnitService;
import com.housingfund.collection.vo.UnitEditForm;
import com.housingfund.collection.vo.UnitEditResult;
import com.housingfund.collection.vo.UnitOpenForm;
import com.housingfund.collection.vo.UnitOpenResult;
import com.housingfund.collection.vo.UnitQueryForm;
import com.housingfund.collection.vo.UnitQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;

    @Autowired
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/open")
    public String openForm(Model model) {
        if (!model.containsAttribute("unitOpenForm")) {
            model.addAttribute("unitOpenForm", new UnitOpenForm());
        }
        return "unit/open";
    }

    @PostMapping("/open")
    public String open(@ModelAttribute("unitOpenForm") UnitOpenForm form,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            return "unit/open";
        }
        try {
            UnitOpenResult result = unitService.openUnit(form);
            model.addAttribute("receipt", result);
            return "unit/receipt";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            return "unit/open";
        }
    }

    @GetMapping("/query")
    public String queryForm(Model model) {
        if (!model.containsAttribute("unitQueryForm")) {
            model.addAttribute("unitQueryForm", new UnitQueryForm());
        }
        return "unit/query";
    }

    @PostMapping("/query")
    public String query(@ModelAttribute("unitQueryForm") UnitQueryForm form,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            return "unit/query";
        }
        try {
            List<UnitQueryResult> results = unitService.queryUnits(form);
            model.addAttribute("queryResults", results);
            model.addAttribute("searched", true);
            return "unit/query";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            return "unit/query";
        }
    }

    @GetMapping("/edit")
    public String editSearchForm(Model model) {
        if (!model.containsAttribute("unitEditForm")) {
            model.addAttribute("unitEditForm", new UnitEditForm());
        }
        return "unit/edit";
    }

    @GetMapping("/edit/form")
    public String editForm(@ModelAttribute("unitEditForm") UnitEditForm form,
                           BindingResult bindingResult,
                           Model model) {
        return loadEditForm(form, bindingResult, model);
    }

    @PostMapping("/edit/search")
    public String searchEditForm(@ModelAttribute("unitEditForm") UnitEditForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        return loadEditForm(form, bindingResult, model);
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("unitEditForm") UnitEditForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            model.addAttribute("unitLoaded", true);
            return "unit/edit";
        }
        try {
            UnitEditResult result = unitService.updateUnit(form);
            model.addAttribute("receipt", result);
            return "unit/edit-receipt";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("unitLoaded", true);
            return "unit/edit";
        }
    }

    private String loadEditForm(UnitEditForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            return "unit/edit";
        }
        try {
            UnitBasicInfo unit = unitService.getEditableUnit(form.getUnitAccNum());
            model.addAttribute("unitEditForm", buildEditForm(unit));
            model.addAttribute("unitLoaded", true);
            return "unit/edit";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            return "unit/edit";
        }
    }

    private UnitEditForm buildEditForm(UnitBasicInfo unit) {
        UnitEditForm form = new UnitEditForm();
        form.setUnitAccNum(unit.getUnitAccNum());
        form.setUnitName(unit.getUnitName());
        form.setUnitAddr(unit.getUnitAddr());
        form.setOrgCode(unit.getOrgCode());
        form.setUnitKind(unit.getUnitKind());
        form.setUnitType(unit.getUnitType());
        form.setSalaryDate(unit.getSalaryDate());
        form.setPhone(unit.getPhone());
        form.setAgentName(unit.getAgentName());
        form.setAgentIdCard(unit.getAgentIdCard());
        form.setRemark(unit.getRemark());
        return form;
    }
}
