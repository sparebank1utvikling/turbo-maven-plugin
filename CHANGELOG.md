## 1.1.0
- Made directories that will be checked under pom (child) projects configurable. It is possible to use * to include all directories. Default if not set, is `src` directory as before. See [README.md](README.md)
     ```xml
      <includeTopDirectories>*</includeTopDirectories>
- Also made it possible to exclude directories. * can not be used. Default if not set, nothing will be excluded.
    ```xml
    <excludeTopDirectories>target,node_modules</excludeTopDirectories>
- Bugfix so that `ignoreChangesInFiles` can contain more than one file (comma separated).

## 1.0.0
- Initial version
