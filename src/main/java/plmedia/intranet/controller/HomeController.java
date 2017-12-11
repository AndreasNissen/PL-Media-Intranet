package plmedia.intranet.controller;

import java.security.Principal;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
* @author Jonas Holm
 *@author Andreas Nissen
* */

@Controller
public class HomeController {


  /**
  * Gives a good overview over where the default entry point is
  * */
  @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
  public String defaultEntryPoint(Model model, Principal principal) {




    /**
     * Dirigere de rette users, i den rette retning
     * Her er det vigtigt at type har enten værdien ROLE_EMP eller ROLE_PAR
     * */
    if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMP"))){

      return "redirect:/admin";
    }
    return "redirect:/parents";
  }

  @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
  public String login(){
    return "login";
  }





}
