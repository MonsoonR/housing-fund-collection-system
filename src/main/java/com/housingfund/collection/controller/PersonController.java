package com.housingfund.collection.controller;

import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.service.PersonService;
import com.housingfund.collection.vo.PersonOpenForm;
import com.housingfund.collection.vo.PersonOpenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private static final String ID_TYPE_RESIDENT = "居民身份证";

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
            model.addAttribute("error", ex.getMessage());
            return "person/open";
        }
    }
}
