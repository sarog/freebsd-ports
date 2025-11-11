PORTNAME=	elasticsearch
PORTVERSION=	9.2.0
CATEGORIES=	textproc java devel
MASTER_SITES=	https://github.com/portsbuild/${PORTNAME}/releases/download/v${PORTVERSION}/
PKGNAMESUFFIX=	92
DISTNAME=	${PORTNAME}-${PORTVERSION}-freebsd-x86_64

MAINTAINER=	elastic@FreeBSD.org
COMMENT=	Distributed, RESTful search and analytics engine
WWW=		https://www.elastic.co/

LICENSE=	APACHE20

BUILD_DEPENDS=	jna>0:devel/jna
RUN_DEPENDS=	bash:shells/bash \
		jna>0:devel/jna

USES=		cpe java:run
JAVA_VERSION=	21+
USE_RC_SUBR=	${PORTNAME}

CONFLICTS=	elasticsearch6 elasticsearch7 elasticsearch8 elasticsearch91

NO_ARCH=	yes
NO_BUILD=	yes
WRKSRC=		${WRKDIR}/${PORTNAME}-${PORTVERSION}

OPTIONS_DEFINE=	DOCS

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
		elasticsearch-sql-cli elasticsearch-sql-cli-${PORTVERSION}.jar \
		elasticsearch-syskeygen elasticsearch-users

PORTDOCS=	LICENSE.txt NOTICE.txt

SIGAR_ARCH=	${ARCH:S|i386|x86|}
SEARCHUSER?=	elasticsearch
SEARCHGROUP?=	${SEARCHUSER}
USERS=		${SEARCHUSER}
GROUPS=		${SEARCHGROUP}

PORTSCOUT=	limit:^9

SUB_FILES=	pkg-message
SUB_LIST=	ETCDIR=${ETCDIR} \
		JAVA_HOME=${JAVA_HOME} \
		BINDIR=${PREFIX}/bin

post-patch:
	${RM} ${WRKSRC}/modules/repository-azure/jna-5*.jar
	${REINPLACE_CMD} -e 's|%%LOCALBASE%%|${LOCALBASE}|g' ${WRKSRC}/config/jvm.options

do-install:
	${INSTALL} -d -m 0750 ${STAGEDIR}${PREFIX}/etc/elasticsearch
.for f in ${CONFIG_FILES}
	${INSTALL} ${WRKSRC}/config/${f} ${STAGEDIR}${ETCDIR}/${f}.sample
.endfor
	${MKDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/bin
.for f in ${BINS}
	${INSTALL_SCRIPT} ${WRKSRC}/bin/${f} ${STAGEDIR}${PREFIX}/lib/elasticsearch/bin
	${RLN} ${STAGEDIR}${PREFIX}/lib/elasticsearch/bin/${f} ${STAGEDIR}${PREFIX}/bin/${f}
	${ECHO_CMD} "bin/${f}" >> ${TMPPLIST}
.endfor
	${MKDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/lib
	(cd ${WRKSRC}/lib && ${COPYTREE_SHARE} . ${STAGEDIR}${PREFIX}/lib/elasticsearch/lib/ "-name *\.jar")

	${MKDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/modules
	(cd ${WRKSRC}/modules && ${COPYTREE_SHARE} . ${STAGEDIR}${PREFIX}/lib/elasticsearch/modules/)
	${MKDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/plugins
	${MKDIR} ${STAGEDIR}${PREFIX}/libexec/elasticsearch
	${INSTALL} -lrs ${STAGEDIR}${ETCDIR} ${STAGEDIR}${PREFIX}/lib/elasticsearch/config
	${LN} -s ${JAVASHAREDIR}/classes/jna.jar ${STAGEDIR}${PREFIX}/lib/elasticsearch/lib/jna-0.0.0.jar
	${MKDIR} ${STAGEDIR}${ETCDIR}/jvm.options.d
	${TOUCH} ${STAGEDIR}${ETCDIR}/users.sample ${STAGEDIR}${ETCDIR}/users_roles.sample

do-install-DOCS-on:
	${MKDIR} ${STAGEDIR}${DOCSDIR}
.for f in ${PORTDOCS}
	${INSTALL_DATA} ${WRKSRC}/${f} ${STAGEDIR}${DOCSDIR}
.endfor

post-install:
	${ECHO} "@sample ${ETCDIR}/elasticsearch.yml.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/jvm.options.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/log4j2.properties.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/role_mapping.yml.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/roles.yml.sample" >> ${TMPPLIST}
	${FIND} -s ${STAGEDIR}${PREFIX}/lib/elasticsearch -not -type d | ${SORT} | \
		${SED} -e 's#^${STAGEDIR}${PREFIX}/##' >> ${TMPPLIST}
	${ECHO} "@dir lib/elasticsearch/plugins" >> ${TMPPLIST}
	${ECHO} "@dir libexec/elasticsearch" >> ${TMPPLIST}
	${ECHO} "@dir(${SEARCHUSER},${SEARCHGROUP},0755) ${ETCDIR}" >> ${TMPPLIST}
	${ECHO} "@owner ${SEARCHUSER}" >> ${TMPPLIST}
	${ECHO} "@group ${SEARCHGROUP}" >> ${TMPPLIST}
	${ECHO} "@mode 0640" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/users.sample" >> ${TMPPLIST}
	${ECHO} "@sample ${ETCDIR}/users_roles.sample" >> ${TMPPLIST}
	${ECHO} "@mode" >> ${TMPPLIST}
	${ECHO} "@owner" >> ${TMPPLIST}
	${ECHO} "@group" >> ${TMPPLIST}

.include <bsd.port.mk>
