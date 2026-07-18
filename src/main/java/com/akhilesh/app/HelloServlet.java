package com.akhilesh.app;

import java.io.IOException;
import java.io.PrintWriter;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String serverInfo = getServletContext().getServerInfo();
        String hostName = request.getServerName();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Java Tomcat WebApp</title>");
            out.println("<style>");
            out.println("body{font-family:Arial,sans-serif;background:#0f172a;color:#e2e8f0;");
            out.println("display:flex;align-items:center;justify-content:center;height:100vh;margin:0;}");
            out.println(".card{background:#1e293b;padding:40px 50px;border-radius:12px;");
            out.println("box-shadow:0 10px 30px rgba(0,0,0,0.4);text-align:center;}");
            out.println("h1{color:#38bdf8;margin-bottom:10px;}");
            out.println("p{color:#94a3b8;margin:6px 0;}");
            out.println("code{background:#0f172a;padding:2px 8px;border-radius:4px;color:#4ade80;}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='card'>");
            out.println("<h1>&#128640; Deployed Successfully!</h1>");
            out.println("<p>This Java web app was deployed to Apache Tomcat via <code>GitHub Actions</code></p>");
            out.println("<p>Server: <code>" + serverInfo + "</code></p>");
            out.println("<p>Host: <code>" + hostName + "</code></p>");
            out.println("<p>Server Time: <code>" + timestamp + "</code></p>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
