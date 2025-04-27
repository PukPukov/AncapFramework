DISCLAIMER (if you somehow stumbled upon this repo):

Framework is not considered for everyone/production ready anymore and in the foreseeable future. Basically it is just for me now. Say thanks to everything that happens with minecraft nowadays.

Repository private status reverted because some folks still from time to time want to tinker with ANCAPMINE plugins all of which require framework.

# Build
## One-time
### Requirements
- `JDK 21+` at JAVA_HOME (you can use https://github.com/PukPukov/truewrapper for convenience with multiple projects)
### Steps
- `./mvnw clean install` or `./mvnw21 clean install` if you are using truewrapper
## Development mode
### Requirements
- `mvn` 3 and `mvnd` 1 installed
- `just` installed
- `nushell` installed
- `JDK 21` at JAVA_HOME (you can use https://github.com/PukPukov/truewrapper for convenience with multiple projects)
### Steps
AF builds with just over maven
- `just shoot` for parallel incremental Artifex build
- `just release` for full tested build and local repo installation
- `just reload` try if you have daemon/cache related problems

You also can use maven directly
- `mvnd install` for full build (with driver pot; not needed in 99% cases)

# Usage (Server owner)
## Required environment:
- `Paper API supporting platform`
- `MC 1.21.4+`
- `JDK 21+` installed
- `packetevents` plugin installed
## Installation
Just make sure your server environment matches requirements and install Artifex plugin on it. You can obtain pre-built jars on github releases or build it yourself. 

If you experience any bugs, you can check if everything works correctly specifically in your environment with `/artifex status`.

# Usage (Plugin developer)
AncapFramework is not published to repositories at the time. You need to build it yourself (its easy because build system is maven), and then add local dependency on Artifex:

```xml
<dependency>
    <groupId>ru.ancap</groupId>
    <artifactId>Artifex</artifactId>
    <version>current version</version>
    <scope>provided</scope>
</dependency>
```
or selected modules to not use internals accidentally:
```xml
<dependency>
    <groupId>ru.ancap</groupId>
    <artifactId>CommandAPI</artifactId>
    <version>current version</version>
    <scope>provided</scope>
</dependency>
```

# Overview

Minecraft development framework, currently based on Paper 1.21.4+. Yes, Paper is huge pile of toxic totalitarian shit, and modern minecraft versions are even bigger shit, but doing anything about it in the context of framework requires a lof of effort that I cant provide now.

There are no strict goals, but simplicity, safety, inclusivity and community orientation are "what" and deep reflection is "how".

## Simplicity

You don't need to worry about parsing arguments in commands, writing SQL queries, manually reading configs, or dealing with other repetitive tasks that are not unique to your plugin. Just focus on writing your plugin logic.

## Safety

Finding bugs in runtime using System.out.println() can be frustrating. Much better if those bugs popup early or even totally prevented.

With AncapFramework, heisenbugs will become early runtime error, and early runtime errors in much places will become type errors.

We have integration test framework, StatusAPI. It solves one of the most painful tasks in minecraft development — testing. Usually you are going to manually test what is working if you need integration with minecraft, but now this is no longer a case.

AncapFramework itself is heavily tested and its health status can be checked in Artifex implementation with `/artifex status`.

## Inclusivity

AncapFramework have LanguageAPI module aimed to help in server internationalization. Make your plugin a good choice for inclusive servers that want to allow every nation to play.

## Community orientation

I believe that proper community orientation can not only make people happy, but also help development processes. Make PRs, make issues, make plugins on AncapFramework and show us problems with your own use-case! It is very appreciated!

Yeah no one uses ancap framework except me (maybe i will change it atsp) but community orientation is one of deeply contemplated ideas that solve a lot of problems by-design, also if I am the 100% of community and AF is me-oriented it is 100% community oriented :D

There are also discord server: https://discord.gg/jCcT9vdcpE

# Wiki

There is a [wiki](https://github.com/ancap-dev/AncapFramework/wiki). Its horribly outdated and basically useless because of that though.