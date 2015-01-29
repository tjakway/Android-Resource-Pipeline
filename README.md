Project Description
-----------------------
This project exists to reduce the irritation of dealing with Android screen densities.  The goal is to integrate seamlessly with the gradle to export rasterized images for each Android screen density from an input set of SVG images.
(automatically resizing PNG/JPEGs to Android screen densities is a long-term goal but not recommended because of the loss in quality)

Status
-----------------------
Still in beta.
Most of the hard work is done (like the Batik code to convert SVG -> PNG), but it needs a lot of polish (especially with gradle).

TODO:
-----------------------
1. Convert all projects to use JCommander for more standard command line handling.
2. Integrate with gradle as a task.

License:
-----------------------
The Android Resource Pipeline is licensed under a permissive open source license (Apache 2).
