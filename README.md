#Shoehorn

Shoehorn is a [Java Agent](http://docs.oracle.com/javase/6/docs/api/java/lang/instrument/package-summary.html)
that maps OS environment variables to Java system properties. It is helpful for using with applications that only
accept system properties for configuration when the values are set as environment variables. Of course you could
use `-D` flags to accomplish the same thing, but that can quickly become unmanageable.

##Adding Shoehorn to an App

###Create a Mapping File

To add Shoehorn to an app, first create a `shoehorn.map` mapping file in the root directory of the application.
In this file, list the environment variables that should be mapped to system properties.
You can optionally map the name of environment variable to a different system property name by specifying the
new name after an `=` sign. Additionally, the same environment variable can be mapped to multiple
system properties by listing multiple new names delimited by spaces. Here is an example mapping file:

    ENV_VAR_TO_MAP_AS_IS
    ENV_VAR_TO_MAP_TO_A_NEW_NAME=new.name.as.system.property
    ENV_VAR_TO_MAP_TO_MULTIPLE_NEW_NAMES=new.name.as.system.property another.name.as.system.property

###Adding the Java Agent

Once the mapping file is place, just add `shoehorn.jar` as a Java Agent when launching the app:

    java -javaagent:shoehorn.jar [rest of args for launching the app...]

`shoehorn.jar` can be created independently or integrated into the app's Maven build, as described below.

###Integrating with Maven

If the app using Shoehorn is using Maven, add the following to the app's `pom.xml` to create the `shoehorn.jar` file.

####1. Add the Repository

    <repository>
        <id>github-ryanbrainard</id>
        <name>ryanbrainard's personal repo on github</name>
        <url>https://raw.github.com/ryanbrainard/repo/master</url>
        <layout>default</layout>
    </repository>

####2. Add the Dependency

    <dependency>
        <groupId>com.github.ryanbrainard</groupId>
        <artifactId>shoehorn</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>runtime</scope>
    </dependency>


####3. Add the Build Plugin

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <version>2.3</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals><goal>copy</goal></goals>
                <configuration>
                    <artifactItems>
                        <artifactItem>
                            <groupId>com.github.ryanbrainard</groupId>
                            <artifactId>shoehorn</artifactId>
                            <version>1.0-SNAPSHOT</version>
                            <destFileName>shoehorn.jar</destFileName>
                        </artifactItem>
                    </artifactItems>
                </configuration>
            </execution>
        </executions>
    </plugin>

####4. Point to the Shoehorn JAR

Now when running `mvn package` on the app, the Shoehorn JAR will be copied to `target/dependency/shoehorn.jar`,
so set the path in the `-javaagent` argument appropriately:

    java -javaagent:target/dependency/shoehorn.jar  [rest of args for launching the app...]


###Hacking
If you'd like to contribute to this project, fork it and send me a pull request with tests.