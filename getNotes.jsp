<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
   <title>Update Notes   ${thesessioncount}</title>
   <meta charset="utf-8">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
   <style>
   div.scrolling {
       background-color: #FFFFFF;
       height: 400px;
       overflow: scroll;
   }
   pre {
      background-color: inherit;
      border-width: 0px;
   }
   .field {
      background-color: #f5f5f5;
      border: 1px solid #ccc;
      border-radius: 4px;
   }
   </style>
</head>

<body>
   <div class = "container-fluid">
      <div class = "row">
         <center> <h1> Select a Version</h1></center>
         <hr />
         <div class = "col-lg-6">
            <c:set var="this_version" scope="page"  value="${theBean.version_id}" />
            <c:if test="${this_version == 0}">
            <c:set var="this_version"  value=""/><p>
            </c:if>

            <form method="get" action="http://hoare.cs.umsl.edu/servlet/j-spencer/sessionServlet" >
               Java Source: <input type="text" name="java_source" value="${theBean.file_name}">&nbsp;&nbsp;&nbsp;Version: <input type="text" name="version" value="${this_version}">
               <br>
               <input type="hidden" name="task" value="1">
               <input type="submit" value="Submit">
            </form>
         </div>
      </div>
      <div class = "row">
         <div class = "col-lg-6 form-group">
            <form method="get" action="http://hoare.cs.umsl.edu/servlet/j-spencer/sessionServlet" target="_blank">
               <center>
                  <h2>Notes: ${theBean.file_name} ${this_version}</h2>
               </center>
               <textarea class="form-control field" rows="5" cols="100"  width="100%" name="notes">
               ${theBean.notes}
               </textarea> <br>

               <input type="hidden" name="java_source" value="${theBean.file_name}">
               <input type="hidden" name="version" value="${theBean.version_id}">
               <input type="hidden" name="task" value="2">
               <input type="submit" value="Submit">
            </form>
         </div>
         <div class = "col-lg-6 form-group">
         <center> <h2>The file:</h2> </center>
            <div class="scrolling field form-group" >
               <pre><b>
             ${theBean.this_version}
            </b></font></pre>
            </div>
         </div>

         </font>
      </div>
   </div>
</body>
</html>