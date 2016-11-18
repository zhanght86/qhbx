SET CLASSPATH=lib\ant.jar;lib\ant-launcher.jar;lib\xercesImpl.jar;%JAVA_HOME%\lib\tools.jar
java -Dant.home=%cd% org.apache.tools.ant.launch.Launcher -f build.xml package_release
echo done
pause