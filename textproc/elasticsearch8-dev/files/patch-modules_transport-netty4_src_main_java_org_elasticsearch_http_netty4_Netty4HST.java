--- modules/transport-netty4/src/main/java/org/elasticsearch/http/netty4/Netty4HttpServerTransport.java.orig	2026-01-28 17:31:29 UTC
+++ modules/transport-netty4/src/main/java/org/elasticsearch/http/netty4/Netty4HttpServerTransport.java
@@ -190,7 +190,7 @@ public class Netty4HttpServerTransport extends Abstrac
 
             if (SETTING_HTTP_TCP_KEEP_ALIVE.get(settings)) {
                 // Netty logs a warning if it can't set the option, so try this only on supported platforms
-                if (IOUtils.LINUX || IOUtils.MAC_OS_X) {
+                if (IOUtils.LINUX || IOUtils.MAC_OS_X || IOUtils.FREEBSD) {
                     if (SETTING_HTTP_TCP_KEEP_IDLE.get(settings) >= 0) {
                         serverBootstrap.childOption(
                             NioChannelOption.of(NetUtils.getTcpKeepIdleSocketOption()),
