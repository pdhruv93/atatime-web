package com.heroku.dhruv.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.heroku.dhruv.util.DBWork;

@WebServlet("/fetchActivityDetailsFromDB")
public class FetchActivityDetailsFromDBServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
 
    String activity=request.getParameter("activity");
    
    
    ArrayList<String> currUSersList=DBWork.fetchActivityFromDB(activity);
    String json = new Gson().toJson(currUSersList);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);
    
    
    }


}
