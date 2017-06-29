package com.gofirst.framework.jetty;



/*import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.gofirst.framework.configure.SystemConfigProperties;*/

public class JettyMain {  
  
	/*private static String WEBAPP = "jetty.web.app.context.webapp";
	private static String PATH = "jetty.web.app.context.path";*/
	
   /* public static void main(String[] args) { 
		
        Server server = new Server(8081);  
  
        SystemConfigProperties config = new SystemConfigProperties();
  
        WebAppContext webAppContext = new WebAppContext(config.getProperty(WEBAPP), config.getProperty(PATH));  
  
        webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());  
        webAppContext.setParentLoaderPriority(true);  
        server.setHandler(webAppContext);  
  
        try {  
            server.start();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        System.out.println("server is  start");  
    }  */
}  