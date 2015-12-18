package com.peta2kuba.pa165_haunted_houses.mvc.controllers;

import com.peta2kuba.pa165_haunted_houses.dto.HaunterDTO;
import com.peta2kuba.pa165_haunted_houses.facade.HaunterFacade;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author petr.melicherik
 */
@Controller
@RequestMapping("/haunter")
public class HaunterController {

    final static Logger logger = LoggerFactory.getLogger(AbilityController.class);

    @Autowired
    private HaunterFacade haunterFacade;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("haunters", haunterFacade.findAll());
        return "haunter/list";
    }

    @RequestMapping(value = "/list/active", method = RequestMethod.GET)
    public String listActive(Model model) {
        model.addAttribute("haunters", haunterFacade.findActiveHaunters());
        return "haunter/list";
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable long id, Model model) {
        model.addAttribute("haunters", haunterFacade.findAll());
        model.addAttribute("selectedHaunter", new HaunterDTO());
        model.addAttribute("haunter", haunterFacade.findById(id));
        return "haunter/detail";
    }
    
    
    @RequestMapping(value = "/compare/{id}", method = RequestMethod.POST)
    public String compare(@PathVariable long id, @ModelAttribute("selectedHaunter") HaunterDTO haunterDTOselected, Model model, UriComponentsBuilder uriBuilder, RedirectAttributes redirectAttributes) {
        HaunterDTO haunterDTO = haunterFacade.findById(id);
        logger.error("" + haunterDTOselected.toString());
        
        HaunterDTO haunterDTO2 = haunterFacade.findById(haunterDTOselected.getId());

        logger.error(haunterDTO.toString());
        logger.error(haunterDTO2.toString());
        long result = haunterFacade.isHaunterStronger(haunterDTO, haunterDTO2);
        String modelAttr;
        if (result > 0) {
            modelAttr = "Haunter \"" + haunterDTO.getName() + "\" is stronger.";
        } else if (result == 0) {
            modelAttr = "Haunters power is equal";
        } else {
            modelAttr = "Haunter \"" + haunterDTO.getName() + "\" is weaker.";
        }
        model.addAttribute("value", modelAttr);
        return "haunter/compare";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable long id, Model model, UriComponentsBuilder uriBuilder, RedirectAttributes redirectAttributes) {
        HaunterDTO haunterDTO = haunterFacade.findById(id);
        haunterFacade.removeHaunter(haunterDTO);
        redirectAttributes.addFlashAttribute("alert_success", "Haunter \"" + haunterDTO.getName() + "\" was deleted.");
        return "redirect:" + uriBuilder.path("/haunter/list").toUriString();
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String newHaunterForm(Model model) {
        model.addAttribute("newHaunter", new HaunterDTO());
        return "haunter/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String newHaunterCreate(@Valid @ModelAttribute("newHaunter") HaunterDTO formBean, BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        logger.debug("create(newHaunter={})", formBean);
        //in case of validation error forward back to the the form
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                logger.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                logger.trace("FieldError: {}", fe);
            }
            return "haunter/add";
        }
        //create person
        haunterFacade.createHaunter(formBean);
        //report success
        redirectAttributes.addFlashAttribute("alert_success", "Haunter was created");
        return "redirect:" + uriBuilder.path("haunter/list").toUriString();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editHaunter(@PathVariable long id, Model model) {
        model.addAttribute("haunter", haunterFacade.findById(id));
        return "haunter/edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editPerson(@Valid @ModelAttribute("haunter") HaunterDTO haunterDTO, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, @PathVariable long id) {

        //in case of validation error forward back to the the form
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                logger.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                logger.trace("FieldError: {}", fe);
            }
            redirectAttributes.addFlashAttribute("errors", bindingResult);
            return "/haunter/edit";
        }

        haunterFacade.editHaunter(haunterDTO);
        redirectAttributes.addFlashAttribute("alert_success", "Haunter was updated");
        return "redirect:" + uriBuilder.path("/haunter/list").toUriString();
    }
}
