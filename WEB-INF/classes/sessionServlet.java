import BH.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*; 
import java.util.*;
import java.util.function.*;
import static java.util.Arrays.asList;
import java.text.DateFormat;


public class sessionServlet extends HttpServlet {
    private    List<String[]>   the_sessions;
    private    DateFormat df;


    public void init() throws ServletException  {
        the_sessions=new ArrayList<String[]>();
        df=DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
        String user_name = "";
        String user_pw = "";

        if ((!(req.getParameter("task") == null)) && (req.getParameter("task").trim().equals("deploy"))) {
            PrintWriter out = res.getWriter();
            out.println("<html>");
            out.println("<body>");
            out.println("<hr /><center><h1>sessionServlet Deployed</h1></center><hr />");
            out.println("</body>");
            out.println("</html>"); 
            return;
        }
        Consumer <String> forwardTo =(s) ->ForwardTo(s,req,res);
        boolean is_first_visit=true;
        String[] this_session=new String[3];
        String ip = req.getRemoteAddr();
        for (String [] a_session :the_sessions) {
            if (a_session[0].equals(ip)) {  //Found an active session
                is_first_visit=false;
                this_session=a_session;
                break;
            }
        }

        if(req.getParameter("logout") != null && req.getParameter("logout").equals("true")){
            log("running logout");
            HttpSession session = req.getSession(); //get the session
            Cookie[] cookies = req.getCookies(); //get all the cookies
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setMaxAge(0); //actually delete the cookies
                    cookie.setPath("/"); //allow the entire application to access it
                    res.addCookie(cookie); //add the deleted cookie back to the browser
                }
            }
            the_sessions.remove(this_session);
            req.setAttribute("thesessioncount",the_sessions.size());
            session.invalidate(); //invalidate the session and unbind any object within the session
            forwardTo.accept("startSession.jsp");
            return;
        }

        if ((req.getParameter("task") == null) && (!is_first_visit)) {
            the_sessions.remove(this_session);
            is_first_visit=true; // just used http://hoare.cs.umsl.edu/servlet/js_test/sessionServlet
        }
        req.setAttribute("thesessioncount",the_sessions.size());

        if (is_first_visit) {
            log("running first visit");

            if (the_sessions.size() == 10) {
                forwardTo.accept("noSessions.jsp");  //No Available Sessions
                return;
            }
            String[] new_session = {ip,df.format(new Date()),"need a name"};
            the_sessions.add(new_session);
            this_session=new_session;
            forwardTo.accept("startSession.jsp");
            return;
        }

        if (this_session[2].equals("need a name")) { //No name given yet
        log("adding a name");
            user_name = req.getParameter("whoisit");
            user_pw = req.getParameter("passwd");
            this_session[2] = user_name.trim();
            if ((user_name == null)||(user_name.trim().length() == 0)) {
                the_sessions.remove(this_session);
                req.setAttribute("thesessioncount",the_sessions.size());
                forwardTo.accept("startSession.jsp");
                return;  // didn't enter a name in startSession
            }
        }
        req.setAttribute("thename", this_session[2]);

        if (tooLong(this_session[1],df.format(new Date()))) {  //Has the session timed out?
            the_sessions.remove(this_session);
            forwardTo.accept("Expired.jsp");
            return;
        }

        if (req.getParameter("task") != null) {
            log("The value of the parameter task is " + req.getParameter("task"));
            this_session[1]=df.format(new Date()); //reset the last session activity time
            NotesBean thesenotes=new NotesBean();
            if (!req.getParameter("task").trim().equals("0")) {
                thesenotes.setAll(req.getParameter("java_source"),Integer.parseInt(req.getParameter("version")));
                if (req.getParameter("task").trim().equals("2")) {
                    thesenotes.setNotes(req.getParameter("notes").trim(),req.getParameter("java_source"),Integer.parseInt(req.getParameter("version")));
                }
            }
            req.setAttribute("thesessioncount",the_sessions.size());
            req.setAttribute("theBean",thesenotes);
            //req.setAttribute("theURL", "http://www.umsl.edu/~siegelj/turing.jpg");
            forwardTo.accept("getNotes.jsp");
            return;
        }
        return;
    }//end doGet

    boolean tooLong(String now,String then){
        //Check amount of time that passed
        return false;
    }
    boolean checkPW(String name,String password){
        //Check password against name
        return false;
    }

    public void log(String s){
        FileWriter fileWriter = null;
        try {
            String content =s+" at :"+new Date(System.currentTimeMillis()).toString()+"\n";
            File theLogFile = new File("./servlet.log");
            fileWriter = new FileWriter(theLogFile,true);
            fileWriter.write(content);
        } catch (IOException ex) {
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {

            }
        }

    }

    void ForwardTo(String s,HttpServletRequest req, HttpServletResponse res)
    {
        RequestDispatcher rd= req.getRequestDispatcher(s);
        try {
            rd.forward(req, res);
        } catch (IOException|ServletException is) {
            log(" req from "+s+" not forwarded at ");
            try {
                throw is;
            } catch (Exception e) {
            }
        }
    }

    public void destroy()
    {
        log("The instance was destroyed");
    }
    public String getRandomString(){
        byte[] randbyte=new byte[10];
        Random rand  = new Random(System.currentTimeMillis());
        for (int idx = 0; idx <10; ++idx) {
            int randomInt = rand.nextInt(26); //0<=randomInt<26
            //System.out.println(randomInt);
            randbyte[idx]=(byte)(randomInt+65);  
        }
        try {
            String rs=new String(randbyte, "UTF-8");
            //System.out.println(rs);
            return rs;
        } catch (Exception e) {
            //System.out.println("bad string");
            return "bad";
        }
    }

}


