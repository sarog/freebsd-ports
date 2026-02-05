--- server/src/main/java/org/elasticsearch/bootstrap/BootstrapChecks.java.orig	2026-01-28 17:31:29 UTC
+++ server/src/main/java/org/elasticsearch/bootstrap/BootstrapChecks.java
@@ -207,10 +207,10 @@ final class BootstrapChecks {
         if (Constants.LINUX) {
             checks.add(new MaxNumberOfThreadsCheck());
         }
-        if (Constants.LINUX || Constants.MAC_OS_X) {
+        if (Constants.LINUX || Constants.MAC_OS_X || Constants.FREE_BSD) {
             checks.add(new MaxSizeVirtualMemoryCheck());
         }
-        if (Constants.LINUX || Constants.MAC_OS_X) {
+        if (Constants.LINUX || Constants.MAC_OS_X || Constants.FREE_BSD) {
             checks.add(new MaxFileSizeCheck());
         }
         if (Constants.LINUX) {
