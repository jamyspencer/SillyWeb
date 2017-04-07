import java.net.URI;
import org.apache.http.client.methods.CloseableHttpResponse;  
import org.apache.http.client.methods.HttpUriRequest; 
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair;  
import java.io.*;
import static  BH.Bytes_Hex.*;

public class JStoreServletConnect {
    CloseableHttpClient httpclient;

    public static void main(String[] args){
        try {
            System.out.println(  new JStoreServletConnect().get_version("littlefile.java", 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String store_version(String the_name,String the_path, String the_notes){
        try {
            httpclient = HttpClients.createDefault(); 
            HttpUriRequest httpuri = RequestBuilder.post()
                                     .setUri(new URI("http://hoare.cs.umsl.edu/servlet/siegel/Jstore"))
                                     .addParameter("the_task","0")
                                     .addParameter("the_name",the_name)
                                     .addParameter("the_class", ByteArray2HexString(makeByteArray(the_path)))
                                     .addParameter("the_notes",ByteArray2HexString(the_notes.getBytes()))
                                     .build();
            CloseableHttpResponse response = httpclient.execute(httpuri); 
            return EntityUtils.toString(response.getEntity());
            //Just the String not the Status
            // System.out.println("Response Status line :" + response.getStatusLine());    
        } catch (Exception exc) {
        }

        return "Servlet Connect Problem";
    }
    public String get_version(String the_name,int the_version) throws Exception{
        httpclient = HttpClients.createDefault(); 
        String inttos=Integer.toString(the_version);
        String note_response="";
        int the_response_length=0;
        int the_code_length=0;
        try {
            HttpUriRequest httpuri = RequestBuilder.post()
                                     .setUri(new URI("http://hoare.cs.umsl.edu/servlet/siegel/Jstore"))
                                     .addParameter("the_task","1")
                                     .addParameter("the_name", the_name)
                                     .addParameter("the_version", inttos)
                                     .build();
            CloseableHttpResponse response = httpclient.execute(httpuri);
            String the_response=EntityUtils.toString(response.getEntity()).trim();
            //System.out.println(the_response);
            the_response_length=the_response.length();
            int the_break=the_response.indexOf("ccss44001100");
            //  System.out.println(the_response_length+" "+the_break);
            String code_response=the_response.substring(0,the_break);
            note_response=HexString2String(the_response.substring(the_break+12));
            //System.out.println(notes_response);
            the_code_length=code_response.length();
            String codeDecoded=HexString2String(code_response);              
            JStoreViewer jsv=new JStoreViewer(2,codeDecoded);
            new Thread(jsv).start();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        if (note_response.length()>0) {
            return note_response  ;
        }
        return "";
    }
    public String update_notes(String the_name,int the_version, String the_notes){
        String inttos=Integer.toString(the_version);
        try {
            httpclient = HttpClients.createDefault(); 
            HttpUriRequest httpuri = RequestBuilder.post()
                                     .setUri(new URI("http://hoare.cs.umsl.edu/servlet/siegel/Jstore"))
                                     .addParameter("the_task","2")
                                     .addParameter("the_name", the_name)
                                     .addParameter("the_version", inttos)
                                     .addParameter("the_notes", ByteArray2HexString(the_notes.getBytes()))
                                     .build();
            CloseableHttpResponse response = httpclient.execute(httpuri); 
            return EntityUtils.toString(response.getEntity());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return "Servlet Connect Problem";
    }
}

/*
    public static String Byte2HexString(byte[] barray){
        StringBuilder result = new StringBuilder();
        for (byte b : barray)
            result.append(String.format("%02X", b));  
        return result.toString();
    }

    public static byte[] HexString2Byte(String str,byte[] buf1){
        for (int i=0;i<str.length();i=i+2) {
            buf1[i/2] = (byte) Integer.parseInt(str.substring(i, i+2), 16);
        }
        return buf1;
    }

    public static String HexString2String(String hex){
        if((hex==null)||hex.length()==0) {
            return "No notes for this version.";
        }
        try {
            byte [] byte_hex=new byte[(hex.length())/2];
            byte_hex= HexString2Byte(hex,byte_hex);
            return  new String(byte_hex, "UTF-8");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }

    public static byte[] makeByteArray(String thepath){
        byte [] thetextfile ;
        try {
            File thefile = new File(thepath);
            FileInputStream fintext = new FileInputStream(thefile);
            thetextfile  = new byte[(int)thefile.length()];
            fintext.read(thetextfile);
            return thetextfile;

        } catch (Exception e) {
            System.out.println(e);
        }
        return new byte[0];

    }


*/
