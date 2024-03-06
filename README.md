# turbo-maven-plugin
A maven plugin that turbo charges your Maven build by only building the projects that need to be built.
 
 It does it by analyzing whether source files have changed in the projects / artifacts. No change -> no build.
 
 This is done by comparing the current md5 checksum of each source file with a serialized map of (source file path, source file md5 checksum) for all source files in all projects / artifacts of the build.
 
 The serialized data is stored along the artifacts in the m2 repo.
 
 If a project has a changed source file, it, and the projects dependent on it (downstream projects), will be built.
 ## Usage
 The plugin has the following parameters:
 
 |parameter name| description                                                                                                                                                                     | default value |
 |------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|
 | enabled  | Enables of disables the plugin.                                                                                                                                                 | true          |
 | alwaysBuildModules  | Always build the specified modules. Comma separated. Checked only by doing a contains of the file name in this string                                                           | empty string  |
 | ignoreChangesInFiles | Skips build even these files have changed. Comma separated.                                                                                                                     | empty string  |
 | includeTopDirectories | Specify which top directories under pom (child) project that will be included in check. Use * to include all. Comma separated. If not used, only src directory will be included | src           |
 | excludeTopDirectories | Specify which top directories under pom (child) project that will be excluded in check. Comma separated. If not used, nothing explicit will be excluded                         | empty string  |
 
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
        <alwaysBuildModules>distribution</alwaysBuildModules>
        <ignoreChangesInFiles>swagger.html</ignoreChangesInFiles>
    </configuration>
</plugin>
```

### Example of more advanced plugin configuration:
 ```
 <plugin>
    <groupId>no.sparebank1</groupId>
    <artifactId>turbo-maven-plugin</artifactId>
    <version>${maven-turbo-plugin.version}</version>
    <extensions>true</extensions>
    <configuration>
        <enabled>true</enabled>
        <ignoreChangesInFiles>swagger.json,.versionsBackup,.jar</ignoreChangesInFiles>
        <alwaysBuildModules>distribution</alwaysBuildModules>
        <includeTopDirectories>*</includeTopDirectories>
        <excludeTopDirectories>target,node_modules</excludeTopDirectories>
    </configuration>
</plugin>
```

You typically add the plugin to the plugins section of your root pom. 

#### Building with the plugin enabled
The first time you do a build, a normal build will be done, and at the same time, the serialized checksums will be written to your maven repo.

Now, just try trigging a new build, and you will hopefully see your turbo charged build in action?
