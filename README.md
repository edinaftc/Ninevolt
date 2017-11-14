# Ninevolt
[ ![Download](https://api.bintray.com/packages/edinaftc/ftc/ninevolt/images/download.svg) ](https://bintray.com/edinaftc/ftc/ninevolt/_latestVersion)
[![Build Status](https://travis-ci.org/edinaftc/Ninevolt.svg?branch=master)](https://travis-ci.org/edinaftc/Ninevolt)

Ninevolt is an FTC utilities library that adds a layer on top of the FTC SDK to make it easier to program your robot by having reusable classes for common season-indepent code.

[Looking for the FTC SDK README?](https://github.com/ftctechnh/ftc_app#readme)

## Installing
Ninevolt can be added super easily to your existing FTC project:

`build.common.gradle`:

```gradle
repositories {
    ...
    maven { url 'http://dl.bintray.com/edinaftc/ftc/' }
}
```

`TeamCode/build.gradle`:

```gradle
dependencies {
    compile (group: 'com.edinaftc.ftc', name: 'NinevoltLib', version: '0.1.0', ext: 'aar')
}
```

## Usage
Simply `import` any of Ninevolt's classes and start using it!
A sample autonomous OpMode using an omni-swerve chassis is available in [`HoloAutoOpMode.java`](src/main/java/com/edinaftc/ninevolt/examples/HoloAutoOpMode.java).

A full javadoc is available here: [https://edinaftc.github.io/Ninevolt](https://edinaftc.github.io/Ninevolt)
