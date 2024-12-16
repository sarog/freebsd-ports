--- cmake/FindFastFloat.cmake.orig	2023-12-06 23:14:06 UTC
+++ cmake/FindFastFloat.cmake
@@ -2,4 +2,4 @@ target_include_directories(FastFloat::fast_float
 
 target_include_directories(FastFloat::fast_float
     INTERFACE
-        ${CMAKE_CURRENT_LIST_DIR}/../third-party/fast_float/include)
+    	${CMAKE_INSTALL_PREFIX}/include)
