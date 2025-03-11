PORTNAME=	sqlite3
DISTVERSION=	${PYTHON_DISTVERSION}
PORTREVISION=	8
CATEGORIES=	databases python
MASTER_SITES=	PYTHON/ftp/python/${DISTVERSION}
PKGNAMEPREFIX=	${PYTHON_PKGNAMEPREFIX}
DISTNAME=	Python-${DISTVERSION}
DIST_SUBDIR=	python

MAINTAINER=	python@FreeBSD.org
COMMENT=	Standard Python binding to the SQLite3 library (Python ${PYTHON_VER})

LICENSE=	PSFL
LICENSE_FILE=	${WRKDIR}/Python-${DISTVERSION}/LICENSE

LIB_DEPENDS=	libsqlite3.so:databases/sqlite3

BUILD_DEPENDS+=	${UNZIP_CMD}:archivers/unzip \
		${PYTHON_PKGNAMEPREFIX}setuptools>=0:devel/py-setuptools@${PY_FLAVOR} \
		${PYTHON_PKGNAMEPREFIX}wheel>=0:devel/py-wheel@${PY_FLAVOR} \
		${PYTHON_PKGNAMEPREFIX}cython3>=3.0.6:lang/cython3@${PY_FLAVOR}

USES=		compiler:c11 python:2.7+ tar:xz
USE_PYTHON=	autoplist allflavors pep517

PORTSCOUT=	ignore:1

DISTINFO_FILE=	${OVERLAYS}/lang/python${PYTHON_SUFFIX}/distinfo
WRKSRC_SUBDIR=	Modules

PYDISTUTILS_INSTALLARGS+=	--install-lib ${PYTHON_LIBDIR}/lib-dynload

TESTING_UNSAFE=	ModuleNotFoundError: No module named '_sqlite3'

.include <bsd.port.pre.mk>

post-extract:
	@${CP} ${FILESDIR}/setup3.py ${WRKSRC}/setup.py
	@${SED} -e 's|PY_VERSION|${PYTHON_DISTVERSION}|' ${FILESDIR}/pyproject.toml.in >>  ${WRKSRC}/pyproject.toml
	${TOUCH} ${WRKSRC}/__init__.py

do-install:
	@${MKDIR} ${STAGEDIR}${PYTHONPREFIX_LIBDIR}/lib-dynload
	@cd ${INSTALL_WRKSRC} \
	    && ${UNZIP_CMD} -d ${STAGEDIR}${PYTHONPREFIX_LIBDIR}/lib-dynload ${BUILD_WRKSRC}/dist/${PORTNAME:C|[-_]+|_|g}-${DISTVERSION}*.whl
	@${PYTHON_CMD} -B ${PORTSDIR}/Mk/Scripts/strip_RECORD.py \
		${STAGEDIR}/${PYTHONPREFIX_LIBDIR}/lib-dynload/${PORTNAME:C|[-_]+|_|g}-${DISTVERSION}*.dist-info/RECORD | \
		${SED} -e 's|^|${PYTHONPREFIX_LIBDIR}/lib-dynload/|' >> ${_PYTHONPKGLIST}

post-install:
	@${STRIP_CMD} ${STAGEDIR}${PYTHON_LIBDIR}/lib-dynload/_sqlite3*.so

do-test:
	@${PYTHON_CMD} -c 'import sqlite3'  # minimal smoke test

.include <bsd.port.post.mk>
