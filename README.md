logviewer
=========

Web Application to view log files

The application provides with a basic web interface to view server side log files.
The user has to configure an XML file with the log files and path of log files to be monitored.

To run the project, please do the following steps:

1. Create the following folder structure

B:\Jeffrey\ProgramFiles\logviewer

2. Copy the following 2 files present in WebContent into the folder created abovve

environments.xml
logviewer.properties

environments.xml - File where log file names and corresponding path is specified
logviewer.properties - File to specify the path of 'environments.xml'

3. Create folder structure as specified in the file 'environments.xml'. According the 'environments.xml' available, folder structure
to be created is,

B:\Jeffrey\ProgramFiles\logviewer\weblogic_1
B:\Jeffrey\ProgramFiles\logviewer\weblogic_2
B:\Jeffrey\ProgramFiles\logviewer\weblogic_3

4. Put some sample text files with name 'ALERT.log', 'csom.log', 'gc.log'.

5. Run the application with Tomcat 7.0 Server or Web Logic 10.3.6 or above server.

