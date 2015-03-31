package org.hibernate.gradle.mixedjdkissue;

import org.hibernate.boot.jaxb.cfg.spi.JaxbCfgHibernateConfiguration;

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

	public Repository() {
		JaxbCfgHibernateConfiguration jaxbBinding = new JaxbCfgHibernateConfiguration();
	}

	public void store(Object data) {
		// no op.  its an example silly
	}
}
