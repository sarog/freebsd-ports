--- x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/analysis/AnalyzerTests.java.orig	2026-08-05 21:29:08 UTC
+++ x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/analysis/AnalyzerTests.java
@@ -2365,6 +2365,8 @@ public class AnalyzerTests extends ESTestCase {
     }
 
     public void testDenseVectorImplicitCasting() {
+        // https://github.com/elastic/elasticsearch/blob/9.1/x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/analysis/AnalyzerTests.java#L2376C1-L2376C119
+        assumeTrue("dense_vector capability not available", EsqlCapabilities.Cap.DENSE_VECTOR_FIELD_TYPE.isEnabled());
         Analyzer analyzer = analyzer(loadMapping("mapping-dense_vector.json", "vectors"));
 
         var plan = analyze("""
