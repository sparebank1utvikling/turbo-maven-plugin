#!/bin/bash
set -e

declare -r bold=$(tput bold)
declare -r normal=$(tput sgr0)

# Clean out any previously generated files (from templates, build files etc) which are not under version control.
git clean -d -f -x


#######################
###### VERSION ########
#######################

currentVersion=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)')
# Removing existing qualifier from current version
currentVersionWithoutQualifier=$(echo ${currentVersion} | sed "s/-.*//")

# Create the Maven qualifier. This is always treated as a String, and thus we must zero-pad the build number to
# make sure Maven is able to give us the newest build if we decide to use Mavens version ranges.
# We also strip off the "origin/"-part of the branch name.
qualifier=${GIT_BRANCH#origin/}_$(printf "%04d" ${BUILD_NUMBER})
# Removing "-" from qualifier
qualifierNoHyphen=${qualifier//-/''}

newVersionNumber=${currentVersionWithoutQualifier}-${qualifierNoHyphen}

#######################
###### VERSION END ####
#######################


#######################
###### BUILD ##########
#######################

echo -e "${bold}Build version ${newVersionNumber} is on its way${normal}"

echo -e "${bold}Setting new version number to ${newVersionNumber}${normal}"
mvn versions:set -DnewVersion=${newVersionNumber}

echo -e "${bold}Building the new version ${newVersionNumber}${normal}"
# TODO: Re-add tests
#./build.sh --profiles verify,integration,!source "$@"
./build.sh --profiles verify,!source "$@"

#######################
#### BUILD END ########
#######################
