--- libs/native/src/main21/java/org/elasticsearch/nativeaccess/jdk/JdkNativeLibraryProvider.java.orig	2026-01-28 17:31:29 UTC
+++ libs/native/src/main21/java/org/elasticsearch/nativeaccess/jdk/JdkNativeLibraryProvider.java
@@ -9,6 +9,7 @@ package org.elasticsearch.nativeaccess.jdk;
 
 package org.elasticsearch.nativeaccess.jdk;
 
+import org.elasticsearch.nativeaccess.lib.BsdCLibrary;
 import org.elasticsearch.nativeaccess.lib.JavaLibrary;
 import org.elasticsearch.nativeaccess.lib.Kernel32Library;
 import org.elasticsearch.nativeaccess.lib.LinuxCLibrary;
@@ -39,7 +40,9 @@ public class JdkNativeLibraryProvider extends NativeLi
                 ZstdLibrary.class,
                 JdkZstdLibrary::new,
                 VectorLibrary.class,
-                JdkVectorLibrary::new
+                JdkVectorLibrary::new,
+                BsdCLibrary.class,
+                JdkFreebsdCLibrary::new
             )
         );
     }
