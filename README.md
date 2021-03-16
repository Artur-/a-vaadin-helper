A random set of utility methods for Vaadin 14+ applications, Java and TS

To build the package, run
```
npm install
rm src/main/resources/META-INF/resources/frontend/a-vaadin-helper-bundle.js
rollup -c 
mvn versions:set -DnewVersion=
mvn clean install -Pdirectory
```
