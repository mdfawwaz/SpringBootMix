package com.tennis.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;
import com.tennis.controller.Game;
import com.tennis.controller.Player;


public class TennisServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 private Game game;
	 private Player p1;
	 private Player p2;
	 private JakartaServletWebApplication application;
	 private TemplateEngine templateEngine;

    
    @Override
    public void init(ServletConfig config) throws ServletException {
      super.init(config);
      application = JakartaServletWebApplication.buildApplication(getServletContext());
      p1 = new Player("player1");
      p2 = new Player("player2");
      game = new Game(p1,p2);
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
      templateEngine.process("tennis", ctx, resp.getWriter());
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var out = response.getWriter();
		
		String User1 = request.getParameter("player1Name");
		String User2 = request.getParameter("player2Name");
		
		
		String p1ScoreInc = request.getParameter("player1Button");
		String p2ScoreInc = request.getParameter("player2Button");
		
		final IWebExchange webExchange = this.application.buildExchange(request, response);
	      final WebContext ctx = new WebContext(webExchange);
	      p1.setName(User1);
	      p2.setName(User2);
	      ctx.setVariable("player1Name", p1.getName());
	      ctx.setVariable("player2Name", p2.getName());
	      
		  
		  if(request.getParameter("player1Button") != null) {
			  try {
				game.PlayerScore(p1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  ctx.setVariable("player1score", p1.getScore());
			  ctx.setVariable("player2score", p2.getScore());
			  
			  ctx.setVariable("player1MatchesWon", p1.getGamesWon());
			  ctx.setVariable("player2MatchesWon", p2.getGamesWon());
			  
			  ctx.setVariable("player1SetsWon", p1.getSetsWon());
			  ctx.setVariable("player2SetsWon", p2.getSetsWon());
			  
			  
			  ctx.setVariable("winnerP1", game.WinnerString);
			  
		  }else if(request.getParameter("player2Button") != null) {
			  try {
				game.PlayerScore(p2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  ctx.setVariable("player2score", p2.getScore());
			  ctx.setVariable("player1score", p1.getScore());
			  
			  ctx.setVariable("player2MatchesWon", p2.getGamesWon());
			  ctx.setVariable("player1MatchesWon", p1.getGamesWon());
			  
			  ctx.setVariable("player1SetsWon", p1.getSetsWon());
			  ctx.setVariable("player2SetsWon", p2.getSetsWon());
			  
			  ctx.setVariable("winnerP2",game.WinnerString);

		  }else if(request.getParameter("reset") != null) {
			  game.reset();
		  }
	      templateEngine.process("tennis", ctx, out);
	}
}
