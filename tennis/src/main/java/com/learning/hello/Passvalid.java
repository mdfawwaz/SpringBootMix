package com.learning.hello;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/passvalid")
public class Passvalid extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public boolean checkBanPass(String pwd) {
        String filePath = "/home/dasankush/eclipse-workspace/dynamictest/src/main/java/com/learning/hello/bannedPass.txt"; 

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(pwd)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String formPassword = request.getParameter("pass");
        
        out.println(String.format("<h1>%s</h1>", formPassword));

        if (checkBanPass(formPassword)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password is not allowed.");
        } else {
            response.getWriter().write("Password accepted.");
        }
    }
}
