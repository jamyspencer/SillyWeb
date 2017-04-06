<!doctype html>
<html>
<head>
<title>Start Session  ${thesessioncount}</title>
</head>

<body>
<center>
   <h1>Please Log in  </h1>

	<hr />
	<font size="+1">
	<form method="get" action="http://localhost:8080/js_test/sessionServlet" >
		<table>
			<tr>
				<td>Your name: </td>
				<td><input type="text" name="whoisit"></td>
			</tr>
			<tr>
				<td>Your password: </td>
				<td><input type="password" name="passwd"></td>
			</tr>
			<tr>
				<td><input type="hidden" name="task" value="0"></td>
				<td><input type="submit" value="Login"></td>	
			</tr>
		</table>
	</form>
	</font>
</center>
</body>
</html>
