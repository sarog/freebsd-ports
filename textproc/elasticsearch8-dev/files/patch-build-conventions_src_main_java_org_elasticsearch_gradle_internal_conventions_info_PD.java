--- build-conventions/src/main/java/org/elasticsearch/gradle/internal/conventions/info/ParallelDetector.java.orig	2026-01-28 17:31:29 UTC
+++ build-conventions/src/main/java/org/elasticsearch/gradle/internal/conventions/info/ParallelDetector.java
@@ -80,6 +80,14 @@ public class ParallelDetector {
 
 
                 _defaultParallel = Integer.parseInt(stdout.trim());
+            } else if (isFreeBSD(project.getProviders())) {
+                String stdout = project.getProviders()
+                    .exec(execSpec -> execSpec.commandLine("sysctl", "-n", "kern.smp.cores"))
+                    .getStandardOutput()
+                    .getAsText()
+                    .get();
+
+                _defaultParallel = Integer.parseInt(stdout.trim());
             }
 
             if (_defaultParallel == null || _defaultParallel < 1) {
@@ -93,6 +101,10 @@ public class ParallelDetector {
 
     private static boolean isMac(ProviderFactory providers) {
         return providers.systemProperty("os.name").getOrElse("").startsWith("Mac");
+    }
+
+    private static boolean isFreeBSD(ProviderFactory providers) {
+        return providers.systemProperty("os.name").getOrElse("").startsWith("FreeBSD");
     }
 
     private static boolean isMontereyOrNewer(ProviderFactory providers) {
