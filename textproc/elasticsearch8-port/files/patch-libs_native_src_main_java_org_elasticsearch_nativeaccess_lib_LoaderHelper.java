--- libs/native/src/main/java/org/elasticsearch/nativeaccess/lib/LoaderHelper.java.orig	2026-08-05 21:29:08 UTC
+++ libs/native/src/main/java/org/elasticsearch/nativeaccess/lib/LoaderHelper.java
@@ -46,6 +46,8 @@ public class LoaderHelper {
             os = "linux";
         } else if (osname.startsWith("Mac OS")) {
             os = "darwin";
+        } else if (osname.startsWith("FreeBSD")) {
+            os = "freebsd";
         } else {
             os = "unsupported_os[" + osname + "]";
         }
