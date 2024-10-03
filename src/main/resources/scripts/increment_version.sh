#!/bin/sh

# Define the paths
VERSION_FILE="src/main/resources/versioning/version.txt"
POM_FILE="pom.xml"

# Check if the version file exists
if [ ! -f "$VERSION_FILE" ]; then
  echo "Error: $VERSION_FILE not found!"
  exit 1
fi

# Read the current version from the version file
version=$(cat "$VERSION_FILE")
major=$(echo $version | cut -d. -f1)
minor=$(echo $version | cut -d. -f2)
patch=$(echo $version | cut -d. -f3)

# Increment the patch version
patch=$((patch + 1))

# Check if patch version has reached 10
if [ $patch -ge 10 ]; then
  patch=0
  minor=$((minor + 1))
  
  # Check if minor version has reached 10
  if [ $minor -ge 10 ]; then
    minor=0
    major=$((major + 1))
  fi
fi

# Update the version file with the new version
new_version="$major.$minor.$patch"
echo $new_version > "$VERSION_FILE"

# Update the version in pom.xml
if [ -f "$POM_FILE" ]; then
  # Use sed to specifically replace the version of the project
sed -i.bak "s/<project.version>[0-9]*\.[0-9]*\.[0-9]*<\/project.version>/<project.version>$new_version<\/project.version>/" "$POM_FILE"
  echo "Version in $POM_FILE updated to $new_version"
else
  echo "Error: $POM_FILE not found!"
fi


echo "Version updated to $new_version"

# Execute Maven clean package
if mvn clean package; then
  echo "Build generated successfully with version: $new_version"

# Add version.txt and pom.xml to the commit
git add "$VERSION_FILE" "$POM_FILE"

else
  echo "Build generation failed!"
  exit 1
fi

