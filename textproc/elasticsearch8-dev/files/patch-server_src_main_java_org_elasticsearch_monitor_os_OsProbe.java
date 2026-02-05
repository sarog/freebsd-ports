--- server/src/main/java/org/elasticsearch/monitor/os/OsProbe.java.orig	2026-01-28 17:31:29 UTC
+++ server/src/main/java/org/elasticsearch/monitor/os/OsProbe.java
@@ -227,7 +227,7 @@ public class OsProbe {
                 return null;
             }
         } else {
-            assert Constants.MAC_OS_X;
+            assert Constants.MAC_OS_X || Constants.FREE_BSD;
             if (getSystemLoadAverage == null) {
                 return null;
             }
