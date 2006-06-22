***************************************************
* THE ARANEA FRAMEWORK, release 1.0 M3 (May 2006) *
*        http://www.araneaframework.org/          *
***************************************************

INTRODUCTION

Aranea is a Java Hierarchical Model-View-Controller Web Framework that provides a common simple
approach to building the web application components, reusing custom or general GUI logic and extending the
framework.

RELEASE INFO

The Aranea Web Framework requires J2SE 1.3 and should run on any servlet 
container supporting the 2.3 Servlet API and 1.2 JSP API, but was
tested with the following containers:
 * BEA WebLogic 7.0, 8.1, 9.0, 9.1
 * Oracle Application Server 9i, 10g
 * Tomcat 4.x, 5.x
 * Jetty 5.x


Release contents:
 * "src" contains the Java source files for the framework
 * "etc" contains the accompanying configuration files
 * "tests" contains the unit tests of the framework
 * "template" template library for the examples
 * "examples" examples of using the framework
 * "doc" documentation of the project

DISTRIBUTION JAR FILES
The "dist" directory contains the following jar files for use in applications:
 * aranea-core.jar
 	The core interfaces and base implementation.
 	
 * aranea-framework.jar
 	Container independent services.
 
 * aranea-servlet.jar
 	Servlet-dependant services.
 	
 * aranea-spring.jar
 	Integration for Spring IoC container.
 
 * aranea-jsp.jar
 	Custom tag library.
 
 * aranea-backend.jar
 	Supporting classes for use in the application service layer.

 * aranea-uilib.jar
    Reusable user interface widgets.

DOCUMENTATION

Documentation can be found in the "doc" directory.
	doc/white-paper is a whitepaper on the Aranea Web Framework
	doc/tutorial is a tutorial using the Aranea Web Framework
	doc/reference includes the reference manual.
	doc/javadoc includes the javadoc

KNOWN ISSUES
 
 * Saving user session on the client side is difficult to configure
  at the moment. The filter should be added to the main configuration
  file (it is there right now but commented out) and then a new
  aranea.jar should be built.