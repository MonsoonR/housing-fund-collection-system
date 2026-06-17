package com.housingfund.collection.controller;

import com.housingfund.collection.entity.SystemParam;
import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/params")
public class ParamController {

    private final ParamService paramService;

    @Autowired
    public ParamController(ParamService paramService) {
        this.paramService = paramService;
    }

    @GetMapping
    public String list(@RequestParam(value = "seqname", required = false) String seqname, Model model) {
        model.addAttribute("params", paramService.findBySeqnameLike(seqname));
        model.addAttribute("querySeqname", seqname);
        return "param/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("systemParam", new SystemParam());
        model.addAttribute("mode", "create");
        return "param/form";
    }

    @PostMapping
    public String add(@ModelAttribute("systemParam") SystemParam systemParam,
                      Model model,
                      RedirectAttributes redirectAttributes) {
        try {
            paramService.add(systemParam);
            redirectAttributes.addFlashAttribute("success", "系统参数新增成功");
            return "redirect:/params";
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("mode", "create");
            return "param/form";
        }
    }

    @GetMapping("/{seqname}/edit")
    public String editForm(@PathVariable("seqname") String seqname,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("systemParam", paramService.getBySeqname(seqname));
            model.addAttribute("mode", "edit");
            return "param/form";
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/params";
        }
    }

    @PostMapping("/{seqname}/update")
    public String update(@PathVariable("seqname") String seqname,
                         @ModelAttribute("systemParam") SystemParam systemParam,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            paramService.update(seqname, systemParam);
            redirectAttributes.addFlashAttribute("success", "系统参数修改成功");
            return "redirect:/params";
        } catch (BusinessException ex) {
            systemParam.setSeqname(seqname);
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("mode", "edit");
            return "param/form";
        }
    }

    @PostMapping("/{seqname}/delete")
    public String delete(@PathVariable("seqname") String seqname, RedirectAttributes redirectAttributes) {
        try {
            paramService.delete(seqname);
            redirectAttributes.addFlashAttribute("success", "系统参数删除成功");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/params";
    }
}
