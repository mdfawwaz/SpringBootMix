package com.learning.hello;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import com.learning.hello.contoller.OdometerController;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/odometer")
public class OdometerServlet extends HttpServlet{
  
  private OdometerController odc;
  private JakartaServletWebApplication application;
  private TemplateEngine templateEngine;
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    odc = new OdometerController(5);
    odc.reset();
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
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      // Get the action parameter from the request to determine the user's action
      String action = req.getParameter("action");
      
      // Assuming "odc" is your existing object, modify the following part based on your context
      if ("increment".equals(action)) {
          // Increment the reading
    	  odc.increment();
    	  req.setAttribute("increment", odc.getReading());
    	  req.setAttribute("decrement", null);
      } else if ("decrement".equals(action)) {
          // Decrement the reading
    	  odc.decrementReading();
    	  req.setAttribute("decrement", odc.getReading());
    	  req.setAttribute("increment", null);
      }

      // Your existing code for processing and rendering the template
      var out = resp.getWriter();
      final IWebExchange webExchange = this.application.buildExchange(req, resp);
      final WebContext ctx = new WebContext(webExchange);
      ctx.setVariable("increment", odc.nextReading().getReading());
      ctx.setVariable("decrement", odc.prevReading().getReading());
      ctx.setVariable("current", odc.getReading());
      
      templateEngine.process("odometer", ctx, out);
     
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    final IWebExchange webExchange = this.application.buildExchange(req, resp);
    final WebContext ctx = new WebContext(webExchange);
    templateEngine.process("odometer", ctx, resp.getWriter());
  }

}