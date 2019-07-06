package com.heroku.dhruv.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heroku.dhruv.util.DBWork;

@WebServlet("/AddActivityToDB")
public class AddActivityToDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
 
    String activity=request.getParameter("activity");
    String userId=request.getParameter("userEmail");
    String userName=request.getParameter("userName");
    String location=request.getParameter("location");
    String profileImage=request.getParameter("profileImage");
    
    DBWork.addActivityToDB(activity,userId,userName,location,profileImage);
    
    }

}
