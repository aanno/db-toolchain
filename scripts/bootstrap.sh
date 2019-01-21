#!/bin/bash

ROOT=`git rev-parse --show-toplevel`

pushd "$ROOT"

    ./scripts/download-needed.sh

    git submodule init
    git submodule update

    pushd submodules

        pushd xmlcalabash1-print
            git remote add upstream git@github.com:ndw/xmlcalabash1-print.git
            git checkout feature/pr-java11-module
        popd

        pushd asciidoctorj
            git remote add upstream git@github.com:asciidoctor/asciidoctorj.git
            git checkout feature/pr-java11-module-jars
            ../../gradlew -x test clean build
        popd

        pushd xslt20-stylesheets
            touch settings.gradle.kts
            ../../gradlew -x test clean build
        popd

        pushd xslt20-resources
            touch settings.gradle.kts
            ../../gradlew -x test clean build
        popd

        pushd jing-trang
            git remote add upstream git@github.com:relaxng/jing-trang.git
            touch settings.gradle.kts
            ../../gradlew -x test clean build
        popd

        pushd fop
            git remote add upstream git@github.com:apache/fop.git
            git checkout feature/pr-remove-avalon-1
            mvn -DskipTests clean package
        popd

        pushd batik
            git remote add upstream git@github.com:apache/batik.git
            git checkout feature/pr-batik-1249-1
            mvn -DskipTests clean package
        popd

    popd

    git submodule foreach git gc

    # HACK to get empty jar
    mkdir -p "lib/ueberjars"
    mkdir -p "lib/tmp"
    touch "lib/ueberjars/jnrchannels.jar"

    ./gradlew copyJarsForUeberJars
    ./scripts/merge-split-jars.sh

    ./gradlew build

popd
