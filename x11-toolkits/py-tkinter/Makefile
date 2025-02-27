PORTNAME=	tkinter
DISTVERSION=	${PYTHON_DISTVERSION}
PORTREVISION=	8
CATEGORIES=	x11-toolkits python
MASTER_SITES=	PYTHON/ftp/python/${DISTVERSION}
PKGNAMEPREFIX=	${PYTHON_PKGNAMEPREFIX}
DISTNAME=	Python-${DISTVERSION}
DIST_SUBDIR=	python

MAINTAINER=	python@FreeBSD.org
COMMENT=	Python bindings to the Tk widget set (Python ${PYTHON_VER})

LICENSE=	PSFL

PORTSCOUT=	ignore:1

USES=		compiler:c11 python:2.7+ tar:xz tk
USE_PYTHON=	autoplist allflavors

.if ${FLAVOR:U} == py27
USE_PYTHON+=	distutils
.else
USE_PYTHON+=	cython pep517
BUILD_DEPENDS+=	${UNZIP_CMD}:archivers/unzip \
		${PYTHON_PKGNAMEPREFIX}setuptools>=0:devel/py-setuptools@${PY_FLAVOR} \
		${PYTHON_PKGNAMEPREFIX}wheel>=0:devel/py-wheel@${PY_FLAVOR} \
		${PYTHON_PKGNAMEPREFIX}cython3>=3.0.6:lang/cython3@${PY_FLAVOR}
.endif

DISTINFO_FILE=	${OVERLAYS}/lang/python${PYTHON_SUFFIX}/distinfo
WRKSRC_SUBDIR=	Modules

PYDISTUTILS_INSTALLARGS+=	--install-lib ${PYTHON_LIBDIR}/lib-dynload

.include <bsd.port.pre.mk>

post-extract:
.if ${PYTHON_REL} < 30000
	@${SED} -e "s|%%TK_VER%%|${TK_VER}|" ${FILESDIR}/setup.py > ${WRKSRC}/setup.py
.else
	@${SED} -e "s|%%TK_VER%%|${TK_VER}|" ${FILESDIR}/setup3.py > ${WRKSRC}/setup.py
	@${SED} -e 's|TK_VERSION|${TK_VER}|' ${FILESDIR}/pyproject.toml.in >>  ${WRKSRC}/pyproject.toml
	${TOUCH} ${WRKSRC}/__init__.py
.endif

.if ${FLAVOR:U} != py27
do-install:
	@${MKDIR} ${STAGEDIR}${PYTHONPREFIX_LIBDIR}/lib-dynload
	@cd ${INSTALL_WRKSRC} \
	    && ${UNZIP_CMD} -d ${STAGEDIR}${PYTHONPREFIX_LIBDIR}/lib-dynload ${BUILD_WRKSRC}/dist/${PORTNAME:C|[-_]+|_|g}-${DISTVERSION}*.whl
	@${PYTHON_CMD} -B ${PORTSDIR}/Mk/Scripts/strip_RECORD.py \
		${STAGEDIR}/${PYTHONPREFIX_LIBDIR}/lib-dynload/${PORTNAME:C|[-_]+|_|g}-${DISTVERSION}*.dist-info/RECORD | \
		${SED} -e 's|^|${PYTHONPREFIX_LIBDIR}/lib-dynload/|' >> ${_PYTHONPKGLIST}
.endif

post-install:
#	${STRIP_CMD} ${STAGEDIR}${PYTHON_LIBDIR}/lib-dynload/_tkinter.cpython-${PYTHON_SUFFIX}.so
	${STRIP_CMD} ${STAGEDIR}${PYTHON_LIBDIR}/lib-dynload/_tkinter*.so

.include <bsd.port.post.mk>
