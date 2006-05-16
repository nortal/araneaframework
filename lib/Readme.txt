The following libraries are included in the Aranea Framework distribution because they 
are required for building the framework Note that each of these libraries is subject
to the respective license; check the respective project distribution/website before using
any of them in your own applications.

*****************************************************************************
* ./jakarta-regexp/jakarta-regexp-1.2.jar
- Pure Java Regular Expression package (http://jakarta.apache.org/regexp/)
- Required by the framework.
*****************************************************************************

*****************************************************************************
* ./log4j/log4j-1.2.8.jar
- LOG4J (http://logging.apache.org/log4j/docs/)
- Required by the framework.
*****************************************************************************

*****************************************************************************
* ./xstream/xstream-1.1.2.jar
- Xstream (http://xstream.codehaus.org/)
- Required by the framework's serialization filter.
*****************************************************************************

*****************************************************************************
* ./base64/base64.jar
- Base64 encoding/decoding (http://iharder.sourceforge.net/base64/)
- Required for compiling the framework and running the client state filter.
*****************************************************************************

*****************************************************************************
* ./jakarta-commons/commons-*.jar
- Jakarta Project Commons (http://jakarta.apache.org/commons/)

- Collections is required by the framework core.
- Lang is required by the framework.
- Fileupload is required by the framework's FileUpload service.
*****************************************************************************

*****************************************************************************
* ./jakarta-taglibs/standard.jar
- JSP custom tag library (http://jakarta.apache.org/taglibs/)
- Required by the Aranea presentation.
*****************************************************************************

*****************************************************************************
* ./junit/junit-3.8.1.jar
- JUnit (http://www.junit.org/index.htm)
- Required for running unit-tests.
*****************************************************************************

*****************************************************************************
* ./spring/spring-mock.jar;./spring/spring.jar
- Spring Framework (http://www.springframework.org/)
- Required for unittests. 
*****************************************************************************

*****************************************************************************
* ./cobertura/cobertura.jar
- Cobertura - code coverage tool (http://cobertura.sourceforge.net/)
- Required for generating unittest code coverage.
*****************************************************************************

*****************************************************************************
* ./jhbasic/jhbasic.jar
- Comes bundled with JavaNCSS(http://www.kclee.de/clemens/java/javancss/)
- Required for generating unit test code coverage.
*****************************************************************************

*****************************************************************************
* ./ccl/ccl.jar
- Comes bundled with JavaNCSS(http://www.kclee.de/clemens/java/javancss/)
- Required for generating unit test code coverage.
*****************************************************************************

*****************************************************************************
* ./javancss/javancss.jar
- Code metric tool Javancss (http://www.kclee.de/clemens/java/javancss/)
- Required for running code coverage.
*****************************************************************************

*****************************************************************************
* ./xdoclet
      xdoclet-web-module-1.2.3.jar
      xdoclet-1.2.3.jar
      xdoclet-xdoclet-module-1.2.3.jar
      xdoclet-hibernate-module-1.2.3.jar
      xjavadoc-1.1.jar
- XDoclet (http://xdoclet.sourceforge.net/xdoclet/index.html)
- Required for generating tld's from the JSP tags' source files.
*****************************************************************************