# Project (backend + frontend submodule)
## Setup
- Terminal: `git clone --recurse-submodules URL`
- IDE: [guide for IntelliJ IDEA](https://www.jetbrains.com/help/idea/manage-projects-hosted-on-github.html#clone-from-GitHub)
## Maven goals
- Clean: `mvn clean`
- Run: `mvn spring-boot:run`
- Run tests: `mvn -P-webapp test`
- IDE: [guide for IntelliJ IDEA](https://www.jetbrains.com/help/idea/work-with-maven-goals.html#run_goal)
## Project hierarchy
- Tests: [`src/test/java`](src/test/java)
- Backend: [`src/main/java`](src/main/java)
- Frontend: [`src/main/webapp`](https://github.com/uji-new/webapp)
## Recommended software
- JDK: [OpenJDK 17](https://jdk.java.net/17)
- IDE: [IntelliJ IDEA 2021](https://www.jetbrains.com/idea)
## Organization repositories
### Project
- Backend: [`app`](https://github.com/uji-new/app)
- Frontend: [`webapp`](https://github.com/uji-new/webapp)
- Docs: [`documentation`](https://github.com/uji-new/documentation)
- Prototype: [`spike`](https://github.com/uji-new/spike)
