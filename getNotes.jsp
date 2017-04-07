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
      h1 {
         text-align: center;
      }
      h2 {
         text-align: center;
      }
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
      }
      .form-group{
         border: 2px solid #ccc;
         border-radius: 4px;
         padding: 5px;
      }
      .btn{
         margin-top: 10px;
         min-width: 120px;
         max-width: 100%;
      }
   </style>
</head>

<body>
   <div class = "container-fluid">
      <div class = "row">
         <h1> Select a Version</h1>
         <hr />
         <c:set var="this_version" scope="page"  value="${theBean.version_id}" />
         <c:if test="${this_version == 0}">
         <c:set var="this_version"  value=""/><p>
         </c:if>

         <div class="col-md-7 col-xs-12" style="padding: 0px">
            <div class="col-md-4 col-xs-8">
               <form class="form-group" method="get" action="http://hoare.cs.umsl.edu/servlet/j-spencer/sessionServlet" >
                  <h2>Java File</h2>
                  Java Source:<br>
                  <input class="field" style="width: 100%;"  type="text" name="java_source" value="${theBean.file_name}"><br>
                  Version:<br>
                  <input class="field" style="width: 100%;" type="text" name="version" value="${this_version}"><br>
                  <input type="hidden" name="task" value="1">
                  <input type="submit" class="btn pull-right" value="Submit">
                  <div class="spacer" style="clear: both;"></div>
               </form>
            </div>

            <div class = "col-md-8 col-xs-12">
               <form method="get" class="form-group" action="http://hoare.cs.umsl.edu/servlet/j-spencer/sessionServlet" target="_blank">
                  <h2>Notes: ${theBean.file_name} ${this_version}</h2>
                  <textarea class="form-control field" rows="5" cols="100"  width="100%" name="notes">
                     ${theBean.notes}
                  </textarea> <br>

                  <input type="hidden" name="java_source" value="${theBean.file_name}">
                  <input type="hidden" name="version" value="${theBean.version_id}">
                  <input type="hidden" name="task" value="2">
                  <input type="submit" class="btn pull-right" value="Submit">
                  <div class="spacer" style="clear: both;"></div>
               </form>
            </div>
         </div>
         <div class = "col-md-5 ">
            <div class="form-group">
               <h2>The file:</h2>
               <div class="scrolling form-control field" >
                  <pre><b>
                ${theBean.this_version}
               </b></pre>
               </div>
            </div>
         </div>

         </font>
      </div>
   </div>
</body>
</html>