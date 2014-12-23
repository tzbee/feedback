package com.feedback.dao;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Initialize database resources
 */
@WebListener
public class LocalEntityManagerFactory implements ServletContextListener {
	private static EntityManagerFactory emf;

	@Override
	public void contextInitialized(ServletContextEvent event) {

		URI dbUri;
		try {
			dbUri = new URI(System.getenv("DATABASE_URL"));

			String username = dbUri.getUserInfo().split(":")[0];
			String password = dbUri.getUserInfo().split(":")[1];
			String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
					+ dbUri.getPort() + dbUri.getPath();

			Properties props = new Properties();
			props.setProperty("javax.persistence.jdbc.driver",
					"org.postgresql.Driver");
			props.setProperty("javax.persistence.jdbc.user", username);
			props.setProperty("javax.persistence.jdbc.password", password);
			props.setProperty("javax.persistence.jdbc.url", dbUrl);
			emf = Persistence.createEntityManagerFactory("Feedback", props);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		emf.close();
	}

	public static EntityManager createEntityManager() {
		if (emf == null) {
			throw new IllegalStateException("Context is not initialized yet.");
		}
		return emf.createEntityManager();
	}
}