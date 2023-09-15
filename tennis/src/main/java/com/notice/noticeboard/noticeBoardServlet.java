package com.notice.noticeboard;

import jakarta.servlet.ServletConfig;  
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

 

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;
import com.notice.controller.DBconnection;

 

@WebServlet("/notice")
public class noticeBoardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
     static {
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    private JakartaServletWebApplication application;
      private TemplateEngine templateEngine;

      DBconnection dbconnect = new DBconnection();
      int index = 1;

      @Override
      public void init(ServletConfig config) throws ServletException {
        super.init(config);

        application = JakartaServletWebApplication.buildApplication(getServletContext());
        final WebApplicationTemplateResolver templateResolver = 
            new WebApplicationTemplateResolver(application);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
      }

      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final IWebExchange webExchange = this.application.buildExchange(req, resp);
        final WebContext ctx = new WebContext(webExchange);
        //ctx.setVariable("reading", oc.getReading());
        ResultSet rs = dbconnect.getNotice();
          try {
            while(rs.next() && index <=6) {
                ctx.setVariable("heading"+index,  rs.getString("heading"));
                ctx.setVariable("body"+index,  rs.getString("body"));
                ctx.setVariable("author"+index, "-by "+ rs.getString("author"));
                index+=1;
             // doGet(req, resp);
              }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
          //var out = resp.getWriter();
        templateEngine.process("Notice", ctx, resp.getWriter());
      }

      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          final IWebExchange webExchange = 
                    this.application.buildExchange(req, resp);
          final WebContext ctx = new WebContext(webExchange);

          String btnId = req.getParameter("btn");
          System.out.println(btnId);
          if(btnId != null) {
              index = 1;
              String heading = req.getParameter("heading");
              String body = req.getParameter("body");
              String author = req.getParameter("author");
              System.out.println("heading -"+heading);
              System.out.println("body -"+body);
              System.out.println("author -"+author);
              try {
                dbconnect.saveNotice(heading,body,author);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

          }

          ResultSet rs = dbconnect.getNotice();
          try {
            while(rs.next() && index <=6) {
                ctx.setVariable("heading"+index,  rs.getString("heading"));
                ctx.setVariable("body"+index,  rs.getString("body"));
                ctx.setVariable("author"+index, "-by "+ rs.getString("author"));
                index+=1;
             // doGet(req, resp);
              }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        var out = resp.getWriter();

        templateEngine.process("Notice", ctx, out);
      }
}