# Contributing Guidelines

Thank you for your interest in contributing to this project. Whether it's a bug report, new feature, correction, or
additional documentation, we greatly value feedback and contributions from our community.

Please read through this document before submitting any issues or pull requests to ensure we have all the necessary
information to effectively respond to your bug report or contribution.

## Building From Source

### Requirements

* Java 11
* Git

### Instructions

1. Clone the github repository and run `./gradlew buildPlugin` <br/> (This will produce a plugin zip
   under `CodeTester-IntelliJ-IDEA/build/distributions`)
2. In your JetBrains IDE (e.g. IntelliJ) navigate to the `Plugins` preferences and select "Install Plugin from Disk...",
   navigate to the zip file produced in step 1.
4. You will be prompted to restart your IDE.

## Contributing via Pull Requests

Contributions via pull requests are much appreciated. Before sending us a pull request, please ensure that:

1. You are working against the latest source on the *main* branch.
2. You check existing open, and recently merged, pull requests to make sure someone else hasn't addressed the problem
   already.
3. You open an issue to discuss any significant work - we would hate for your time to be wasted.

To send us a pull request, please:

1. Fork the repository

2. Modify the source; please focus on the specific change you are contributing. *(note: all changes should have
   associated automated tests)*

3. Ensure local tests pass by running:
   ```
   ./gradlew check
   ```
   or use the predefined ``Run Unit Tests`` Task in you're IDE.

4. Ensure local ui tests pass by running:
   ```
   ./graldew uiTests
   ```

   or use the predefined ```Run Ui Tests`` Task in you're IDE.

5. Ensure local plugin Verifier task pass by running:
   ```
   ./gradlew CodeTester-IntelliJ-IDEA:runPluginVerifier
   ```
   or use the predefined ```Run Verifications``` Tasks in you're IDE.

6. Add you're change log to the change log file in ``./docs/CHANGELOG.md`` . Change log entries should describe the
   change succinctly and may include Git-Flavored Markdown ([GFM](https://github.github.com/gfm/)). Reference the Github
   Issue # if relevant.

7. Commit to your fork using clear commit messages. Again, reference the Issue # if relevant.

8. Send us a pull request by completing the pull-request template.

9. Pay attention to any automated build failures reported in the pull request.

10. Stay involved in the conversation.

## Debugging / Running Locally

To test your changes locally, you can run the project from IntelliJ or gradle.

- **Simple approach:** from the top-level of the repository, run:
  ```
  ./gradlew runIde --info
  ```
  The `runIde` task automatically downloads the version predefine the gradle.properties/platfromVersion of IntelliJ
  Community Edition, builds and installs the plugin, and starts a _new_
  instance of IntelliJ with the built extension.
- To run **unit tests**:
  ```
  ./gradlew check
  ```
- To run **GUI tests**:
  ```
  ./gradlew uiTest
  ```
    - To debug GUI tests,
        1. Start the IDE that will be debugged `./gradlew :CodeTester-IntelliJ-IDEA:runIdeForUiTests --debug-jvm`
        2. In your running Intellij instance `Run -> Attach to process` attach to the ide test debug process.
        4. Run `./gradlew uiTest`. This will attach to the running debug IDE instance and run tests.

## Finding contributions to work on

Looking at the existing issues is a great way to find something to contribute on. 
