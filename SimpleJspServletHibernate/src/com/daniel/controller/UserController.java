package com.daniel.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.daniel.dao.UserDao;
import com.daniel.model.User;

/**
 * Servlet implementation class UserController
 */
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String EDIT_JSP = "/insert.jsp";
	private static String SHOWALL_JSP = "/showAll.jsp";
	private UserDao dao;
	private static Logger logger = Logger.getLogger(UserController.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super();
        dao = new UserDao();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
		String action = request.getParameter("action");
		if (action.equalsIgnoreCase("delete")){
			forward = SHOWALL_JSP;
			int userId = Integer.parseInt(request.getParameter("userId"));
			dao.deleteUser(userId);
			request.setAttribute("users", dao.getAllUsers());
			
		} else if (action.equalsIgnoreCase("edit")){
			forward = EDIT_JSP;
			int userId = Integer.parseInt(request.getParameter("userId"));
			User user = dao.getUserById(userId);
			request.setAttribute("user", user);
		} else if (action.equalsIgnoreCase("showAll")){
			forward = SHOWALL_JSP;
			request.setAttribute("users", dao.getAllUsers());
		} else {
			forward = EDIT_JSP;
		}
		RequestDispatcher view = request.getRequestDispatcher(forward);
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		user.setFirstName(request.getParameter("firstName"));
		user.setLastName(request.getParameter("lastName"));
		try {
			Date dob = new SimpleDateFormat("MM/dd/yyyy").parse(request.getParameter("dob"));
			user.setDob(dob);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		user.setEmail(request.getParameter("email"));
		String userid = request.getParameter("userid");
		if(userid == null || userid.isEmpty())
		{
			logger.info("add user");
			dao.addUser(user);
		}
		else
		{
			user.setUserid(Integer.parseInt(userid));
			logger.info("update user " + userid);
			dao.updateUser(user);
		}
		RequestDispatcher view = request.getRequestDispatcher(SHOWALL_JSP);
		request.setAttribute("users", dao.getAllUsers());
		view.forward(request, response);
	}

}
