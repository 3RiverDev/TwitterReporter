package org.threeriverdev.twitterreporter.data;

import org.hibernate.SessionFactory;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	
	private SessionFactory sf;
	
	public SessionFactory getSessionFactory() {
		if (sf == null) {
			// use hibernate.properties
			ServiceRegistry sr = new ServiceRegistryBuilder()
					.configure()
					.buildServiceRegistry();
			sf = new MetadataSources(sr)
					.buildMetadata().buildSessionFactory();
		}
		return sf;
	}
}
