<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Java Tomcat WebApp</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #0f172a;
            color: #e2e8f0;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }
        .card {
            background: #1e293b;
            padding: 40px 50px;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.4);
            text-align: center;
            max-width: 480px;
        }
        h1 { color: #38bdf8; margin-bottom: 10px; }
        p { color: #94a3b8; line-height: 1.6; }
        a {
            display: inline-block;
            margin-top: 20px;
            background: #38bdf8;
            color: #0f172a;
            padding: 10px 20px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
        }
        code { background: #0f172a; padding: 2px 8px; border-radius: 4px; color: #4ade80; }
    </style>
</head>
<body>
    <div class="card">
        <h1>Java Tomcat WebApp</h1>
        <p>Sample Maven-based servlet application, built for CI/CD deployment
           to <code>Apache Tomcat</code> using <code>GitHub Actions</code>.</p>
        <a href="hello">Test the Servlet &rarr;</a>
    </div>
</body>
</html>
