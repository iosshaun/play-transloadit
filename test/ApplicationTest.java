import org.junit.*;
import play.mvc.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import codesign.Sign;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    @Test 
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
    
    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Your new application is ready.");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Your new application is ready.");
    }
    
    @Test
    public void hmac(){

        //Value from the Transloadit nodejs example with the same input and secret. 
        String expected = "70ad71e8879500120e55bfea5aa2dbbfbaecb798";
        String rawValue = "\"PARAMS\"";
        String secret = "SECRET";
        Sign s = new Sign();
        String signed = s.signature(rawValue, secret);

        //Print the values to match the nodejs output 
        System.err.println(""+rawValue);
        System.err.println("signed "+signed);
        assertThat(signed).isEqualTo(expected);
    }   
}
