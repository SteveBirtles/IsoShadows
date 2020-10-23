#!/bin/bash
PATH_TO_FX=/usr/share/openjfx/lib
javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.graphics,javafx.fxml Main.java
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.graphics,javafx.fxml Main
