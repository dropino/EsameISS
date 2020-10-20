package it.unibo.tearoom.SPRINT4.ui;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
//import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
/*
 * @EnableScheduling --> The @EnableScheduling annotation is used to enable the
 * scheduler for your application. This annotation should be added into the main
 * Spring Boot application class file. The @Scheduled annotation is used to
 * trigger the scheduler for a specific time period.
 * 
 */
public class Application implements ApplicationListener<ApplicationReadyEvent> {
	public static String myipAddr = "";
	public static String myport = "0";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);   
	}

	@Autowired 
	private ApplicationContext applicationContext;
 
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			int port = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.printf("IP=%s:PORT=%d", ip, port);
			myipAddr = ip;
			myport = "" + port;
			System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}  
