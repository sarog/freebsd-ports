--- x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/parser/StatementParserTests.java.orig	2026-08-05 21:29:08 UTC
+++ x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/parser/StatementParserTests.java
@@ -3251,7 +3251,8 @@ public class StatementParserTests extends AbstractStat
     }
 
     public void testInvalidFromPatterns() {
-        var sourceCommands = new String[] { "FROM", "METRICS" };
+        // https://github.com/elastic/elasticsearch/pull/130458
+        var sourceCommands = Build.current().isSnapshot() ? new String[] { "FROM", "METRICS" } : new String[] { "FROM" };
         var indexIsBlank = "Blank index specified in index pattern";
         var remoteIsEmpty = "remote part is empty";
         var invalidDoubleColonUsage = "invalid usage of :: separator";
