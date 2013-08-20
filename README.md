# HttpClient Commandline Tool

You want `cURL`, but don't have it. You're on a Linux/Solaris machine, but can't install packages. There is no compiler installed. What ca you do? With the command line tool for Apache HttpClient you can do some basic commands like `cURL`.

## Bulding 

You need maven to build this project. Run the following command to generate a JAR with all dependencies: 

    mvn clean install

## Usage

Currently only HTTP GET requests are impleneted. There are options: 

  - `-I --header` will only show the response headers
  - `-L --location` will handle redirects 

Sample usage: 

    java -jar httpclient-options-1.0-SNAPSHOT-jar-with-dependencies.jar -I http://www.google.com


