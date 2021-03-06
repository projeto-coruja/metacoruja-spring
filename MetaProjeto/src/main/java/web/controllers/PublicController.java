package web.controllers;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
public class PublicController {
 
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String root(ModelMap model, Principal principal ) {
 
		return "public";
 
	}
	
	@RequestMapping(value="/public", method = RequestMethod.GET)
	public String accessPublic(ModelMap model, Principal principal ) {
 
		return "public";
 
	}
 
}