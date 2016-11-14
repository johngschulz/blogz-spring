package org.launchcode.blogz.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		
		// get parameters from request
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		String error = null;
		
		// set boolean validation parameters (username, password, verify(password))
		boolean validName = isValidUsername(username);
		boolean validPass = isValidPassword(password);
		boolean matchingPass = password.equals(verify);		
		User isNewUser = userDao.findByUsername(username);
		
		// validates and returns error message in signup if error
		if(isNewUser != null){
			error = "That username already exist, try again";
			model.addAttribute("username_error", error);
			return "signup";
		}
		if(validName != true){
			error = "That username is invalid, try again";
			model.addAttribute("username_error", error);
			return "signup";
		}
		if(validPass != true){
			error = "That password is not valid";
			model.addAttribute("password_error", error);
			return "signup";
		}
		if(matchingPass != true){
			error = "Your passwords don't match";
			model.addAttribute("verify_error", error);
			return "signup";
		}
		//creates valid user, starts session and redirects
		User user = new User(username, password);
		userDao.save(user);
		model.addAttribute("username", username);
		HttpSession thisSession = request.getSession();
		setUserInSession(thisSession, user);
		return "redirect:blog/newpost";
		}
	
	// method to see if username matches the regex pattern
	public static boolean isValidUsername(String username) {
		Pattern validUsernamePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]{4,11}");
		Matcher matcher = validUsernamePattern.matcher(username);
		return matcher.matches();
	}
	
	// method to see if password matches the regex pattern
	public static boolean isValidPassword(String password) {
		Pattern validUsernamePattern = Pattern.compile("(\\S){6,20}");
		Matcher matcher = validUsernamePattern.matcher(password);
		return matcher.matches();
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		
		//get parameters from request
		String username = request.getParameter("username");
		model.addAttribute("username", username);
		String password = request.getParameter("password");
		
		//get user by username
		User user = userDao.findByUsername(username);
		// check if password is correct
		boolean matches = user.isMatchingPassword(password);
		
		// log them in, if all is good (i.e. setting the user in the session)
		if(user != null && matches == true){
		HttpSession thisSession = request.getSession();
		setUserInSession(thisSession, user);
		return "redirect:blog/newpost";
		}
		else{
			String error = "Invalid username or password";
			model.addAttribute("error", error);
			return "login";
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
