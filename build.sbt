

import sbt._
import Keys._
import Dependencies._
import Settings._
import com.typesafe.config._
name := "bbs_backend"
version := "1.0"
description := "https://tool.devsep.com/wiki/pages/viewpage.action?pageId=98153563"
organization := "septeni-technology"
unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
lazy val `bbs_backend` = (project in file(".")).enablePlugins(PlayScala)
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .aggregate(application, domain, port, utility)
  .dependsOn(application, domain, port, utility)
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(filters, guice))
  .settings(routesGenerator := InjectedRoutesGenerator)
lazy val application = (project in file("app/application"))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(utility , domain)
  .settings(commonSettings: _*)
lazy val domain = (project in file("app/domain"))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(utility)
  .settings(commonSettings: _*)
lazy val utility = (project in file("app/utility"))
  .settings(commonSettings: _*)
lazy val port = (project in file("app/port"))
  .aggregate(portWebService, portDatabase, portHttp)
  .dependsOn(portWebService, portDatabase, portHttp)
  .settings(commonSettings: _*)
lazy val portWebService = (project in file("app/port/primary/webService"))
  .enablePlugins(PlayScala, SbtWeb)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(utility, application)
  .settings(commonSettings: _*)
lazy val portDatabase = (project in file("app/port/secondary/databaseMySQL"))
  .dependsOn(utility, application)
  .settings(commonSettings: _*)
lazy val portHttp = (project in file("app/port/secondary/http"))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(utility, application)
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(ws))
lazy val config = ConfigFactory.parseFile(file("conf/application.conf")).resolve()