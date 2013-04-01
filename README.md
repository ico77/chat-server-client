# Chat Server and Client (WIP)#

## Description ##

This is a showcase implementation of a chat server and client using Java. The
server component is standalone and multithreaded. Threads are obtained from a
thread pool and used for managing client connections. Clients are implemented
as simple Swing applications.

Clients communicate with the server using Sockets. The server component caches
Socket output streams making it easier to publish messages to clients.

## Build ##

The project can be built:  
+ by opening the project in NetBeans and using the *Run->Build Project* command
  (F11)
+ using the supplied ant scripts from the command line

**Prerequisite jars**  
log4j-api-2.0-beta4.jar  
log4j-core-2.0-beta4.jar

**Run**  
After building and packaging the project use the following commands to run the
server and client:  
Server: `java -classpath JavaChat.jar:lib/* hr.ivica.chat.server.ChatServer`  
Client: `java -classpath JavaChat.jar:lib/* hr.ivica.chat.client.ChatClientView`  
  
The above commands assume the prerequisite jar files are placed in lib/ relative to JavaChat

