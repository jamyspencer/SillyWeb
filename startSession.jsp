<!doctype html>
<html>
<head>
<title>Start Session  ${thesessioncount}</title>
</head>

<body>
<center>
   <h1>Please Log in  </h1>

   <hr />
   <font size="+3">
   <form method="get" action="http://localhost:8080/js_test/sessionServlet" >
      Your name: <input type="text" name="whoisit"><br>
      Your password: <input type="password" name="passwd"><br>
      <input type="hidden" name="task" value="0">
      <input type="submit" value="Login">
   </form>
   </font>
</center>
</body>
</html>