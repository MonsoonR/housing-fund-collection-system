package com.housingfund.collection.controller;

import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.service.PersonService;
import com.housingfund.collection.vo.PersonBatchImportResult;
import com.housingfund.collection.vo.PersonEditForm;
import com.housingfund.collection.vo.PersonEditResult;
import com.housingfund.collection.vo.PersonOpenForm;
import com.housingfund.collection.vo.PersonOpenResult;
import com.housingfund.collection.vo.PersonQueryForm;
import com.housingfund.collection.vo.PersonQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private static final String ID_TYPE_RESIDENT = "01身份证";

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/open")
    public String openForm(Model model) {
        if (!model.containsAttribute("personOpenForm")) {
            PersonOpenForm form = new PersonOpenForm();
            form.setIdType(ID_TYPE_RESIDENT);
            model.addAttribute("personOpenForm", form);
        }
        return "person/open";
    }

    @GetMapping("/open/unit")
    public String openUnitInfo(@ModelAttribute("personOpenForm") PersonOpenForm form,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            return "person/open";
        }
        try {
            PersonOpenForm unitForm = personService.getOpenUnitInfo(form.getUnitAccNum());
            model.addAttribute("personOpenForm", unitForm);
            model.addAttribute("unitLoaded", true);
            return "person/open";
        } catch (BusinessException ex) {
            form.setIdType(ID_TYPE_RESIDENT);
            model.addAttribute("personOpenForm", form);
            model.addAttribute("error", ex.getMessage());
            return "person/open";
        }
    }

    @PostMapping("/open")
    public String open(@ModelAttribute("personOpenForm") PersonOpenForm form,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            return "person/open";
        }
        try {
            PersonOpenResult result = personService.openPerson(form);
            model.addAttribute("receipt", result);
            return "person/receipt";
        } catch (BusinessException ex) {
            model.addAttribute("unitLoaded", form.getUnitName() != null);
            model.addAttribute("error", ex.getMessage());
            return "person/open";
        }
    }

    @PostMapping("/open/import")
    public String importPersons(@RequestParam("excelFile") MultipartFile excelFile,
                                @RequestParam("batchUnitAccNum") String batchUnitAccNum,
                                Model model) {
        PersonOpenForm form = new PersonOpenForm();
        form.setIdType(ID_TYPE_RESIDENT);
        form.setUnitAccNum(batchUnitAccNum);
        model.addAttribute("personOpenForm", form);
        try {
            if (excelFile == null || excelFile.isEmpty()) {
                throw new BusinessException("请上传Excel文件");
            }
            PersonBatchImportResult result = personService.importPersons(
                    excelFile.getInputStream(), excelFile.getOriginalFilename(), batchUnitAccNum);
            model.addAttribute("importResult", result);
            return "person/open";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            return "person/open";
        } catch (IOException ex) {
            model.addAttribute("error", "Excel文件读取失败");
            return "person/open";
        }
    }

    @GetMapping("/query")
    public String queryForm(Model model) {
        if (!model.containsAttribute("personQueryForm")) {
            model.addAttribute("personQueryForm", new PersonQueryForm());
        }
        return "person/query";
    }

    @PostMapping("/query")
    public String query(@ModelAttribute("personQueryForm") PersonQueryForm form,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            return "person/query";
        }
        try {
            PersonQueryResult result = personService.queryPerson(form);
            model.addAttribute("queryResult", result);
            model.addAttribute("searched", true);
            return "person/query";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            return "person/query";
        }
    }

    @GetMapping("/edit")
    public String editSearchForm(Model model) {
        if (!model.containsAttribute("personEditForm")) {
            PersonEditForm form = new PersonEditForm();
            form.setIdType(ID_TYPE_RESIDENT);
            model.addAttribute("personEditForm", form);
        }
        return "person/edit";
    }

    @GetMapping("/edit/form")
    public String editForm(@ModelAttribute("personEditForm") PersonEditForm form,
                           BindingResult bindingResult,
                           Model model) {
        return loadEditForm(form, bindingResult, model);
    }

    @PostMapping("/edit/search")
    public String searchEditForm(@ModelAttribute("personEditForm") PersonEditForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        return loadEditForm(form, bindingResult, model);
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("personEditForm") PersonEditForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            model.addAttribute("personLoaded", true);
            return "person/edit";
        }
        try {
            PersonEditResult result = personService.updatePerson(form);
            if (result.getConflictResult() != null) {
                model.addAttribute("personEditForm", form);
                model.addAttribute("conflict", result.getConflictResult());
                return "person/edit-conflict";
            }
            model.addAttribute("receipt", result);
            return "person/edit-receipt";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("personLoaded", true);
            return "person/edit";
        }
    }

    @PostMapping("/edit/force")
    public String forceUpdate(@ModelAttribute("personEditForm") PersonEditForm form,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            model.addAttribute("personLoaded", true);
            return "person/edit";
        }
        try {
            PersonEditResult result = personService.forceUpdatePerson(form);
            model.addAttribute("receipt", result);
            return "person/edit-receipt";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("personLoaded", true);
            return "person/edit";
        }
    }

    @PostMapping("/edit/back")
    public String backToEdit(@ModelAttribute("personEditForm") PersonEditForm form,
                             Model model) {
        model.addAttribute("personEditForm", form);
        model.addAttribute("personLoaded", true);
        return "person/edit";
    }

    private String loadEditForm(PersonEditForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "表单数据格式不正确");
            return "person/edit";
        }
        try {
            PersonEditForm editableForm = personService.getEditablePerson(form.getPerAccNum());
            model.addAttribute("personEditForm", editableForm);
            model.addAttribute("personLoaded", true);
            return "person/edit";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            return "person/edit";
        }
    }
}
