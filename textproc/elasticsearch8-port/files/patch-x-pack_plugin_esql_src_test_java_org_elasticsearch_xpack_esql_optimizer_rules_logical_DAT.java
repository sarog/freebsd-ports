--- x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/optimizer/rules/logical/DeduplicateAggsTests.java.orig	2026-08-05 21:29:08 UTC
+++ x-pack/plugin/esql/src/test/java/org/elasticsearch/xpack/esql/optimizer/rules/logical/DeduplicateAggsTests.java
@@ -8,6 +8,7 @@ import org.elasticsearch.common.lucene.BytesRefs;
 package org.elasticsearch.xpack.esql.optimizer.rules.logical;
 
 import org.elasticsearch.common.lucene.BytesRefs;
+import org.elasticsearch.xpack.esql.action.EsqlCapabilities;
 import org.elasticsearch.xpack.esql.core.expression.Alias;
 import org.elasticsearch.xpack.esql.core.expression.Expression;
 import org.elasticsearch.xpack.esql.core.expression.Expressions;
@@ -581,6 +582,9 @@ public class DeduplicateAggsTests extends AbstractLogi
      * pe{f}#15]]
      */
     public void testDuplicatedInlineAggWithFoldableIdenticalExpressions() {
+        // https://github.com/elastic/elasticsearch/pull/140070/
+        assumeTrue("requires INLINESTATS command to be enabled", EsqlCapabilities.Cap.INLINESTATS.isEnabled());
+
         String query = """
                 FROM airports
                 | INLINESTATS a = 2*COUNT_DISTINCT(scalerank, 100),
