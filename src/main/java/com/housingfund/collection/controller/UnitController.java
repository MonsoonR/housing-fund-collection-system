package com.housingfund.collection.controller;

import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.service.UnitService;
import com.housingfund.collection.vo.UnitOpenForm;
import com.housingfund.collection.vo.UnitOpenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
