<!doctype html>
<html>
<head>
	<title>Start Session  ${thesessioncount}</title
	<link href="css/bootstrap.css" rel="stylesheet">
</head>

<body>
   <h1>Please Log in  </h1>

	<hr />
	<form method="get" action="http://hoare.cs.umsl.edu/servlet/j-spencer/sessionServlet" >
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
</body>
</html>
