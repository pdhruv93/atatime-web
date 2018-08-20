package com.heroku.dhruv.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.heroku.dhruv.util.DBWork;

@WebServlet("/ChatFeature")
public class ChatFeatureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
 
    String userEmail=request.getParameter("userEmail");
    String mobnumber=request.getParameter("mobnumber");
    String chat=request.getParameter("chat");
    
    DBWork.chatFeature(userEmail,mobnumber,chat);
    
    }

}
