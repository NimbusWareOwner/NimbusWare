#!/bin/bash

# Download gradle wrapper jar
echo "Downloading gradle wrapper..."
curl -L -o gradle/wrapper/gradle-wrapper.jar https://github.com/gradle/gradle/raw/v7.3.0/gradle/wrapper/gradle-wrapper.jar

echo "Gradle wrapper setup complete!"