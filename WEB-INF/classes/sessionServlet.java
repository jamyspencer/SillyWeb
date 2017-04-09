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
    private    List<HttpSession> the_sessions;
    private    DateFormat df;


    public void init() throws ServletException  {
        the_sessions = new ArrayList<HttpSession>();
        df = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
        final String USER_IP = "userIP";
        String user_name = "";
        String user_pw = "";
        boolean is_valid_session = false;

        String realPath = getServletContext().getRealPath("/");
        req.setAttribute("path", realPath);

        Consumer <String> forwardTo =(s) ->ForwardTo(s,req,res);
        HttpSession this_session = null;
        String ip = req.getRemoteAddr();

        for (int i = 0; i < the_sessions.size(); i++) {
            try {
                if (the_sessions.get(i).getAttribute(USER_IP).equals(ip)) {  //Found an active session
                    is_valid_session = true;
                    this_session = the_sessions.get(i);
                    break;
                }
            }catch (Exception e){
                the_sessions.remove(i);
            }
        }
        log(String.valueOf(req.getParameter("logout") != null));
        //Check for user logging out
        if(req.getParameter("logout") != null && req.getParameter("logout").equals("true")){
            log("in the logout");
            HttpSession session = req.getSession(); //get the session
            Cookie[] cookies = req.getCookies(); //get all the cookies
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    res.addCookie(cookie);
                }
            }
            the_sessions.remove(this_session);
            this_session.invalidate();
            req.setAttribute("thesessioncount",the_sessions.size());
            forwardTo.accept("startSession.jsp");
            return;
        }
        if (!is_valid_session) {

            //check that there is room for new session
            if (the_sessions.size() == 10) {
                forwardTo.accept("noSessions.jsp");  //No Available Sessions
                return;
            }
            //see if there were a user name and password passed in
            if (req.getParameter("whoisit") != null && req.getParameter("passwd") != null) {
                user_name = req.getParameter("whoisit").trim();
                user_pw = req.getParameter("passwd").trim();
            }
            //if no valid user name and password send to login page
            if (user_name.isEmpty() || user_pw.isEmpty()) {
                req.setAttribute("thesessioncount",the_sessions.size());
                forwardTo.accept("startSession.jsp");
                return;
            }
            else {
                log("validated user");
                this_session = req.getSession(true);
                this_session.setAttribute(USER_IP, ip);
                the_sessions.add(this_session);
                req.setAttribute("thesessioncount",the_sessions.size());
                is_valid_session = true;
            }
        }

        if(is_valid_session) {
            req.setAttribute("thesessioncount",the_sessions.size());
            if (req.getParameter("task") != null) {
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
            File theLogFile = new File("/my_log");
            if (!theLogFile.exists()){
                theLogFile.createNewFile();
            }
            fileWriter = new FileWriter(theLogFile,true);
            fileWriter.write(content);
        } catch (IOException ex) {
        } finally {
            try {
                fileWriter.close();
            } catch (Exception ex) {

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


