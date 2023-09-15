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
      // Get the action parameter
      String action = req.getParameter("action");

      var out = resp.getWriter();
      final IWebExchange webExchange = this.application.buildExchange(req, resp);
      final WebContext ctx = new WebContext(webExchange);
      
      ctx.setVariable("current", odc.getReading());
      
      if("increment".equals(action)) {
    	  odc.increment();
    	  ctx.setVariable("increment", odc.getReading());
    	  ctx.setVariable("decrement", null);
      }else if("decrement".equals(action)) {
    	  odc.decrementReading();
    	  ctx.setVariable("decrement", odc.getReading());
    	  ctx.setVariable("increment", null);
      }
      
      templateEngine.process("odometer", ctx, out);
     
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    final IWebExchange webExchange = this.application.buildExchange(req, resp);
    final WebContext ctx = new WebContext(webExchange);
    templateEngine.process("odometer", ctx, resp.getWriter());
  }

}