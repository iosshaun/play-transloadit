package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import codesign.Sign;

import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static String futureUTCTimeString(long hours){
        Long current = System.currentTimeMillis();

        // One hour in the future.
        Long soon = current + 3600L * (1000L * hours); 
        Date expiresDate = new Date(soon);
        SimpleDateFormat formatter = (SimpleDateFormat) 
                    SimpleDateFormat.getInstance();
        
        // Required format: 2013/06/30 04:22:12+00:00
        formatter.applyPattern("yyyy/MM/dd HH:mm:ss+00:00");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC")); 
        String s = formatter.format(expiresDate);
        System.out.println(s);
        
        return s;
    }

    public static Result upload() {

        //Change this to your transloadit secret and key.
        //Change the templateId to your templateId.
        //change the redirectUrl to a url the transloadit server can hit.
        String secret = "12121212121212121212121212121212";
        String key = "abcd efg hijk lmnop qrs tuv wx yz";
        String templateId = "1234567890";
        String redirectUrl = "http://localhost:9000/install";
        
        redirectUrl = "";
        secret = "";
        key = "";
        templateId="";
        
        //Expire request in 1 hour.
        String expires = futureUTCTimeString(1L);
        
        ObjectNode auth = Json.newObject();
        auth.put("expires", expires);
        auth.put("key", key);
        
        ObjectNode params = Json.newObject();
        params.put("auth", auth);
        params.put("template_id", templateId);
        params.put("redirect_url", redirectUrl);
        
        //sign this value.
        String paramsString = Json.stringify(params);
        
        //Sign the form values to secure our data.
        Sign s = new Sign();
        String signature = s.signature(paramsString, secret);

        System.out.println("Application :: params value="+paramsString);
        System.out.println("Application :: signature value="+signature);
        //Display the upload form to the user.
        return ok(upload.render(paramsString, signature));
    }
    
    
    /*
     * This is the callback for transloadit submit.
     * It's the redirect URL we send to transloadit on upload.
     * It's automatically called by the transloadit Jquery plugin when they 
     * have processed the upload request.
     *  Form submit -> transloadit -> response -> formsubmit to redirectUrl
     *  
     * Transloadit has copied the file to S3.
     * Optionally check the uuid we passed through to identify the 
     * request, if we need to.
     * 
     * We could also check the request signature if we wanted to ensure the 
     * call came from transloadit - or we could use our custom uuid to match
     * against the form we generated originally for the upload process.
     * 
     * */
    public static Result install() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String transloadit = requestData.get("transloadit");
        System.out.println("Application :: "+transloadit);
        JsonNode node = Json.parse(transloadit);
        System.out.println("Application :: json :: "+Json.stringify(node));
        
        node = node.get("results");
        //String url = node.findValuesAsText(url);
        node = node.findValue("url");
        String url = node.getTextValue();
        //Here we could check if the request is valid and if it is, then store 
        //the meta-data transloadit sent back, in our DB.
        //... do stuff
        
        //Display the transloadit meta-data we received.
        return ok(install.render(url));
    }

  
}
