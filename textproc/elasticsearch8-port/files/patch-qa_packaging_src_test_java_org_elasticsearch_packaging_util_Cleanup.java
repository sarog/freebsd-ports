--- qa/packaging/src/test/java/org/elasticsearch/packaging/util/Cleanup.java.orig	2026-08-26 20:47:40 UTC
+++ qa/packaging/src/test/java/org/elasticsearch/packaging/util/Cleanup.java
@@ -42,6 +42,19 @@ public class Cleanup {
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
 
@@ -54,6 +67,11 @@ public class Cleanup {
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
@@ -64,17 +82,24 @@ public class Cleanup {
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
 
         lsGlob(getRootTempDir(), "elasticsearch*").forEach(FileUtils::rm);
-        final List<String> filesToDelete = Platforms.WINDOWS ? ELASTICSEARCH_FILES_WINDOWS : ELASTICSEARCH_FILES_LINUX;
+        final List<String> filesToDelete = Platforms.WINDOWS
+            ? ELASTICSEARCH_FILES_WINDOWS
+            : (Platforms.FREEBSD ? ELASTICSEARCH_FILES_FREEBSD : ELASTICSEARCH_FILES_LINUX);
         filesToDelete.stream().map(Paths::get).filter(Files::exists).forEach(FileUtils::rm);
     }
 
@@ -91,5 +116,10 @@ public class Cleanup {
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
