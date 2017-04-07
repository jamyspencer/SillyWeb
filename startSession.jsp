<!doctype html>
<html>
<head>
	<title>Start Session  ${thesessioncount}</title
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>

<body>
	<div class = "container">
		<div class = "row">
			<div class = "col-md-4, col-md-offset-2, col-lg-5, col-lg-offset-3">
				<h1>Please Sign In</h1>
			</div>
		</div>
		<div class = "row">
			<div class =  "col-md-4, col-md-offset-2, col-lg-5, col-lg-offset-3">
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
			</div>
		</div>
	</div>
</body>
</html>
