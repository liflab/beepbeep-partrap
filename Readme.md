AntRun: a general-purpose Ant build script
==========================================

This project contains a demo of [BeepBeep 3](https://github.com/liflab/beepbeep-3),
a Complex Event Processing and monitoring tool developed at [Laboratoire d'informatique formelle](http://liflab.ca)
in Université du Québec à Chicoutimi.

Building this project
---------------------

To compile the project, make sure you have the following:

- The Java Development Kit (JDK) to compile. The project complies
  with Java version 6; it is probably safe to use any later version.
- [Ant](http://ant.apache.org) to automate the compilation and build process

The palette also requires the following Java libraries:

- The latest version of [BeepBeep 3](https://liflab.github.io/beepbeep-3)
- The latest version of the [BeepBeep 3 palette bundle](https://liflab.github.io/beepbeep-3-palettes)

These dependencies can be automatically downloaded and placed in the
`dep` folder of the project by typing:

    ant download-deps

From the project's root folder, the sources can then be compiled by simply
typing:

    ant
