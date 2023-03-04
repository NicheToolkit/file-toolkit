## [file-Toolkit](https://github.com/NicheToolkit/file-toolkit) file开发工具组

[![GitHub License](https://img.shields.io/badge/license-Apache-blue.svg)](https://github.com/NicheToolkit/file-toolkit/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.nichetoolkit/file-toolkit-service-starter)](https://central.sonatype.com/search?smo=true&q=file-toolkit-service-starter&namespace=io.github.nichetoolkit)
[![Nexus Release](https://img.shields.io/nexus/r/io.github.nichetoolkit/file-toolkit-service-starter?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/releases/io/github/nichetoolkit/file-toolkit-service-starter/)
[![Nexus Snapshot](https://img.shields.io/nexus/s/io.github.nichetoolkit/file-toolkit-service-starter?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/nichetoolkit/file-toolkit-service-starter/)
![Tests](https://github.com/NicheToolkit/file-toolkit/workflows/Tests/badge.svg)

&emsp;&emsp; 依赖[rice-toolkit](https://github.com/NicheToolkit/rice-toolkit/rice-toolkit-starter)组件下的基于[postgresql](https://www.postgresql.org/)数据库环境下的简单业务通用服务开发组件.

## Release介绍

-  [File-Toolkit@1.0.3](https://github.com/NicheToolkit/file-toolkit/tree/master/release/1.0.3.md)

### v1.0.3 Release

1、修复`minio`依赖`okhttp3`版本为`4.8.1`。

2、升级`spring boot`版本至`2.6.6`版本。

3、优化文件上传异步处理，加快文件上传接口响应。

4、拆分`MinioUtils`工具至单独模块`file-toolkit-minio-utils`。

5、拆分文件模型定义相关`FileChunk`、`FileIndex`至单独模块`file-toolkit-common-starter`。

# [file-toolkit-test-web](https://github.com/NicheToolkit/file-toolkit/tree/master/file-toolkit-test-web)

[logback-spring.xml](https://github.com/NicheToolkit/file-toolkit/blob/master/file-toolkit-test-web/src/main/resources/logback-spring.xml)
默认日志配置文件调整，新增`LOGS_FILE`所有日志的到`./logs.log`文件的输出，方便同一个`Tomcat`服务器下部署多个`War`包时，查看单个服务的日志混乱的问题。

**Full Changelog**: https://github.com/NicheToolkit/file-toolkit/compare/v1.0.2...v1.0.3

## Maven Central

-  [Maven Central Repository](https://search.maven.org/search?q=io.github.nichetoolkit)

-  [Sonatype Central Repository](https://central.sonatype.dev/search?q=io.github.nichetoolkit)

## 依赖环境
 > [Spring Boot](https://spring.io/projects/spring-boot) 2.6.6.RELEASE\
 > [Maven](https://maven.apache.org/) 3.6.0+\
 > [JDK](https://www.oracle.com/java/technologies/downloads/#java8) 1.8\
 > [PostgreSQL](https://www.postgresql.org/) 10.0+
 
## file-toolkit-common-starter
 * Maven (`pom.xml`)
```xml
  <dependency>
    <groupId>io.github.nichetoolkit</groupId>
    <artifactId>file-toolkit-common-starter</artifactId>
    <version>1.0.3</version>
  </dependency>
``` 

## file-toolkit-service-starter
 * Maven (`pom.xml`)
```xml
  <dependency>
    <groupId>io.github.nichetoolkit</groupId>
    <artifactId>file-toolkit-service-starter</artifactId>
    <version>1.0.3</version>
  </dependency>
```

## file-toolkit-minio-starter
 * Maven (`pom.xml`)
```xml
  <dependency>
    <groupId>io.github.nichetoolkit</groupId>
    <artifactId>file-toolkit-minio-starter</artifactId>
    <version>1.0.3</version>
  </dependency>
```

## file-toolkit-minio-utils
 * Maven (`pom.xml`)
```xml
  <dependency>
    <groupId>io.github.nichetoolkit</groupId>
    <artifactId>file-toolkit-minio-utils</artifactId>
    <version>1.0.3</version>
  </dependency>
``` 

## 使用方式

参考[file-toolkit-test-web](https://github.com/NicheToolkit/file-toolkit/tree/master/file-toolkit-test-web)模块.

 ## 依赖参考

 [rice-toolkit](https://github.com/NicheToolkit/rice-toolkit)
 
 ## License 

 [Apache License](https://www.apache.org/licenses/LICENSE-2.0)
 
 ## Dependencies
 
 [rest-toolkit](https://github.com/NicheToolkit/rest-toolkit)
  
 [Spring Boot](https://github.com/spring-projects/spring-boot)