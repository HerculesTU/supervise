select T.SYSUSER_ID,T.SYSUSER_ACCOUNT,T.SYSUSER_NAME,T.SYSUSER_MOBILE,C.COMPANY_NAME
,D.DEPART_NAME
from PLAT_SYSTEM_SYSUSER T
 LEFT JOIN PLAT_SYSTEM_COMPANY C ON T.SYSUSER_COMPANYID=C.COMPANY_ID
 LEFT JOIN PLAT_SYSTEM_DEPART D ON T.SYSUSER_DEPARTID=D.DEPART_ID
 WHERE T.SYSUSER_ID in 