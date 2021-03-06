/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peta2kuba.pa165_haunted_houses.mvc.controllers;

import com.peta2kuba.pa165_haunted_houses.dto.AbilityDTO;
import com.peta2kuba.pa165_haunted_houses.dto.PersonDTO;
import com.peta2kuba.pa165_haunted_houses.facade.AbilityFacade;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author petr.melicherik
 */
@Controller
@RequestMapping("/ability")
public class AbilityController {

    final static Logger logger = LoggerFactory.getLogger(AbilityController.class);

    @Autowired
    private AbilityFacade abilityFacade;

    /**
     * List all abilities
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("abilities", abilityFacade.findAll());
        return "ability/list";
    }

    /**
     * Ability detail
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable long id, Model model) {
        model.addAttribute("ability", abilityFacade.findById(id));
        return "ability/detail";
    }

    /**
     * Delete ability
     * @param id
     * @param model
     * @param uriBuilder
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable long id, Model model, UriComponentsBuilder uriBuilder, RedirectAttributes redirectAttributes) {
        AbilityDTO abilityDTO = abilityFacade.findById(id);
        if (abilityDTO != null) {
            abilityFacade.remove(abilityDTO);
        }
        redirectAttributes.addFlashAttribute("alert_success", "Ability \"" + abilityDTO.getName() + "\" was deleted.");
        return "redirect:" + uriBuilder.path("/ability/list").toUriString();
    }

    /**
     * Add ability
     * @param model
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String newAbilityForm(Model model) {
        model.addAttribute("newAbility", new AbilityDTO());
        return "ability/add";
    }

    /**
     * Add new ability
     * @param formBean
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @param uriBuilder
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String newAbilityCreate(@Valid @ModelAttribute("newAbility") AbilityDTO formBean, BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        logger.debug("create(newAbility={})", formBean);
        //in case of validation error forward back to the the form
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                logger.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                logger.trace("FieldError: {}", fe);
            }
            return "ability/add";
        }
        //create person
        abilityFacade.create(formBean);
        //report success
        redirectAttributes.addFlashAttribute("alert_success", "Ability was created");
        return "redirect:" + uriBuilder.path("/ability/list").toUriString();
    }

    /**
     * Edit existing ability
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editAbility(@PathVariable long id, Model model) {
        model.addAttribute("ability", abilityFacade.findById(id));
        return "ability/edit";
    }

    /**
     * Edit existing ability
     * @param abilityDTO
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @param uriBuilder
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editPerson(@Valid @ModelAttribute("ability") AbilityDTO abilityDTO, BindingResult bindingResult, Model model,
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
            return "/ability/edit";
        }

        abilityFacade.edit(abilityDTO);
        redirectAttributes.addFlashAttribute("alert_success", "Ability was updated");
        return "redirect:" + uriBuilder.path("/ability/list").toUriString();
    }
    
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")  // 409
    @ExceptionHandler(ConstraintViolationException.class)
    public void conflict() {
        // Nothing to do
    }
}
