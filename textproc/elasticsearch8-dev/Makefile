PORTNAME=	elasticsearch
DISTVERSIONPREFIX=	v
DISTVERSION=	8.19.11
CATEGORIES=	textproc java devel
PKGNAMESUFFIX=	8

MAINTAINER=	elastic@FreeBSD.org
COMMENT=	Distributed, RESTful search and analytics engine
WWW=		https://www.elastic.co/

LICENSE=	APACHE20 AGPLv3 SSPLv1 EL20
LICENSE_COMB=	multi
LICENSE_NAME_SSPLv1=	Server Side Public License v1
LICENSE_NAME_EL20=	Elastic License 2.0
LICENSE_FILE_SSPLv1=	${WRKSRC}/licenses/AGPL-3.0+SSPL-1.0+ELASTIC-LICENSE-2.0.txt
LICENSE_FILE_EL20=	${WRKSRC}/licenses/ELASTIC-LICENSE-2.0.txt
LICENSE_FILE=	${WRKSRC}/LICENSE.txt
LICENSE_PERMS_SSPLv1=	dist-mirror dist-sell pkg-mirror pkg-sell auto-accept
LICENSE_PERMS_EL20=	dist-mirror dist-sell pkg-mirror pkg-sell auto-accept

ONLY_FOR_ARCHS=	amd64

BUILD_DEPENDS=	bash:shells/bash \
				jna>0:devel/jna \
				gradle>=9.2.1:devel/gradle \
				${LOCALBASE}/openjdk17/bin/java:java/openjdk17 \
				${LOCALBASE}/openjdk19/bin/java:java/openjdk19 \
				${LOCALBASE}/openjdk20/bin/java:java/openjdk20 \
				${LOCALBASE}/openjdk21/bin/java:java/openjdk21 \
				${LOCALBASE}/openjdk22/bin/java:java/openjdk22 \
				${LOCALBASE}/openjdk23/bin/java:java/openjdk23 \
				${LOCALBASE}/openjdk25/bin/java:java/openjdk25
RUN_DEPENDS=	bash:shells/bash \
				jna>0:devel/jna \
				java:java/openjdk21

USES=		cpe java shebangfix
JAVA_VERSION=	21+
JAVA_OS=	native
JAVA_VENDOR=	openjdk
USE_RC_SUBR=	${PORTNAME}

CPE_VENDOR=	elastic

USE_GITHUB= yes
GH_ACCOUNT= elastic
GH_PROJECT= elasticsearch

SHEBANG_FILES=	distribution/src/bin/elasticsearch \
		distribution/src/bin/elasticsearch-cli \
		distribution/src/bin/elasticsearch-env \
		distribution/src/bin/elasticsearch-env-from-file \
		distribution/src/bin/elasticsearch-geoip \
		distribution/src/bin/elasticsearch-keystore \
		distribution/src/bin/elasticsearch-node \
		distribution/src/bin/elasticsearch-plugin \
		distribution/src/bin/elasticsearch-shard \
		x-pack/plugin/security/src/main/bin/elasticsearch-certgen \
		x-pack/plugin/security/src/main/bin/elasticsearch-certutil \
		x-pack/plugin/security/src/main/bin/elasticsearch-create-enrollment-token \
		x-pack/plugin/security/src/main/bin/elasticsearch-reconfigure-node \
		x-pack/plugin/security/src/main/bin/elasticsearch-reset-password \
		x-pack/plugin/security/src/main/bin/elasticsearch-saml-metadata \
		x-pack/plugin/security/src/main/bin/elasticsearch-service-tokens \
		x-pack/plugin/security/src/main/bin/elasticsearch-setup-passwords \
		x-pack/plugin/security/src/main/bin/elasticsearch-syskeygen \
		x-pack/plugin/security/src/main/bin/elasticsearch-users \
		x-pack/plugin/sql/src/main/bin/elasticsearch-sql-cli \
		x-pack/plugin/watcher/src/main/bin/elasticsearch-croneval

CONFLICTS=	elasticsearch7 elasticsearch91 elasticsearch92 elasticsearch93

OPTIONS_DEFINE=	LIBVEC DOCS
OPTIONS_DEFAULT=	LIBVEC DOCS

LIBVEC_DESC=	Build vector library (libvec.so)
LIBVEC_USES=	compiler:c++17-lang

# Move Gradle's home below ${WRKDIR} instead of using ${HOME}/.gradle
_GRADLE_ENV=	GRADLE_USER_HOME=${WRKDIR}/gradle-home \
				JAVA_TOOLCHAIN_HOME=${LOCALBASE}/openjdk25 \
				RUNTIME_JAVA_HOME=${LOCALBASE}/openjdk25 \
				LOCAL_VEC_BINARY_OS=freebsd

_GRADLE_RUN=	${SETENV} ${_GRADLE_ENV} gradle --no-daemon

_LOCALDIST=	${WRKSRC}/build/distribution/local/${PORTNAME}-${PORTVERSION}

.include <bsd.port.options.mk>

CONFIG_FILES=	elasticsearch.yml jvm.options log4j2.properties \
		role_mapping.yml roles.yml
BINS=		elasticsearch elasticsearch-certgen elasticsearch-certutil \
		elasticsearch-cli elasticsearch-create-enrollment-token \
		elasticsearch-croneval elasticsearch-env \
		elasticsearch-env-from-file elasticsearch-geoip \
		elasticsearch-keystore elasticsearch-node elasticsearch-plugin \
		elasticsearch-reconfigure-node elasticsearch-reset-password \
		elasticsearch-saml-metadata elasticsearch-service-tokens \
		elasticsearch-setup-passwords elasticsearch-shard \
		elasticsearch-sql-cli elasticsearch-sql-cli-${DISTVERSION}.jar \
		elasticsearch-syskeygen elasticsearch-users

PORTDOCS=	LICENSE.txt NOTICE.txt README.asciidoc

SIGAR_ARCH=	${ARCH:S|i386|x86|}
SEARCHUSER?=	elasticsearch
SEARCHGROUP?=	${SEARCHUSER}
USERS=		${SEARCHUSER}
GROUPS=		${SEARCHGROUP}

PORTSCOUT=	limit:^8

SUB_FILES=	pkg-message
SUB_LIST=	ETCDIR=${ETCDIR} \
		JAVA_HOME=${JAVA_HOME} \
		BINDIR=${PREFIX}/bin \
		SEARCHUSER=${SEARCHUSER}

post-extract:
	${CP} ${PATCHDIR}/public.key ${WRKSRC}/ ## extracted from: x-pack-core-X.XX.X.jar

post-patch:
	${RM} ${WRKSRC}/gradle/gradle-daemon-jvm.properties

do-configure:
	@${MKDIR} ${WRKDIR}/gradle-home
	@${ECHO_CMD} "org.gradle.java.installations.paths=${LOCALBASE}/openjdk17,${LOCALBASE}/openjdk19,${LOCALBASE}/openjdk20,${LOCALBASE}/openjdk21,${LOCALBASE}/openjdk22,${LOCALBASE}/openjdk23,${LOCALBASE}/openjdk25" > ${WRKDIR}/gradle-home/gradle.properties

do-build:
	cd ${WRKSRC} && ${_GRADLE_RUN} localDistro -D"build.snapshot=false" -D"license.key=public.key"

post-build:
	${RM} ${_LOCALDIST}/modules/repository-azure/jna-5*.jar
	${REINPLACE_CMD} -e 's|hs_err_pid%p.log|$${ES_TMPDIR}/hs_err_pid%p.log|g' ${_LOCALDIST}/config/jvm.options
	${REINPLACE_CMD} -e 's|gc.log|$${ES_TMPDIR}/gc.log|g' ${_LOCALDIST}/config/jvm.options
	@${PRINTF} "\n## Path to native libraries\n-Des.nativelibs.path=${LOCALBASE}/lib\n" >> ${_LOCALDIST}/config/jvm.options
	${REINPLACE_CMD} -e 's|#path.data: /path/to/data|path.data: /var/db/elasticsearch|g' ${_LOCALDIST}/config/elasticsearch.yml
	${REINPLACE_CMD} -e 's|#path.logs: /path/to/logs|path.logs: /var/log/elasticsearch|g' ${_LOCALDIST}/config/elasticsearch.yml
	@${PRINTF} "\n# ml is currently not supported on FreeBSD\nxpack.ml.enabled: false\n" >> ${_LOCALDIST}/config/elasticsearch.yml

do-install:
	${INSTALL} -d -m 0750 ${STAGEDIR}${PREFIX}/etc/elasticsearch
.for f in ${CONFIG_FILES}
	${INSTALL} ${_LOCALDIST}/config/${f} ${STAGEDIR}${ETCDIR}/${f}.sample
.endfor
	${MKDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/bin
.for f in ${BINS}
	${INSTALL_SCRIPT} ${_LOCALDIST}/bin/${f} ${STAGEDIR}${PREFIX}/lib/elasticsearch/bin
	${RLN} ${STAGEDIR}${PREFIX}/lib/elasticsearch/bin/${f} ${STAGEDIR}${PREFIX}/bin/${f}
	${ECHO_CMD} "bin/${f}" >> ${TMPPLIST}
.endfor
	${MKDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/lib
	(cd ${_LOCALDIST}/lib && ${COPYTREE_SHARE} . ${STAGEDIR}${PREFIX}/lib/elasticsearch/lib/ "-name *\.jar")
	${MKDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/modules
	(cd ${_LOCALDIST}/modules && ${COPYTREE_SHARE} . ${STAGEDIR}${PREFIX}/lib/elasticsearch/modules/)
	${MKDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/plugins
	${MKDIR} ${STAGEDIR}${PREFIX}/libexec/elasticsearch
	${INSTALL} -lrs ${STAGEDIR}${ETCDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/config
	${LN} -s ${JAVASHAREDIR}/classes/jna.jar ${STAGEDIR}${PREFIX}/lib/elasticsearch/lib/jna-0.0.0.jar
	${MKDIR} ${STAGEDIR}${ETCDIR}/jvm.options.d
	${TOUCH} ${STAGEDIR}${ETCDIR}/users.sample ${STAGEDIR}${ETCDIR}/users_roles.sample

post-build-LIBVEC-on:
	@${PRINTF} "\n-Dorg.elasticsearch.nativeaccess.enableVectorLibrary=true\n" >> ${_LOCALDIST}/config/jvm.options

post-build-LIBVEC-off:
	@${PRINTF} "\n-Dorg.elasticsearch.nativeaccess.enableVectorLibrary=false\n" >> ${_LOCALDIST}/config/jvm.options

do-build-LIBVEC-on:
	@cd ${WRKSRC}/libs/simdvec/native && \
		${_GRADLE_RUN} buildSharedLibrary

do-install-LIBVEC-on:
	${INSTALL_LIB} ${WRKSRC}/libs/simdvec/native/build/libs/vec/shared/amd64/libvec.so ${STAGEDIR}${PREFIX}/lib/
	${ECHO} "${PREFIX}/lib/libvec.so" >> ${TMPPLIST}

do-install-DOCS-on:
	${MKDIR} ${STAGEDIR}${DOCSDIR}
.for f in ${PORTDOCS}
	${INSTALL_DATA} ${WRKSRC}/${f} ${STAGEDIR}${DOCSDIR}
.endfor

post-install:
	${FIND} -s ${STAGEDIR}${PREFIX}/lib/elasticsearch -not -type d | ${SORT} | \
		${SED} -e 's#^${STAGEDIR}${PREFIX}/##' >> ${TMPPLIST}
	${ECHO} "@dir lib/elasticsearch/modules/x-pack-ml/platform" >> ${TMPPLIST}
	${ECHO} "@dir lib/elasticsearch/plugins" >> ${TMPPLIST}
	${ECHO} "@dir libexec/elasticsearch" >> ${TMPPLIST}
	${ECHO} "@dir(${SEARCHUSER},${SEARCHGROUP},0755) ${ETCDIR}" >> ${TMPPLIST}
	${ECHO} "@owner ${SEARCHUSER}" >> ${TMPPLIST}
	${ECHO} "@group ${SEARCHGROUP}" >> ${TMPPLIST}
	${ECHO} "@mode 0750" >> ${TMPPLIST}
	${ECHO} "@dir ${ETCDIR}/jvm.options.d" >> ${TMPPLIST}
	${ECHO} "@mode 0660" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/users.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/users_roles.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/elasticsearch.yml.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/jvm.options.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/log4j2.properties.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/role_mapping.yml.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/roles.yml.sample" >> ${TMPPLIST}
	${ECHO} "@mode" >> ${TMPPLIST}
	${ECHO} "@owner" >> ${TMPPLIST}
	${ECHO} "@group" >> ${TMPPLIST}

.include <bsd.port.mk>
