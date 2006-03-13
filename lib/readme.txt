The following libraries are included in the Aranea Framework distribution because they are
required either for building the framework or for running the sample apps. Note that each
of these libraries is subject to the respective license; check the respective project
distribution/website before using any of them in your own applications.

* ./ant/ant.jar
- Apache Ant (http://ant.apache.org/)
- Required to build the framework and run the samples out of the box.

* ./antlr/antlr-2.7.5H3.jar
- ANTRL (http://www.antlr.org/)
- Required for running the sample applications.

* ./asm/asm-2.2.1.jar;./asm/asm-attrs.jar
- ASM (http://asm.objectweb.org/)
- Required for running the sample application.

* ./cglib/cglib-nodep-2.1_3
- CGLIB - code generation library (http://cglib.sourceforge.net/)
- Required by Spring

* ./cobertura/cobertura.jar
- Cobertura - code coverage tool (http://cobertura.sourceforge.net/)
- Required for generating unittest code coverage.

* ./dom4j/dom4j-1.6.1.jar;./dom4j/jaxen-full.jar
- DOM4J XML parser (http://www.dom4j.org)

* ./hibernate/hibernate3.jar
- O/R mapping service for Java (http://www.hibernate.org/)
- Required for running the sample application.

* ./hsqldb/hsqldb.jar
- HSQL database engine (http://www.hsqldb.org/)
- Required for running the sample applications.

* ./jakarta-commons/commons-*.jar
- Jakarta Project Commons (http://jakarta.apache.org/commons/)

* ./jakarta-oro/jakarta-oro-2.0.8.jar
- Perl5 compatible RegExp http://jakarta.apache.org/oro/

* ./jakarta-regexp/jakarta-regexp-1.2.jar
- Pure Java Regular Expression package (http://jakarta.apache.org/regexp/)

* ./jakarta-taglibs/standard.jar
- JSP custom tag library (http://jakarta.apache.org/taglibs/)

* ./jasper/jasper-runtime.jar;./jasper/jasper-compiler.jar
- JSP engine Jasper http://tomcat.apache.org
- Required for running the sample applications.

* ./javancss/javancss.jar
- Code metric tool Javancss (http://www.kclee.de/clemens/java/javancss/)
- Required for running code coverage.

* ./jetty/org.mortbay.jetty.jar
- Standalone servlet container (http://jetty.mortbay.org/jetty/)
- Required for running the sample applications.

* ./jhbasic/jhbasic.jar
- Comes bundled with JavaNCSS(http://www.kclee.de/clemens/java/javancss/)

* ./ccl/ccl.jar
- Comes bundled with JavaNCSS(http://www.kclee.de/clemens/java/javancss/)

* ./junit/junit-3.8.1.jar
- JUnit (http://www.junit.org/index.htm)
- Required for running unit-tests.

* ./log4j/log4j-1.2.8.jar
- LOG4J (http://logging.apache.org/log4j/docs/)
- Required for compiling and running the framework and sample applications.

* ./spring/spring-mock.jar;./spring/spring.jar
- Spring Framework (http://www.springframework.org/)
- Required for unittests. 

* ./xdoclet
      xdoclet-web-module-1.2.3.jar
      xdoclet-1.2.3.jar
      xdoclet-xdoclet-module-1.2.3.jar
      xdoclet-hibernate-module-1.2.3.jar
      xjavadoc-1.1.jar
- XDoclet (http://xdoclet.sourceforge.net/xdoclet/index.html)
- Required for generating tld's from the JSP tags' source files.

* ./xstream/xstream-1.1.2.jar
- Xstream (http://xstream.codehaus.org/)
- Required by the framework's serialization filter.