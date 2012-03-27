#Shoehorn

Shoehorn is a [Java Agent](http://docs.oracle.com/javase/6/docs/api/java/lang/instrument/package-summary.html)
that maps OS environment variables to Java system properties. It is helpful for using with applications that only
accept system properties for configuration, but [the values are set as environment variables](http://www.12factor.net/config).
Of course you could use `-D` flags to accomplish the same thing, but that can quickly become unmanageable.

##Adding Shoehorn to an App

###Setup Mappings

To use Shoehorn with an app, key-value mappings need to be created to assign environment variables to system properties.
For example:

    system.property.name=ENV_VAR_NAME

The mappings themselves are provided to Shoehorn as environment variables starting with `SHOEHORN_MAP`.
Multiple mappings can be can be placed on the same line with `;` delimiters.
For example, to set the mappings above as envronment variables:

    export SHOEHORN_MAP_SINGLE="system.property.name=ENV_VAR_NAME"
    export SHOEHORN_MAP_MULTI="system.property.name=ENV_VAR_NAME;another.system.property.name=ANOTHER_ENV_VAR_NAME"

Notice how the mappings can all be lumped into one environment variable delimited by `;` or configured separated.

###Adding the Java Agent

Once the mappings are place, just add `shoehorn.jar` as a Java Agent when launching the app:

    java -javaagent:shoehorn.jar [rest of args for launching the app...]

`shoehorn.jar` can be created independently or integrated into the app's Maven build, as described below.

###Integrating with Maven

If the app using Shoehorn is using Maven, add the following to the app's `pom.xml` to create the `shoehorn.jar` file.

####1. Add the Build Plugin

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
                            <version>0.1</version>
                            <destFileName>shoehorn.jar</destFileName>
                        </artifactItem>
                    </artifactItems>
                </configuration>
            </execution>
        </executions>
    </plugin>

####2. Point to the Shoehorn JAR

Now when running `mvn package` on the app, the Shoehorn JAR will be copied to `target/dependency/shoehorn.jar`,
so set the path in the `-javaagent` argument appropriately:

    java -javaagent:target/dependency/shoehorn.jar  [rest of args for launching the app...]


###Hacking
If you'd like to contribute to this project, fork it and send me a pull request with tests.