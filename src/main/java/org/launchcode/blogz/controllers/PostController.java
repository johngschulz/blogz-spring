package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost
		
		// get request parameters
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		String error = null;
		User user = getUserFromSession(request.getSession());

		// validate parameters
		// if not valid, send back to the form, with error message

		if (title == null){
			error = "Please enter a value for title";
			model.addAttribute("error", error);
			return "newpost";
		}
		if (body == null){
			error = "Please add a body for your post";
			model.addAttribute("error", error);
			return "newpost";
		}
		model.addAttribute("value", title);
		model.addAttribute("body", body);
		// if valid, create new Post
		Post post = new Post(title, body, user);
		postDao.save(post);
		String username = user.getUsername();
		int uid = post.getUid();
		
		return ("redirect:/blog/" + username + "/"+ uid); // TODO - this redirect should go to the new post's page  		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		
		// get the given post
		Post post = postDao.findByUid(uid);
		// pass the post into the template
		model.addAttribute("post", post);
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		
		// get all of the user's posts List<listofpost>
		User user = userDao.findByUsername(username);

		List<Post> posts = user.getPosts();
		// pass the posts into the template
		model.addAttribute("posts", posts);
		
		return "blog";
	}
	
}
