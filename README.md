# turbo-maven-plugin
A maven plugin that turbo charges your Maven build by only building the projects that need to be built.
 
 It does it by analyzing whether source files have changed in the projects / artifacts. No change -> no build.
 
 This is done by comparing the current md5 checksum of each source file with a serialized map of (source file path, source file md5 checksum) for all source files in all projects / artifacts of the build.
 
 The serialized data is stored along the artifacts in the m2 repo.
 
 If a project has a changed source file, it, and the projects dependent on it (downstream projects), will be built.
 ## Usage
 The plugin has the following parameters:
 
 |parameter name|description|default value|
 |-------------|------------|-------------|
 | enabled  | Enables of disables the plugin. | true |
 | ignoreChangesInFiles | Skips build even these files have changed. Comma separated. Checked only by doing a contains of the file name in this string | empty string |
 
 ### Command line parameters
 The parameters can be set on the command line. They will have precedence over the parameters in the plugin configuration.
 
 Example:
 ```
mvn clean install -Dturbo.enabled=false
```
 ### Example plugin configuration:
 ```
 <plugin>
    <groupId>no.sparebank1</groupId>
    <artifactId>turbo-maven-plugin</artifactId>
    <version>${maven-turbo-plugin.version}</version>
    <extensions>true</extensions>
    <configuration>
        <enabled>true</enabled>
        <ignoreChangesInFiles>swagger.html</ignoreChangesInFiles>
    </configuration>
</plugin>
```
You typically add the plugin to the plugins section of your root pom. 

You find the latest version of the plugin from the master branch build [here](https://digitalbankbyggmaster.test.sparebank1.no/job/turbo-maven-plugin_master/).

#### Building with the plugin enabled
The first time you do a build, a normal build will be done, and at the same time, the serialized checksums will be written to your maven repo.

Now, just try trigging a new build, and you will hopefully see your turbo charged build in action?

# Known issues
This plugin only does its magic locally. It will not break anything on the build server, but it will not speed up anything either. We have to figure out how to handle that each build gets its own version first. There is an issue with shared maven repo on the build slaves we have to cater for. 

We have disabled the plugin on the buildserver for now, by having it disable itself when it sees that the path to the local maven repo is /var/cache/maven_repository.

# It doesn't work for me? What do I do?
This plugin is still a child, and needs to face the sometimes odd usecases of enterprise development to grow up and become mature.

Please contact vidar.moe@sparebank1.no with bugs for this plugin, improvement suggestions or just to have a chat?
