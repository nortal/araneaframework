**********************************************************
* THE ARANEA FRAMEWORK, release 1.0 RC4 (September 2006) *
*        http://www.araneaframework.org/                 *
**********************************************************

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
 * "etc" contains the accompanying configuration files and web scripts
 * "tests" contains the unit tests of the framework
 * "examples" examples of using the framework
 * "examples/common" common class library for the examples
 * "doc" documentation of the project

DISTRIBUTION JAR FILES
The "dist" directory contains the following jar files for use in applications:
 * aranea.jar
    All current Aranea classes and modules.

 * modules/aranea-core.jar
 	Aranea core interfaces and base implementations.
 	
 * modules/aranea-framework.jar
 	Container independent services.
 
 * modules/aranea-http.jar
 	Web-dependant services.
 	
 * modules/aranea-jsp.jar
 	Custom tag library.
 
 * modules/aranea-backend.jar
 	Supporting classes for use in the application service layer.

 * modules/aranea-uilib.jar
    Reusable user interface widgets.

 * integration/aranea-spring.jar
 	Integration for Spring IoC container.

DOCUMENTATION

Documentation can be found in the "doc" directory.
	doc/white-paper is a whitepaper on the Aranea Web Framework
	doc/tutorial is a tutorial/intro to Aranea Web Framework
	doc/reference includes the reference manual.
	doc/javadoc includes the javadoc
