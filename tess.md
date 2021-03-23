+++
categories = ["recipes"]
tags = ["scala", "java", "fast parse","parser"]
summary = "Running the scala java parser"
title = "Parse Java Script"
taxonomy = ["script"]
review_status = ["NOT DONE"]
date = 2017-09-29T15:00:37-04:00
anchor = "parseJavaScript"
+++

### General steps to run the parseJava script

The parseJava script will parse all java files, extract any class and method signatures and output them into a new file in the specified target directory.  The script will also generate all the package folders to mirror the original package.  If there are method signatures it can't parse, it will generate the error file called (parseJava-error.txt) in the target directory.

### Skairipa-tools
1. parsejava.sc
1. uncrustify.config

### Setup
The parseJava.scala script is located in skairipa-tools repository.  In order to run this script, you will need to install the following:

1. Scala 2.12, copy the command below to a terminal:
   `sudo curl -L -o /usr/local/bin/amm https://git.io/v5PPx && sudo chmod +x /usr/local/bin/amm && amm`

1. Uncrustify, copy the command below to a terminal:
   `brew install uncrustify`

1. Copy the parseScala.sc to `/usr/local/bin`

### Instructions

#### parseJava.sc usage message
```
$ parseJava.sc
Missing arguments: (--relativePathToFile: String, --targetDir: String, --uncrustifyConfig: String)

Arguments provided did not match expected signature:

run
The parseJava script extracts existing Java class and method signatures into a
new java file.  The script needs to run at the top level where you want the
script to mirror subsequent package directories.
--relativePathToFile  String: Current top level of the Java package folder
--targetDir           String: Full path of output directory
--uncrustifyConfig    String: OPTIONAL: Full path to uncrustify config file
```

1. On the terminal, change your current working directory to the top level of the java package folder. For instance, if your project directory is `myproject/main/java/mil/af/example` then go to `cd my/project/main/java` as your starting point.
1. Determine where you will have the output files, e.g. `/Users/pivotal/output`
1. On the terminal, type in the following command:
   ```find . -name '*.java' -exec parseJava.sc {} '/Path/To/Output/Folder' \; ```

#### Exclude directories
To have find exclude directories, use `-not -path *<folderName>*`
```find . -name '*.java' -not -path "*wsdl*" -not -path "*interface*" -exec parseJava.sc {} '/Path/To/Output/Folder' \; ```


#### Uncrustify option
This feature is currently under development.  If you choose to have uncrustify beautified your output java files, then use the uncrustify.config file (located in skairipa-tools repo).

1. Below is an example of how you can run uncrustify separately:
```
uncrustify -c /path/to/uncrustify.config -f /path/to/file -o /path/to/file
```

For multiple lines processing:
```
find /Path/To/Output/Folder -name '*.java' -exec uncrustify -c /Path/To/uncrustify.config -f {} -o {} \;
```