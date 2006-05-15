Aranea framework main example - simple webapp demonstrating usage of 
different widgets and concepts.

For running example the in-memory database should be started first:
   ant run-database
and then
   ant run-app

Running webapp can be accessed at http://localhost:2000

=================================

 Hibernate is high-performance object-relational mapping solution written in Java.
 It takes care of mapping database tables to objects and much more. In our 
 examples its usage is very basic— just mapping database columns to fields in Data
 Objects (simple Java beans with getter and setter methods for private fields)—so 
 that we can easily save and retrieve objects from/to database. Mappings between
 Java objects and database tables are generated automatically from xdoclet 
 annotations—take a look at war/WEB-INF/classes/mappings  directory to see what
 typical generated mappings look like—or any classes from 
 org.araneaframework.templateApp.business.model  to see samples of annotations from
 which mappings are generated. 
 Hibernate homepage is located at http://www.hibernate.org.

Some examples (especially SQL-based lists) need to use relational database to be 
informational. We have used capable little relational database called HSQLDB for 
that purpose. You should start database with command ant run-database before 
running main examples, otherwise some of them will not function correctly. HSQLDB 
and Hibernate are tied together in etc/hibernate.cfg.xml where SQL dialect and 
database used by Hibernate are configured.

=================================

Logging for all examples is configured in examples/log4j.xml file. Verbosity
mode for logging statements is selected here—the level of logging statements
that are actually output. By default this is set to DEBUG level for most
packages. Just one appender is defined—one that sends logging statements to 
standard output:

<appender name="CONSOLE" class="org.apache.log4j.ConsoleAppender">
  <layout class="org.apache.log4j.PatternLayout">
    <param name="ConversionPattern" value="%d{hh:mm:ss} %-5p-%c{1}.%M(%F:%L)-%m%n"/>
  </layout>
</appender>

Different packages are easily configured to have different logging verbosity
levels and various output destinations:

<category name="org.araneaframework">
  <priority value="DEBUG"/>
  <appender-ref ref="CONSOLE"/>
</category>

For detailed information about using log4j logging framework refer to 
http://logging.apache.org/log4j/docs/
