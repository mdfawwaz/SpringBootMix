package com.learning.hello;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Servlet implementation class HeartbeatServlet
 */
public class HeartbeatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<Integer> generateFibonacciSeries(int n) {
        ArrayList<Integer> fibonacciSeries = new ArrayList<>();
        
        if (n >= 1) {
            fibonacciSeries.add(0);  // First Fibonacci number
        }
        
        if (n >= 2) {
            fibonacciSeries.add(1);  // Second Fibonacci number
        }
        
        for (int i = 2; i < n; i++) {
            int nextFibonacci = fibonacciSeries.get(i - 1) + fibonacciSeries.get(i - 2);
            fibonacciSeries.add(nextFibonacci);
        }
        
        return fibonacciSeries;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 try {
		      Map<String, String[]> parameterMap = request.getParameterMap();
		      parameterMap.entrySet().stream().forEach(entry -> {
		        System.out.print("key: " + entry.getKey());
		        System.out.print(" values: " + Arrays.toString(entry.getValue()) + "\n");
		      });
		      PrintWriter out = response.getWriter();
		      int num = Integer.valueOf(request.getParameter("n"));
		      
		      out.println(generateFibonacciSeries(num));
		    } catch (IOException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    }
		  }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



      PrintWriter out = response.getWriter();
        int limit = Integer.valueOf(request.getParameter("formLimit"));
        out.println(String.format("<p>%s</p>", generateFibonacciSeries(limit)));
    	}
	}

