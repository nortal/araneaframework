*****************************************
* THE ARANEA FRAMEWORK, release 1.1-M3  *
*    http://www.araneaframework.org/    *
*****************************************

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
 	Web-dependent services.
 	
 * modules/aranea-jsp.jar
 	Custom tag library.
 
 * modules/aranea-jsp-engine.jar
    Experimental lightweight JSP engine (in prototype stage).
 
 * modules/aranea-backend.jar
 	Supporting classes for use in the application service layer.

 * modules/aranea-uilib.jar
    Reusable user interface widgets.

 * integration/aranea-spring.jar
 	Integration for Spring IoC container.

DOCUMENTATION

Documentation can be found in the "doc" directory.
	doc/intro describes Aranea conceptual hello-world application
	doc/tutorial is a tutorial/intro to Aranea Web Framework
	doc/reference includes the reference manual.
	doc/javadoc includes the javadoc

BUILDING THE DISTRIBUTION
  If you have downloaded the official distribution, 'ant build' will do.
  If you have checked out the source code from repository, then you need
  to fetch the libraries before building, execute 'ant fetch-libs -lib lib/ivy'
  followed by 'ant build'.
