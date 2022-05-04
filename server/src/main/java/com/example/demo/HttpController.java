package com.example.demo;



import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


class Answer {
	public String data;
}


@RestController
public class HttpController extends HttpServlet {
	
	@Autowired
	private UserRepository userRepository;

	
	@RequestMapping("/index")
	public Answer index(HttpServletRequest req, HttpServletResponse res) {
		
		String val = req.getParameter("foo");
		System.out.print(val);
		
		Answer a = new Answer();
		a.data = "some answer";
		
		return a;
	}
	
	
	@GetMapping("/user/get-one")
	public User getUser(HttpServletRequest req, HttpServletResponse res) {
		
		Integer id = Integer.parseInt(req.getParameter("id"));
		Optional<User> user = userRepository.findById(id);
		return user.get();
	}
	
	
	public boolean saveUser() {
		return false;
	}
}
