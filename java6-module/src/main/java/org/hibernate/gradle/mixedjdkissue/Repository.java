package org.hibernate.gradle.mixedjdkissue;

import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.boot.jaxb.cfg.spi.JaxbCfgHibernateConfiguration;
import org.hibernate.boot.jaxb.hbm.spi.JaxbHbmHibernateMapping;

/**
 * A simple Java 6 compatible class
 *
 * @author Steve Ebersole
 */
public class Repository {
	/**
	 * Singleton access
	 */
	public static final Repository INSTANCE = new Repository();

	ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<String,Object>();

	public Repository() {
		JaxbCfgHibernateConfiguration jaxbBinding = new JaxbCfgHibernateConfiguration();
		JaxbHbmHibernateMapping jaxbMapping = new JaxbHbmHibernateMapping();
	}

	public void store(Object data) {
		// This is all the fuss with AnimalSniffer versus bootClasspath...
		// Obviously the Map#keySet is present in Java 2 even, so it is
		// available in Java 6.  The problem is that Java 8 changed the
		// return type of ConcurrentHashMap#keySet using covariance.  The
		// covariant return is specific to Java 8 (not present in Java 6) and
		// when compiling using Java 8, that "difference" gets written to the
		// compiled bytecode making the bytecode Java 8 specific.
		//
		// So, all that boils down to a choice between:
		//		1) Change this to use ConcurrentMap rather than ConcurrentHashMap - its keySet return is not covariant
		//		2) Compile this using Java 6
		map.keySet();
	}
}
