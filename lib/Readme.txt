The following libraries are included in the Aranea Framework distribution because they 
are required for building/testing/running the framework or running the examples. Note 
that each of these libraries is subject to the respective license; check the respective 
project distribution/website before using any of them in your own applications.

buildtime/ - libraries that are only required to build Aranea or run unit tests
optional/ - libraries that are needed at runtime only for certain functionality
required/ - libraries that are required at runtime

*****************************************************************************
* ./buildtime/asm/asm-2.2.1.jar;./buildtime/asm/asm-attrs.jar
- ASM (http://asm.objectweb.org/)
- Required for running the sample application and for generating unittest code 
  coverage.
*****************************************************************************

*****************************************************************************
* ./required/jakarta-regexp-1.2.jar
- Pure Java Regular Expression package (http://jakarta.apache.org/regexp/)
- Required by the framework.
*****************************************************************************

*****************************************************************************
* /required/log4j-1.2.8.jar
- LOG4J (http://logging.apache.org/log4j/docs/)
- Required by the framework.
*****************************************************************************

*****************************************************************************
* ./optional/xstream-1.2.jar
- Xstream (http://xstream.codehaus.org/)
- Required by the framework's serialization testing filter.
*****************************************************************************

*****************************************************************************
* ./optional/base64/base64.jar
- Base64 encoding/decoding (http://iharder.sourceforge.net/base64/)
- Required for compiling the framework and running the client state filter.
*****************************************************************************

*****************************************************************************
* jakarta-commons
- Jakarta Project Commons (http://jakarta.apache.org/commons/)
- ./required/commons-collections-3.1.jar is required by the framework core.
- ./required/commons-lang-2.1.jar is required by the framework.
- ./required/commons-validator-1.3.0.jar is required by the uilib.
- ./optional/commons-fileupload-1.2.jar is required by the framework's file 
   upload filter.
- ./optional/commons-io-1.3.1 is required by commons-fileupload
- ./buildtime/commons-logging.jar is required for building the framework with
  java 1.3 and actually running the sample applications on Jetty.
*****************************************************************************

*****************************************************************************
* ./required/standard.jar
- JSP custom tag library (http://jakarta.apache.org/taglibs/)
- Required by the Aranea presentation.
*****************************************************************************

*****************************************************************************
* ./buildtime/junit-3.8.1.jar
- JUnit (http://www.junit.org/index.htm)
- Required for running unit-tests.
*****************************************************************************

*****************************************************************************
* ./optional/spring/spring-mock.jar;
* ./required/spring/spring.jar
- Spring Framework (http://www.springframework.org/)
- Required by framework when using spring dispatcher.
- Mock objects are needed for running the tests. 
*****************************************************************************

*****************************************************************************
* ./buildtime/cobertura.jar
- Cobertura - code coverage tool (http://cobertura.sourceforge.net/)
- Required for generating unittest code coverage.
*****************************************************************************

*****************************************************************************
* ./buildtime/jakarta-oro/jakarta-oro-2.0.8.jar
- Java classes that provide Perl5 compatible regular expressions, 
  AWK-like regular expressions, glob expressions, and utility classes for 
  performing substitutions, splits, filtering filenames, etc.
- Required for generating unittest code coverage.
*****************************************************************************

*****************************************************************************
* ./buildtime/jhbasic.jar
- Comes bundled with JavaNCSS(http://www.kclee.de/clemens/java/javancss/)
- Required for generating unit test code coverage.
*****************************************************************************

*****************************************************************************
* ./buildtime/ccl.jar
- Comes bundled with JavaNCSS(http://www.kclee.de/clemens/java/javancss/)
- Required for generating unit test code coverage.
*****************************************************************************

*****************************************************************************
* ./buildtime/javancss/javancss.jar
- Code metric tool Javancss (http://www.kclee.de/clemens/java/javancss/)
- Required for running code coverage.
*****************************************************************************

*****************************************************************************
* ./buildtime/xdoclet
      xdoclet-web-module-1.2.3.jar
      xdoclet-1.2.3.jar
      xdoclet-xdoclet-module-1.2.3.jar
      xdoclet-hibernate-module-1.2.3.jar
      xjavadoc-1.1.jar
- XDoclet (http://xdoclet.sourceforge.net/xdoclet/index.html)
- Required for generating tld's from the JSP tags' source files.
*****************************************************************************
