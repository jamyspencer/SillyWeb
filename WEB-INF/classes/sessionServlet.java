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
    private    List<String> the_sessions;
    private    DateFormat df;
    private final boolean logging = true;

    public void init() throws ServletException  {
        the_sessions = new ArrayList<String>();
        df = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
        String this_session;
        String user_name = "";
        String user_pw = "";
        boolean is_valid_session = false;
        Consumer <String> forwardTo =(s) ->ForwardTo(s,req,res);

        if (req.getParameter("sessionID") != null){
            this_session = req.getParameter("sessionID").trim();
        }
        else{
            this_session = "noID";
        }

        for (int i = 0; i < the_sessions.size(); i++) {
            try {
                if (the_sessions.get(i).equals(this_session)) {  //Found an active session
                    is_valid_session = true;
                    break;
                }
            }catch (Exception e){
                the_sessions.remove(i);
                if (logging) log("Error in session check");
            }
        }
        //Check for user logging out
        if(req.getParameter("logout") != null && is_valid_session){
            the_sessions.remove(this_session);
            req.setAttribute("thesessioncount",the_sessions.size());
            forwardTo.accept("startSession.jsp");
            return;
        }
        //Check to see if the session needs to be validated
        if (!is_valid_session) {
            //check that there is room for new session
            if (the_sessions.size() >= 10) {
                forwardTo.accept("noSessions.jsp");  //No Available Sessions
                return;
            }
            //see if there was a user name and password passed in
            if (req.getParameter("whoisit") != null && req.getParameter("passwd") != null) {
                user_name = req.getParameter("whoisit").trim();
                user_pw = req.getParameter("passwd").trim();
                req.removeAttribute("whoisit");
                req.removeAttribute("passwd");
            }
            //if no valid user name and password send to login page
            if (user_name.isEmpty() || user_pw.isEmpty()) {
                req.setAttribute("thesessioncount",the_sessions.size());
                forwardTo.accept("startSession.jsp");
                return;
            }
            else {
                this_session = getRandomString();
                the_sessions.add(this_session);
                req.setAttribute("thesessioncount",the_sessions.size());
                req.setAttribute("sessionID",this_session);
                is_valid_session = true;
                if(logging) log(this_session);
            }
        }
        //valid session, run primary logic and display getNotes.jsp
        if(is_valid_session) {
            final Object lock = req.getSession().getId().intern();
            req.setAttribute("thesessioncount",the_sessions.size());
            synchronized(lock) {
                if (req.getParameter("task") != null) {
                    NotesBean thesenotes = new NotesBean();
                    if (!req.getParameter("task").trim().equals("0")) {
                        thesenotes.setAll(req.getParameter("java_source"), Integer.parseInt(req.getParameter("version")));
                        if (req.getParameter("task").trim().equals("2")) {
                            thesenotes.setNotes(req.getParameter("notes").trim(), req.getParameter("java_source"), Integer.parseInt(req.getParameter("version")));
                        }
                    }
                    req.setAttribute("theBean", thesenotes);
                }
            }
            req.setAttribute("thesessioncount",the_sessions.size());
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

        try {
            File outFile = new File(getServletContext().getRealPath("/") + "my_log");
            outFile.createNewFile();
            PrintWriter fileWriter = new PrintWriter(new FileOutputStream(outFile,true));
            fileWriter.println(s+" at: " + new Date(System.currentTimeMillis()).toString());
            fileWriter.close();
        } catch (IOException ex) {

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


