package org.hibernate.build.gradle.animalsniffer

import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.SourceSet

import org.codehaus.mojo.animal_sniffer.ClassListBuilder
import org.codehaus.mojo.animal_sniffer.SignatureChecker
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class AnimalSnifferPlugin implements Plugin<Project> {
	private Logger logger = LoggerFactory.getLogger( this.class )

	@Override
	void apply(Project project) {
		project.configurations.maybeCreate( "animalSnifferSignature" )
		final AnimalSnifferExtension extension = project.extensions.create( "animalSniffer", AnimalSnifferExtension, project )

		project.afterEvaluate(
				new Action<Project>() {
					@Override
					void execute(Project evaluatedProject) {
						extension.sourceSets.each{ SourceSet sourceSet->
							project.tasks.findByName( sourceSet.compileJavaTaskName ).doLast(
									new Action<Task>() {
										@Override
										void execute(Task task) {
											if ( extension.skip ) {
												return;
											}

											def gradleLoggingBridge = new AnimalSnifferToGradleLoggingBridge( logger )
											def ignores = buildIgnores(
													project,
													sourceSet,
													extension,
													gradleLoggingBridge
											)

											def signatures = project.configurations.animalSnifferSignature.resolvedConfiguration.resolvedArtifacts*.file

											signatures.each{ File file->
												task.logger.lifecycle( "Starting AnimalSniffer checks for SourceSet ${sourceSet.name} against ${file.name}" )

												SignatureChecker signatureChecker = new SignatureChecker(
														new FileInputStream( file ),
														ignores,
														gradleLoggingBridge
												)
												signatureChecker.setCheckJars( false );

												List<File> sourceDirs = new ArrayList<File>();
												sourceDirs.addAll( sourceSet.java.srcDirs )
												signatureChecker.setSourcePath( sourceDirs )

												signatureChecker.process( project.file( sourceSet.output.classesDir ) );

												if ( signatureChecker.isSignatureBroken() ) {
													throw new GradleException(
															"Signature errors found for SourceSet ${sourceSet.name} against ${file.name}. Verify them and ignore them with the proper annotation if needed."
													);
												}
											}
										}
									}
							);
						}
					}
				}
		);
	}

	private static Set<String> buildIgnores(
			Project project,
			SourceSet sourceSet,
			AnimalSnifferExtension extension,
			AnimalSnifferToGradleLoggingBridge gradleLoggingBridge) {
		def ClassListBuilder clb = new ClassListBuilder( gradleLoggingBridge )

		// Any references to classes from the current project are fine
		clb.process( project.file( sourceSet.output.classesDir ) );

		if ( extension.excludeCompileClasspath ) {
			sourceSet.compileClasspath.each{ File classpathItem->
				clb.process( classpathItem );
			}
		}

		// it's ignored types actually, not packages
		return clb.getPackages()
	}
}


