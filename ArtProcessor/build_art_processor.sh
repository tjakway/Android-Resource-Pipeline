
ant -buildfile build.xml create_run_jar
sudo java -Xms1000m -Xmx2500m -Xss100m -jar art_processor.jar in/ out/ --overwrite=on
