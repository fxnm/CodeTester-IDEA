# CodeTester-IDEA Changelog

## [Unreleased]
### Added
- Added AutoUpdate form open Check Result Tool Windows after a new Scan 
- Added Button to Jump directly to the Check Result Summary Tool Window
- Added Button to close all open Check Result Panels
- Added indication if the test crashed
- Added Plugin Verifier IDE Version 202.3
- Added new Modul Structure
- Added predefined Run / Debug / Test Configurations
- Added new Pull Request Template
- Added Security Policy
- Added Copy Button in the Check Result for each In- and Output Line

### Changed
- Changed Login Dialog implementation
- Changed ToolWindow Base and simplified the component structure
- Migrated settings.gradle to Kotlin
- Changed Feature Request Issue Template label  
- Changed Multi Threading to Runnable and the corresponding Futures 

### Fixed
- Fixed Removal of all open Check Result ToolWindows if user is logging out
- Fixed Login Dialog and error if login was canceled
- Fixed typos
- Fixed not disabling logout action if CodeTester is running 
- Fixed check result filtering after rerun

### Removed

## [1.0.2]
### Added
- Added settings Menu
- Added Global Settings
- Added Project Settings
- Added PopupNotifier with fix Action
- Added Plugin Code support for Kotlin

### Changed
- Changed BaseURL in CommonURL to use the BaseURL from Project Settings
- Changed Implementation for the UI Feedback Listeners
- Changed gradle to the Kotlin DSL
- Changed Plugin appearance in the Settings/Plguins Menu

### Removed
- Changelog Action from the master release workflow

### Fixed
- Fixed missing dependency to com.intellij.java

## [1.0.1]
### Added
- Added new gradle runPluginVerifier action

### Changed
- Changed Runtime IDEA Version
- Changed supported IDEA Types form all to only IntelliJ IC and IU

### Fixed
- Fixed Master Release Merge Workflow by changing 'from_branch' to 'head_to_merge'
- Fixed Deprecations form IDEA 203

## [1.0.0]
### Added
- Initial release
