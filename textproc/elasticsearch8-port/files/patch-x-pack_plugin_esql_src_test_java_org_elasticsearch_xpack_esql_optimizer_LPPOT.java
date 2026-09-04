--- x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/optimizer/LocalPhysicalPlanOptimizerTests.java.orig	2026-08-05 21:29:08 UTC
+++ x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/optimizer/LocalPhysicalPlanOptimizerTests.java
@@ -1263,6 +1263,10 @@ public class LocalPhysicalPlanOptimizerTests extends M
     }
 
     public void testKnnOptionsPushDown() {
+        // https://github.com/elastic/elasticsearch/blob/9.1/x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/optimizer/LocalPhysicalPlanOptimizerTests.java#L1365
+        assumeTrue("dense_vector capability not available", EsqlCapabilities.Cap.DENSE_VECTOR_FIELD_TYPE.isEnabled());
+        assumeTrue("knn capability not available", EsqlCapabilities.Cap.KNN_FUNCTION.isEnabled());
+
         String query = """
             from test
             | where KNN(dense_vector, [0.1, 0.2, 0.3],
