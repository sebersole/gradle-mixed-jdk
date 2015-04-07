package org.hibernate.build.gradle.jaxb

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

/**
 * @author Steve Ebersole
 */
class JaxbExtension {
	final Project project

//	String xjcTaskName = 'com.sun.tools.xjc.XJCTask'
	String xjcTaskName = 'org.jvnet.jaxb2_commons.xjc.XJC2Task'

	File outputDir

	final NamedDomainObjectContainer<SchemaDescriptor> schemas;

	public JaxbExtension(Project project) {
		this.project = project;

		// for now, although ultimately I think we want to move generated-src out of buildDIr...
		outputDir = project.file( "${project.buildDir}/generated-src/jaxb" )

		// Create a dynamic container for SchemaDescriptor definitions by the user
		schemas = project.container( SchemaDescriptor, new SchemaDescriptorFactory( project ) )
	}

	def schemas(Closure closure) {
		schemas.configure( closure )
	}
//	def jaxb(Closure closure) {
//		project.logger.lifecycle( "In JaxbExtension#jaxb" )
//		ConfigureUtil.configure( closure, this )
//		schemas.configure( closure )
////		ConfigureUtil.configure( closure, schemas )
//	}
}
