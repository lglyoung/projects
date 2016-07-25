package org.smart4j.chapter1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = df.format(new Date());
		request.setAttribute("currentTime", currentTime);
		request.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(request, response);
	}
}
