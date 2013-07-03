@GrabResolver("https://oss.jfrog.org/artifactory/repo")
@Grab(value = "org.ratpack-framework:ratpack-groovy:0.9.0-SNAPSHOT")

import org.ratpackframework.groovy.templating.TemplateRenderer
import org.ratpackframework.groovy.templating.TemplatingModule
import static org.ratpackframework.groovy.RatpackScript.ratpack

// For session handling
import org.ratpackframework.session.Session
import org.ratpackframework.session.store.MapSessionsModule
import org.ratpackframework.session.store.SessionStorage

import java.util.logging.Logger

ratpack {

    def logger = Logger.getLogger("")
	
    modules {
        get(TemplatingModule).setCacheSize(0)
		
		// TODO:
		// Enable non persistent map based session storage
        register(new MapSessionsModule(10, 5))
    }
    
    handlers {
	
	    get {
            get(TemplateRenderer).render "index.html", title: "Authorization Demo"
	    }
		
	    post("login") {
		
		    def authHeader = request.getHeader("Authorization")
			
			// println authHeader
			def encodedValue = authHeader.split(" ")[1]
			//println encodedValue.size()
			
            def decodedValue = new String(encodedValue.decodeBase64())?.split(":")			
            //println decodedValue.size()
			
			// Validate that both username and password fileds are present
			if (decodedValue.size() < 2) {
              response.send "ERROR - Both Username and Password entries must be present"			
			}
			
			// Some sort of real validation belongs here.
			// NOTE: using/storing clear-text passwords is a REALLY BAD idea.
			// For a real-world usable example, the encrypted passwords should
			// probably be stored in the database and the encrypted version 
			// compared against that to prevent the passwords from being stolen
			// during a DB hack.
			if (decodedValue[0] == "testuser" && decodedValue[1] == "pass") {
			  response.send "Successful Login"
			}
			else {
               response.send "Unauthorized"			
			}
        }
		
	    post("logout") {
                if (get(Session).existingId) {
                    get(Session).terminate()
                }
                response.send()
	    }

        assets "public"
    }
}

