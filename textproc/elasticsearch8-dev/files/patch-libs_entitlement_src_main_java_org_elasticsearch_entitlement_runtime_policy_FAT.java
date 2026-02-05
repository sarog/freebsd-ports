--- libs/entitlement/src/main/java/org/elasticsearch/entitlement/runtime/policy/FileAccessTree.java.orig	2026-01-28 17:31:29 UTC
+++ libs/entitlement/src/main/java/org/elasticsearch/entitlement/runtime/policy/FileAccessTree.java
@@ -181,7 +181,7 @@ public final class FileAccessTree {
 
     private static final Logger logger = LogManager.getLogger(FileAccessTree.class);
     private static final String FILE_SEPARATOR = getDefaultFileSystem().getSeparator();
-    static final FileAccessTreeComparison DEFAULT_COMPARISON = Platform.LINUX.isCurrent()
+    static final FileAccessTreeComparison DEFAULT_COMPARISON = (Platform.LINUX.isCurrent() || Platform.FREEBSD.isCurrent())
         ? new CaseSensitiveComparison(separatorChar())
         : new CaseInsensitiveComparison(separatorChar());
 
