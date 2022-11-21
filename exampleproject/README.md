# Sample project to demonstrate the turbo-maven-plugin
This multi module Maven exampleproject is set up with the turbo-maven-plugin enabled.

To see the plugin in action, follow these steps:

1. From the exampleproject root, run
```mvn clean install```, watching all the modules get built
3. From the exampleproject root, run ```mvn clean install``` one more time. See that none of the modules are built.
4. Make a small change by adding a comment in ```exampleproject/modules/module-a-child/src/main/java/exampleproject/a/CoolFeature.java```
5. From the exampleproject root, run ```mvn clean install``` one more time. See that only module-a-child and module-a, that depends on its child, get built.


