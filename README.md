# P11-code-repository

This project contains source code and supporting files for MedHead project's Proof Of Concept (POC). It includes the following folders.

- back - Spring boot project containing all the files and folders needed to the back of this poc.
- client - React project containing all the files and folders needed to the front of this poc.
- .github/workflows - This folders contains files that describe the git workflows.

# Build back locally

**Prerequisites:**
- Java Development Kit (JDK) Version 17 - Recommended [BellSoft Liberica JDK](https://bell-sw.com/pages/downloads/#/java-17-lts)

Once the prerequisites set up run the following commands to build.
```
cd back
./gradlew.bat build
```

To run the app use this command `./gradlew.bat bootRun`.

To run the unit tests locally use this command `./gradlew.bat test`.

> [!NOTE]
> All above commands are the Windows version if you use a MacOS/Linux Operating system you need to replace `gradlew.bat` with `gradlew` in every commands.

# Build front locally

**Prerequisites:**
- Node.js - Version 18.x
- yarn - Use npm to install it `npm install -g yarn`.

Once the prerequisites set up run the following commands to build.
```
cd front
yarn install // Install project dependencies
yarn build
```

To start a developpement server run this command `yarn dev`.

> [!NOTE]
> The above command will only start the server on your local network if you want to expose it use `yarn dev --host` instead.

To run the application's unit tests use this command `yarn test`.

# Workflows - Back

This workflow use the below actions.

- [actions/checkout](https://github.com/actions/checkout) => This action checks-out your repository under $GITHUB_WORKSPACE, so your workflow can access it.
- [actions/setup-java](https://github.com/actions/setup-java) => This action Download and setup the requested version of java. It also cache gradle dependencies.
- [gradle/gradle-build-action](https://github.com/gradle/gradle-build-action) => This GitHub Action can be used to configure Gradle and optionally execute a Gradle build on any platform supported by GitHub Actions.

On push or pull request on the main branch this workflow will automatically build the back part of the project and run the unit tests related to it.

# Workflows - Front

This workflow use the below actions.

- [actions/checkout](https://github.com/actions/checkout) => This action checks-out your repository under $GITHUB_WORKSPACE, so your workflow can access it.
- [actions/setup-node](https://github.com/actions/setup-node) => This action download and cache distribution of the requested Node.js version and cache yarn dependencies.

On push or pull request on the main branch this workflow will automatically build the front part of the project and run the unit tests related to it.