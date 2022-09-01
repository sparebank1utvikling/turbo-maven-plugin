#!/bin/bash -ev

VERSION='1.0.0'

mvn deploy \
    -Prelease \
    -s ./settings.xml \
    -Drevision="$VERSION" \
    -Dsonatype.username="$SONATYPE_USERNAME" \
    -Dsonatype.password="$SONATYPE_PASSWORD" \
    -Dgpg.passphrase="$GPG_KEY_PASSPHRASE"