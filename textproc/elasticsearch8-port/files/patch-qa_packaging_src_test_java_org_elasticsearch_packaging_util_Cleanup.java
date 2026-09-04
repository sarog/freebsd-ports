--- qa/packaging/src/test/java/org/elasticsearch/packaging/util/Cleanup.java.orig	2026-08-05 21:29:08 UTC
+++ qa/packaging/src/test/java/org/elasticsearch/packaging/util/Cleanup.java
@@ -44,6 +44,19 @@ public class Cleanup {
         "/usr/lib/sysctl.d/elasticsearch.conf"
     );
 
+    private static final List<String> ELASTICSEARCH_FILES_FREEBSD = Arrays.asList(
+        "/usr/local/share/elasticsearch",
+        "/usr/local/etc/elasticsearch/elasticsearch.keystore",
+        "/usr/local/etc/elasticsearch",
+        "/usr/local/lib/elasticsearch",
+        "/usr/local/libexec/elasticsearch",
+        "/var/log/elasticsearch",
+        "/var/run/elasticsearch",
+        "/var/db/elasticsearch",
+        "/usr/local/share/doc/elasticsearch",
+        "/usr/local/etc/rc.d/elasticsearch"
+    );
+
     // todo
     private static final List<String> ELASTICSEARCH_FILES_WINDOWS = Collections.emptyList();
 
@@ -56,6 +69,11 @@ public class Cleanup {
             sh.runIgnoreExitCode("ps aux | grep -i 'org.elasticsearch.bootstrap.Elasticsearch' | awk {'print $2'} | xargs kill -9");
         });
 
+        Platforms.onFreeBSD(() -> {
+            sh.runIgnoreExitCode("pkill -u elasticsearch");
+            sh.runIgnoreExitCode("ps aux | grep -i 'org.elasticsearch.bootstrap.Elasticsearch' | awk {'print $2'} | xargs kill -9");
+        });
+
         Platforms.onWindows(() -> {
             // the view of processes returned by Get-Process doesn't expose command line arguments, so we use WMI here
             sh.runIgnoreExitCode(
@@ -66,17 +84,24 @@ public class Cleanup {
         });
 
         Platforms.onLinux(Cleanup::purgePackagesLinux);
+        Platforms.onFreeBSD(Cleanup::purgePackagesFreeBSD);
 
         // remove elasticsearch users
         Platforms.onLinux(() -> {
             sh.runIgnoreExitCode("userdel elasticsearch");
             sh.runIgnoreExitCode("groupdel elasticsearch");
         });
+        Platforms.onFreeBSD(() -> {
+            sh.runIgnoreExitCode("pw user del elasticsearch");
+            sh.runIgnoreExitCode("pw group del elasticsearch");
+        });
         // when we run es as a role user on windows, add the equivalent here
         // delete files that may still exist
 
         lsGlob(getRootTempDir(), "elasticsearch*").forEach(Platforms.WINDOWS ? FileUtils::rmWithRetries : FileUtils::rm);
-        final List<String> filesToDelete = Platforms.WINDOWS ? ELASTICSEARCH_FILES_WINDOWS : ELASTICSEARCH_FILES_LINUX;
+        final List<String> filesToDelete = Platforms.WINDOWS
+            ? ELASTICSEARCH_FILES_WINDOWS
+            : (Platforms.FREEBSD ? ELASTICSEARCH_FILES_FREEBSD : ELASTICSEARCH_FILES_LINUX);
         // windows needs leniency due to asinine releasing of file locking async from a process exiting
         Consumer<? super Path> rm = Platforms.WINDOWS ? FileUtils::rmWithRetries : FileUtils::rm;
         filesToDelete.stream().map(Paths::get).filter(Files::exists).forEach(rm);
@@ -95,5 +120,10 @@ public class Cleanup {
         if (isDPKG()) {
             sh.runIgnoreExitCode("dpkg --purge elasticsearch elasticsearch-oss");
         }
+    }
+
+    private static void purgePackagesFreeBSD() {
+        final Shell sh = new Shell();
+        sh.runIgnoreExitCode("pkg delete -y elasticsearch");
     }
 }
