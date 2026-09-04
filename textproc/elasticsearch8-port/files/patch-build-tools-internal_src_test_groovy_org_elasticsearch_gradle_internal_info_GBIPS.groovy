--- build-tools-internal/src/test/groovy/org/elasticsearch/gradle/internal/info/GlobalBuildInfoPluginSpec.groovy.orig	2026-08-05 21:29:08 UTC
+++ build-tools-internal/src/test/groovy/org/elasticsearch/gradle/internal/info/GlobalBuildInfoPluginSpec.groovy
@@ -13,14 +13,21 @@ import spock.lang.TempDir
 import spock.lang.Specification
 import spock.lang.TempDir
 
+import com.sun.net.httpserver.HttpHandler
+import com.sun.net.httpserver.HttpServer
 import org.elasticsearch.gradle.Version
 import org.elasticsearch.gradle.internal.BwcVersions
+import org.elasticsearch.gradle.internal.util.HttpUtils
 import org.gradle.api.Project
 import org.gradle.api.provider.Provider
 import org.gradle.api.provider.ProviderFactory
 import org.gradle.testfixtures.ProjectBuilder
 
+import java.net.InetAddress
+import java.net.InetSocketAddress
+import java.nio.charset.StandardCharsets
 import java.nio.file.Path
+import java.util.concurrent.atomic.AtomicInteger
 
 class GlobalBuildInfoPluginSpec extends Specification {
 
@@ -95,6 +102,53 @@ class GlobalBuildInfoPluginSpec extends Specification 
         then:
         bwcVersions != null
         bwcVersions.unreleased.toSet() == ["9.1.0", "9.0.3", "8.19.1", "8.18.2"].collect { Version.fromString(it) }.toSet()
+    }
+
+    def "offline mode falls back to workspace branches.json when configured location is an http(s) URL"() {
+        given:
+        project.getGradle().getStartParameter().setOffline(true)
+
+        ProviderFactory providerFactorySpy = Spy(project.getProviders())
+        Provider<String> gradleBranchesLocationProvider = project.providers.provider { return "https://example.invalid/branches.json" }
+        providerFactorySpy.gradleProperty("org.elasticsearch.build.branches-file-location") >> gradleBranchesLocationProvider
+        project.getProviders() >> providerFactorySpy
+
+        Path workspaceBranchesJsonPath = projectRoot.toPath().resolve("branches.json")
+        workspaceBranchesJsonPath.text = branchesJson(
+            [
+                new DevelopmentBranch("main", Version.fromString("9.1.0")),
+                new DevelopmentBranch("9.0", Version.fromString("9.0.3")),
+            ]
+        )
+
+        when:
+        project.objects.newInstance(GlobalBuildInfoPlugin).apply(project)
+        BuildParameterExtension ext = project.extensions.getByType(BuildParameterExtension)
+        BwcVersions bwcVersions = ext.bwcVersions
+
+        then:
+        bwcVersions != null
+        bwcVersions.unreleased.toSet() == ["9.1.0", "9.0.3"].collect { Version.fromString(it) }.toSet()
+    }
+
+    def "offline mode fails with clear error when configured location is an http(s) URL and workspace branches.json is missing"() {
+        given:
+        project.getGradle().getStartParameter().setOffline(true)
+
+        ProviderFactory providerFactorySpy = Spy(project.getProviders())
+        Provider<String> gradleBranchesLocationProvider = project.providers.provider { return "https://example.invalid/branches.json" }
+        providerFactorySpy.gradleProperty("org.elasticsearch.build.branches-file-location") >> gradleBranchesLocationProvider
+        project.getProviders() >> providerFactorySpy
+
+        when:
+        project.objects.newInstance(GlobalBuildInfoPlugin).apply(project)
+        project.extensions.getByType(BuildParameterExtension).bwcVersions
+
+        then:
+        def ex = thrown(org.gradle.api.GradleException)
+        ex.message.contains("offline mode")
+        ex.message.contains("branches.json")
+        ex.message.contains("org.elasticsearch.build.branches-file-location")
     }
 
     String branchesJson(List<DevelopmentBranch> branches) {
