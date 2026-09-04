--- server/src/main/java/org/elasticsearch/monitor/os/OsProbe.java.orig	2026-08-05 21:29:08 UTC
+++ server/src/main/java/org/elasticsearch/monitor/os/OsProbe.java
@@ -229,7 +229,7 @@ public class OsProbe {
                 return null;
             }
         } else {
-            assert Constants.MAC_OS_X;
+            assert Constants.MAC_OS_X || Constants.FREE_BSD;
             if (getSystemLoadAverage == null) {
                 return null;
             }
