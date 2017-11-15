# Ninevolt
[ ![Download](https://api.bintray.com/packages/edinaftc/ftc/NinevoltLib/images/download.svg) ](https://bintray.com/edinaftc/ftc/NinevoltLib/_latestVersion)
[![Build Status: master](https://travis-ci.org/edinaftc/Ninevolt.svg?branch=master)](https://travis-ci.org/edinaftc/Ninevolt)
[![Build Status: 1.0-alpha](https://travis-ci.org/edinaftc/Ninevolt.svg?branch=1.0-alpha)](https://travis-ci.org/edinaftc/Ninevolt)

Ninevolt is an FTC utilities library that adds a layer on top of the FTC SDK to make it easier to program your robot by having reusable classes for common season-indepent code.

[Looking for the FTC SDK README?](https://github.com/ftctechnh/ftc_app#readme)

## Installing
Ninevolt can be added super easily to your existing FTC project:

`TeamCode/build.gradle`:

```gradle
dependencies {
    compile (group: 'com.edinaftc.ftc', name: 'NinevoltLib', version: '0.1.0', ext: 'aar')
}
```

## Usage
Simply `import` any of Ninevolt's classes and start using it!
A sample autonomous OpMode using an omni-swerve chassis is available in [`HoloAutoOpMode.java`](src/main/java/com/edinaftc/ninevolt/examples/HoloAutoOpMode.java).

A full Javadoc is available here: [https://edinaftc.github.io/Ninevolt/javadoc](https://edinaftc.github.io/Ninevolt/javadoc)
