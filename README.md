![Project icon](https://git-assets.jellysquid.me/hotlink-ok/lithium/icon-rounded-128px.png)

# Lithium (for Fabric) - Fork for HopperOptimizations

Lithium is a free and open-source Minecraft mod which works to optimize many areas of the game in order to provide
better overall performance. It works on both the **client and server**, and **doesn't require the mod to be installed
on both sides**. The original project can be fround at https://github.com/jellysquid3/lithium-fabric.

### Downloads

You can find downloads for Lithium on the [GitHub releases page](https://github.com/2No2Name/lithium-fabric/releases).

### Installation instructions

Lithium relies on the [Fabric Loader](https://fabricmc.net/use). Users should select Fabric for either the Minecraft launcher (client) or
the dedicated server (server) depending on their needs.
Once you have installed Fabric, place the Lithium .jar in the `mods` folder generated by Fabric.

### Community

If you'd like to check out the latest developments, the Discord community might be for you!
You can join the base project's server for Jelly's mods by clicking
[here](https://jellysquid.me/discord).

### Support the developers

Lithium is made possible by the following core contributors [and others](https://github.com/jellysquid3/lithium-fabric/graphs/contributors).
You can help support members of the core team by making a pledge to our Patreon pages below.

|    | Author   | Role   | Links   |
|----|:---------|:-------|:--------|
| ![jellysquid3's Avatar](https://avatars3.githubusercontent.com/u/1363084?s=32) | jellysquid3 | Project Lead | [Patreon](https://patreon.com/jellysquid) / [Contributions](https://github.com/jellysquid3/lithium-fabric/commits?author=jellysquid3) |
| ![2No2Name's Avatar](https://avatars3.githubusercontent.com/u/50278648?s=32) | 2No2Name | Maintainer | [Patreon](https://patreon.com/2No2Name) / [Contributions](https://github.com/jellysquid3/lithium-fabric/commits?author=2No2Name) |

---

### What makes Lithium different?

One of the most important design goals in Lithium is *correctness*. Unlike other mods which apply optimizations to the
game, Lithium does not sacrifice vanilla functionality or behavior in the name of raw speed. It's a no compromises'
solution for those wanting to speed up their game, and as such, installing Lithium should be completely transparent
to the player.

If you do encounter an issue where Lithium deviates from the norm, please don't hesitate to
[open an issue.](https://github.com/2No2Name/lithium-fabric/issues) Each patch might be carefully checked to ensure
vanilla parity, but after all, bugs are unavoidable.

### Configuration

Out of the box, no additional configuration is necessary once the mod has been installed. Lithium makes use of a
configuration override system which allows you to either forcefully disable problematic patches or enable incubating
patches which are otherwise disabled by default. As such, an empty config file simply means you'd like to use the
default configuration, which includes all stable optimizations by default. 

See [the Wiki page](https://github.com/jellysquid3/lithium-fabric/wiki/Configuration-File) on the configuration file
format and all available options.

---

### Building from source

If you're hacking on the code or would like to compile a custom build of Lithium from the latest sources, you'll want
to start here.

#### Prerequisites

You will need to install JDK 8 in order to build Lithium. You can either install this through a package manager such as
[Chocolatey](https://chocolatey.org/) on Windows or [SDKMAN!](https://sdkman.io/) on other platforms. If you'd prefer to
not use a package manager, you can always grab the installers or packages directly from
[AdoptOpenJDK](https://adoptopenjdk.net/).

On Windows, the Oracle JDK/JRE builds should be avoided where possible due to their poor quality. Always prefer using
the open-source builds from AdoptOpenJDK when possible.

#### Compiling

Navigate to the directory you've cloned this repository and launch a build with Gradle using `gradlew build` (Windows)
or `./gradlew build` (macOS/Linux). If you are not using the Gradle wrapper, simply replace `gradlew` with `gradle`
or the path to it.

The initial setup may take a few minutes. After Gradle has finished building everything, you can find the resulting
artifacts in `build/libs`.

---

### License

Lithium is licensed under GNU LGPLv3, a free and open-source license. For more information, please see the
[license file](https://github.com/jellysquid3/lithium-fabric/blob/1.16.x/fabric/LICENSE.txt).
