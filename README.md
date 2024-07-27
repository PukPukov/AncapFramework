Спик рашн? [Смотри русское ридми!](README_RU.md)

# Overview

Minecraft development framework, currently based on Bukkit.

We have no strict goals, but simplicity, safety and inclusivity is that what we do and community orientation is how we do.

## Simplicity

You don't need to worry about parsing arguments in commands, writing SQL queries, manually reading configs, or dealing with other repetitive tasks that are not unique to your plugin. Just focus on writing your plugin logic.

## Safety

Finding bugs in runtime using System.out.println() — bad idea. Finding bugs at compile-time or even ide-time — much better idea.

The idea is to raise the errors as early as possible. With AncapFramework, heisenbugs will become early runtime error, and early runtime errors in much places will become type errors.

We have integration test framework, StatusAPI. It solves one of the most painful tasks in minecraft development — testing. Usually you are going to manually test what is working if you need integration with minecraft, but now this is no longer a case.

AncapFramework itself is heavily tested and its health status can be checked in Artifex implementation with `/artifex status`.

## Inclusivity

AncapFramework have LanguageAPI module aimed to help in server internationalization. Make your plugin a good choice for inclusive servers that want to allow every nation to play.

## Community orientation

We believe that proper community orientation can not only make people happy, but also help development processes. Make PRs, make issues, make plugins on AncapFramework and show us problems with your own use-case! It is very appreciated!

We also have discord server: https://discord.gg/jCcT9vdcpE

# Development status and Internal compatibility status

TL;DR: We will break backward compatibility in the future, but this is not a big deal.

Current development cycle is `1.7-pre`. You can think about is as something between 1.6 and 1.7.

This is so because 1.7 version has been in development for a while and appeared to be much more hard to release then was expected. There are some scheduled backward compatibility breaks that will be introduced in 1.7 due to past development flaws. For convenience, all of them developed in the different branches and will be introduced atomically.

Until then, and afterward, backward compatibility will be maintained. Version 1.8 will also include an officially supported method to shade some parts of AncapFramework as an additional measure against backward compatibility breaks.

Although it might seem that AncapFramework is not suitable for production development and appears like a "beta" version, this is not the case. AncapFramework has been tested in production on many servers and `1.7-pre` cycle is fully suitable for production use. It is more stable then 1.6, less stable then 1.7.

Version 1.7 is simply a major enhancement that will greatly improve performance and usage convenience by the cost of a bit of pain. The expected migration effort can be compared to migrating a fabric mod from Minecraft 1.20.1 to Minecraft 1.20.2, or even less.

We will make exhausting migration guide.

# External compatibility status

Currently supported environments: Bukkit 1.8.9-1.12.2; 1.20.2+

Planned support is Bukkit 1.8.9+. 1.13-1.20.1 are not supported currently due to issue with Java support by server core. If you wondering, whats the issue: these cores are using ASM library that is using Java internals that are not supporting backward compatibility, so ASM case is case when Java does not provide backward compatibility, so if you want newer Java, you need to update ASM. Paper refused to change one number to help us or even accept PR that will change it for them. 

This is not a policy of AncapFramework and Artifex was specifically supporting MC 1.8.9+, including MC 1.16.5 prior to update to JDK 21, and with AF 1.7 release [Mojalemi](https://github.com/ancap-dev/Mojalemi) project will also be released, that is aimed to bring support of modern Java to MC 1.13-1.20.1 (MC 1.16.5 firstly).

Probably, 1.7.10 support could be achieved easily by downgrading bukkit dependency version, but now this have low priority. Although PRs are appreciated.

Even older minecraft versions (like beta 1.7.3) can't be supported easily because

- packetevents and kyori adventure does not support versions older then 1.7

- Of increasing difficulty of even older versions supporting due to lack of forward compatibility in bukkit that forces usage of the most old api that is wanted to support

But this is theoretically possible by moving AncapFramework to new API that is designed to maintain forward compatibility, so the priority is "never, but what an idea!". Sponge, fabric, forge, neoforge, quilt, etc support is also could be achieved by rebuilding on top of that hypothetical API.

# Usage (Server owner)
Required environment:

- `Spigot API supporting platform` (Spigot, Paper or other forks, maybe mohist or magma)
- `MC 1.8.9-1.12.2; 1.20.2+` (*why no 1.12.2-1.20.1 and when it will be check in "External compatibility status" section*)
- `JDK 21+` installed
- `packetevents` plugin installed

Just install Artifex plugin on your server and make sure it matches required environment. If you experience any bugs, you can check if everything works correctly specifically in your environment with `/artifex status`.

# Usage (Plugin developer)
![Release](https://jitpack.io/v/ancap-dev/AncapFramework.svg?style=flat-square)

Use everything at once:

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.ancap-dev</groupId>
    <artifactId>AncapFramework</artifactId>
    <version>look above</version>
    <scope>provided</scope>
</dependency>
```

Use modules:

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.ancap-dev.AncapFramework</groupId>
    <artifactId>AncapPluginAPI</artifactId>
    <version>look above</version>
    <scope>provided</scope>
</dependency>
```
We recommend to use modules, AncapFramework by design is a modular project, but, no one enforces you to use it like that.

# Wiki

There is a [wiki](https://github.com/ancap-dev/AncapFramework/wiki). It is not very up-to-date though.