

THIS README FILE CONTAINS INFORMATION ABOUT FILES IN "/etc/jsutil/" INSIDE AN ARANEA PROJECT.


1. JSMin
---------

The "jsmin.java" file was originally copied from http://inconspicuous.org/projects/jsmin/jsmin.java. Unfortunately, no
JAR package for "jsmin" existed at the time, only this source code was available. Therefore, we have to accept with that
until there is an official package that can be fetched with Maven or Ivy.

There is a small likelyhood that anyone might want to modify the source. Therefore, no build script takes care of
building the JAR, although "/build.xml" script depends on "JSMin.jar". The JAR was built manually, and so should others
do until it is revised to be done by build scripts.

The commands that was used to build the JAR:

On Windows command-line:
javac org/inconspicuous/jsmin/JSMin.java
jar cfm JSMin.jar JSMin.MF org/inconspicuous/jsmin/* README.TXT
del org\inconspicuous\jsmin\*.class

On Unix/Linux/[other] command-line:
javac org\inconspicuous\jsmin\JSMin.java
jar cfm JSMin.jar JSMin.MF org\inconspicuous\jsmin\* README.TXT
rm org\inconspicuous\jsmin\*.class


Martti Tamm (martti@araneaframework.org)
December 3rd, 2009
