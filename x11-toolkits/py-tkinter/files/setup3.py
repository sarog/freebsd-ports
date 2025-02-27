#!/usr/bin/env python
# To use:
#       python setup.py install
#

import platform
import sysconfig
import os

try:
    from setuptools import Extension, setup
except Exception as e:
    raise SystemExit("Setuptools problem", e)


tkversion = "%%TK_VER%%"
prefix = sysconfig.get_config_var('base')
x11base = os.environ['LOCALBASE'] or '/usr/X11R6'
inc_dirs = [prefix + "/include",
            prefix + "/include/tcl" + tkversion,
            prefix + "/include/tk" + tkversion,
            "../Include/internal",
            "Modules",
            x11base + "/include"]
lib_dirs = [prefix + "/lib", x11base + "/lib"]
libs = ["tcl" + tkversion.replace(".", ""),
        "tk" + tkversion.replace(".", ""),
        "X11"]
#libs = ["tkinter"]
macros = [('WITH_APPINIT', 1)]
ext_srcs = [
    '_tkinter.c',
    'tkappinit.c'
]

major, minor = map(int, platform.python_version_tuple()[:2])

setup(
    ext_modules=[
        Extension(
            "_tkinter",
            sources = ext_srcs,
            include_dirs = inc_dirs,
            libraries = libs,
            runtime_library_dirs = lib_dirs,
            define_macros = macros
        )
    ]
)
