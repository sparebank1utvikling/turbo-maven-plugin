#!/bin/bash
declare extraMavenProfiles=""
declare extraMavenArguments=""
declare mavenGoals="clean install"
declare threadMode="-T4"

_echoUsage() {
    echo "Arguments: (bold is default)"
    echo "  -s or --singlethread: Default is multithreaded builds"
    echo "  -p or --profiles: e.g.: verify,source"
    echo "  -a or --arguments: e.g.: -DskipTests"
    echo "  -g or --goal: e.g.: 'clean install'"
    echo "  -h or --help: Print this information"
    echo ""
    echo "Example 1: build.sh"
    echo "Example 2: build.sh -p source,verify -a '-DskipTests -rf :mymodule'"
    echo "Example 3: build.sh --profiles verify,source --arguments '-DskipTests -rf :mymodule' --goal 'clean install'"
}

# Parse input parameters into a easy-to-process-format
TEMP=`getopt -o shp:a:g: --long singlethread,help,profiles:,arguments:,goal: -n 'build.sh' -- "$@"`
if [ "$?" == "1" ]; then
    _echoUsage;
    exit 1;
fi
eval set -- "$TEMP"

# Check that possible input params have the correct value:
while true ; do
    case "$1" in
        -h|--help)
            _echoUsage; exit 0 ;;

        -s|--singlethread)
            threadMode=""; shift 1 ;;

        -p|--profiles)
            case "$2" in
                "") echo "Maven profiles: No argument"; _echoUsage ; exit 1 ;;
                *)  extraMavenProfiles="-P$2" ; shift 2 ;;
            esac ;;

        -a|--arguments)
            case "$2" in
                "") echo "Extra arguments: No argument"; _echoUsage ; exit 1 ;;
                *)  extraMavenArguments="$2" ; shift 2 ;;
            esac ;;

        -g|--goal)
            case "$2" in
                "") echo "Maven goal: No argument"; _echoUsage ; exit 1 ;;
                *)  mavenGoals="$2" ; shift 2 ;;
            esac ;;

        --)
            # Exit block
            if [ "${TEMP:(-2)}" != "--" ]; then
                echo "Unknown parameter in command: $2"
                _echoUsage; exit 1
            fi
            shift ; break ;;
        *) echo "Internal error!" ; exit 1 ;;
    esac
done
#echo "Remaining arguments:"
#for arg do echo '--> '"\`$arg'" ; done

# We are ok with the input params, so carry on.

set -e

mvn ${mavenGoals} ${threadMode} ${extraMavenProfiles} ${extraMavenArguments}
