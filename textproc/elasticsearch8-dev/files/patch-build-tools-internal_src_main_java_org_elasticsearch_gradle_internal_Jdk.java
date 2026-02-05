--- build-tools-internal/src/main/java/org/elasticsearch/gradle/internal/Jdk.java.orig	2026-01-28 17:31:29 UTC
+++ build-tools-internal/src/main/java/org/elasticsearch/gradle/internal/Jdk.java
@@ -26,7 +26,7 @@ public class Jdk implements Buildable, Iterable<File> 
 
     private static final List<String> ALLOWED_ARCHITECTURES = List.of("aarch64", "x64");
     private static final List<String> ALLOWED_VENDORS = List.of("adoptium", "openjdk", "zulu");
-    private static final List<String> ALLOWED_PLATFORMS = List.of("darwin", "linux", "windows", "mac");
+    private static final List<String> ALLOWED_PLATFORMS = List.of("darwin", "linux", "windows", "mac", "freebsd");
     private static final Pattern VERSION_PATTERN = Pattern.compile(
         "(\\d+)(\\.\\d+\\.\\d+(?:\\.\\d+)?)?\\+(\\d+(?:\\.\\d+)?)(@([a-f0-9]{32}))?"
     );
