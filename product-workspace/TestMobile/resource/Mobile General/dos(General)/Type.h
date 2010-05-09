
typedef struct {
	int  OpenSleep;         // 延迟时间
	char CMGC[15];			// 测试是否支持发消息指令
	char CSCS[15];          // 设置手机语言编码
	char CSCA[15];          // 取得中心服务号码
	char CMGS[15];          // 发送短消息
	char CGMI[15];          // 制造商
	char CGMM[15];          // 手机型号
	char CGMR[15];          // 软件版本
	char CGSN[15];          // IMEI
	char CBC[15];           // 电量
	char CSQ[15];           // 信号量
	char CNUM[15];          // 本机号码
	char SCID[15];          // SIM卡号码
	char CIMI[15];          // IMSI
	char CCLK1[15];         // 获得时间
	char CCLK2[20];         // 设置时间
	char CPMS[20];          // 设置短信存储器
	char CMGL[15];          // 读取短消息
    char CMGW[20];          // 保存短消息
    char CMSS[15];          // 发送未发消息
	char CPMS2[15];         // 获得短信容量大小
	char CMGD[15];          // 删除消息
	char CPBS1[15];         // 设置电话簿存储器，返回电话簿数量
	char CPBS2[15];         // 获得电话簿容量
	char CPBR[15];          // 读取电话簿
	char CPBR2[15];         // 返回电话簿数量
	char CPBR3[15];         // 返回电话簿空间
	char CPBW1[20];         // 写电话簿
	char CPBW2[15];         // 删除电话簿
	char CPBW3[25];         // 修改电话簿
} ATCOMMANDSET;

typedef struct _SIEMENS{
	int  OpenSleep;         // 延迟时间
	char CMGC[15];			// 测试是否支持发消息指令
	char CSCS[15];          // 设置手机语言编码
	char CSCA[15];          // 取得中心服务号码
	char CMGS[15];          // 发送短消息
	char CGMI[15];          // 制造商
	char CGMM[15];          // 手机型号
	char CGMR[15];          // 软件版本
	char CGSN[15];          // IMEI
	char CBC[15];           // 电量
	char CSQ[15];           // 信号量
	char CNUM[15];          // 本机号码
	char SCID[15];          // SIM卡号码
	char CIMI[15];          // IMSI
	char CCLK1[15];         // 获得时间
	char CCLK2[20];         // 设置时间
	char CPMS[20];          // 设置短信存储器
	char CMGL[15];          // 读取短消息
    char CMGW[20];          // 保存短消息
    char CMSS[15];          // 发送未发消息
	char CPMS2[15];         // 获得短信容量大小
	char CMGD[15];          // 删除消息
	char CPBS1[15];         // 设置电话簿存储器，返回电话簿数量
	char CPBS2[15];         // 获得电话簿容量
	char CPBR[15];          // 读取电话簿
	char CPBR2[15];         // 返回电话簿数量
	char CPBR3[15];         // 返回电话簿空间
	char CPBW1[20];         // 写电话簿
	char CPBW2[15];         // 删除电话簿
	char CPBW3[25];         // 修改电话簿

	_SIEMENS::_SIEMENS()
	{
		OpenSleep=100;
		strcpy(CMGC,"AT+CMGC=?\r");
		strcpy(CSCS,"AT+CSCS=%s\r");
		strcpy(CSCA,"AT+CSCA?\r");
		strcpy(CMGS,"AT+CMGS=%d\r");
		strcpy(CGMI,"AT+CGMI\r");
		strcpy(CGMM,"AT+CGMM\r");
		strcpy(CGMR,"AT+CGMR\r");
		strcpy(CGSN,"AT+CGSN\r");
		strcpy(CBC,	"AT+CBC\r");
		strcpy(CSQ,	"AT+CSQ\r");
		strcpy(CNUM,"AT+CNUM\r");
		strcpy(SCID,"AT^SCID\r");        //
		strcpy(CIMI,"AT+CIMI\r");
		strcpy(CCLK1,"AT+CCLK?\r");
		strcpy(CCLK2,"AT+CCLK=\"%s\"\r");
		strcpy(CPMS,"AT+CPMS=%s,%s\r");
		strcpy(CMGL,"AT^SMGL=%d\r");     //AT+CMGL=4
		strcpy(CMGW,"AT+CMGW=%d,%d\r");
		strcpy(CMSS,"AT+CMSS=%d\r");
		strcpy(CPMS2,"AT+CPMS=%s\r");
		strcpy(CMGD,"AT+CMGD=%d\r");
		strcpy(CPBS1,"AT+CPBS=%s\r");
		strcpy(CPBS2,"AT+CPBS?\r");
        strcpy(CPBR,"AT+CPBR=%d\r");
		strcpy(CPBR2,"");               //未使用
		strcpy(CPBR3,"AT+CPBR=?\r");
        strcpy(CPBW1,"AT+CPBW=,%s,%d,%s\r");
		strcpy(CPBW2,"AT+CPBW=%d\r");
		strcpy(CPBW3,"AT+CPBW=%d,%s,%d,%s\r");
	} 
} SIEMENS, *PSIEMENS;


typedef struct _ERICSSON{
	int  OpenSleep;         // 延迟时间
	char CMGC[15];			// 测试是否支持发消息指令
	char CSCS[15];          // 设置手机语言编码
	char CSCA[15];          // 取得中心服务号码
	char CMGS[15];          // 发送短消息
	char CGMI[15];          // 制造商
	char CGMM[15];          // 手机型号
	char CGMR[15];          // 软件版本
	char CGSN[15];          // IMEI
	char CBC[15];           // 电量
	char CSQ[15];           // 信号量
	char CNUM[15];          // 本机号码
	char SCID[15];          // SIM卡号码
	char CIMI[15];          // IMSI
	char CCLK1[15];         // 获得时间
	char CCLK2[20];         // 设置时间
	char CPMS[20];          // 设置短信存储器
	char CMGL[15];          // 读取短消息
    char CMGW[20];          // 保存短消息
    char CMSS[15];          // 发送未发消息
	char CPMS2[15];         // 获得短信容量大小
	char CMGD[15];          // 删除消息
	char CPBS1[15];         // 设置电话簿存储器，返回电话簿数量
	char CPBS2[15];         // 获得电话簿容量
	char CPBR[15];          // 读取电话簿
	char CPBR2[15];         // 返回电话簿数量
	char CPBR3[15];         // 返回电话簿空间
	char CPBW1[20];         // 写电话簿
	char CPBW2[15];         // 删除电话簿
	char CPBW3[25];         // 修改电话簿

	_ERICSSON::_ERICSSON()
	{
		OpenSleep=250;
		strcpy(CMGC,"AT+CMGC=?\r");
		strcpy(CSCS,"AT+CSCS=%s\r");
		strcpy(CSCA,"AT+CSCA?\r");
		strcpy(CMGS,"AT+CMGS=%d\r");
		strcpy(CGMI,"AT+CGMI\r");
		strcpy(CGMM,"AT+CGMM\r");
		strcpy(CGMR,"AT+CGMR\r");
		strcpy(CGSN,"AT+CGSN\r");
		strcpy(CBC,	"AT+CBC\r");
		strcpy(CSQ,	"AT+CSQ\r");
		strcpy(CNUM,"AT+CNUM\r");
		strcpy(SCID,"");                   //尚未找到   
		strcpy(CIMI,"AT+CIMI\r");
		strcpy(CCLK1,"AT+CCLK?\r");
		strcpy(CCLK2,"AT+CCLK=\"%s\"\r");
		strcpy(CPMS,"AT+CPMS=%s,%s\r");
		strcpy(CMGL,"AT+CMGL=%d\r");       //是否有相同功能的扩展指令?例如:AT^SMGL再找找
		strcpy(CMGW,"AT+CMGW=%d,%d\r");
		strcpy(CMSS,"AT+CMSS=%d\r");
		strcpy(CPMS2,"AT+CPMS=%s\r");
		strcpy(CMGD,"AT+CMGD=%d\r");
		strcpy(CPBS1,"AT+CPBS=%s\r");
		strcpy(CPBS2,"AT+CPBS?\r");
        strcpy(CPBR,"AT+CPBR=%d\r");
		strcpy(CPBR2,"AT+CPBR=%d,%d\r");   //
		strcpy(CPBR3,"AT+CPBR=?\r");
        strcpy(CPBW1,"AT+CPBW=,%s,%d,%s\r");
		strcpy(CPBW2,"AT+CPBW=%d\r");
		strcpy(CPBW3,"AT+CPBW=%d,%s,%d,%s\r");
	} 
} ERICSSON, *PERICSSON;



typedef struct _NOKIA{
	int  OpenSleep;         // 延迟时间
	char CMGC[15];			// 测试是否支持发消息指令
	char CSCS[15];          // 设置手机语言编码
	char CSCA[15];          // 取得中心服务号码
	char CMGS[15];          // 发送短消息
	char CGMI[15];          // 制造商
	char CGMM[15];          // 手机型号
	char CGMR[15];          // 软件版本
	char CGSN[15];          // IMEI
	char CBC[15];           // 电量
	char CSQ[15];           // 信号量
	char CNUM[15];          // 本机号码
	char SCID[15];          // SIM卡号码
	char CIMI[15];          // IMSI
	char CCLK1[15];         // 获得时间
	char CCLK2[20];         // 设置时间
	char CPMS[20];          // 设置短信存储器
	char CMGL[15];          // 读取短消息
    char CMGW[20];          // 保存短消息
    char CMSS[15];          // 发送未发消息
	char CPMS2[15];         // 获得短信容量大小
	char CMGD[15];          // 删除消息
	char CPBS1[15];         // 设置电话簿存储器，返回电话簿数量
	char CPBS2[15];         // 获得电话簿容量
	char CPBR[15];          // 读取电话簿
	char CPBR2[15];         // 返回电话簿数量
	char CPBR3[15];         // 返回电话簿空间
	char CPBW1[20];         // 写电话簿
	char CPBW2[15];         // 删除电话簿
	char CPBW3[25];         // 修改电话簿

	_NOKIA::_NOKIA()
	{
		OpenSleep=100;
		strcpy(CMGC,"AT+CMGC=?\r");
		strcpy(CSCS,"AT+CSCS=%s\r");
		strcpy(CSCA,"AT+CSCA?\r");
		strcpy(CMGS,"AT+CMGS=%d\r");
		strcpy(CGMI,"AT+CGMI\r");
		strcpy(CGMM,"AT+CGMM\r");
		strcpy(CGMR,"AT+CGMR\r");
		strcpy(CGSN,"AT+CGSN\r");
		strcpy(CBC,	"AT+CBC\r");
		strcpy(CSQ,	"AT+CSQ\r");
		strcpy(CNUM,"AT+CNUM\r");
		strcpy(SCID,"");                   //尚未找到
		strcpy(CIMI,"AT+CIMI\r");          //8250;8210不支持
		strcpy(CCLK1,"AT+CCLK?\r");        //8250;8210不支持
		strcpy(CCLK2,"AT+CCLK=\"%s\"\r");  //8250;8210不支持
		strcpy(CPMS,"AT+CPMS=%s,%s\r");
		strcpy(CMGL,"AT+CMGL=%d\r");       //是否有相同功能的扩展指令?例如:AT^SMGL再找找
		strcpy(CMGW,"AT+CMGW=%d,%d\r");
		strcpy(CMSS,"AT+CMSS=%d\r");
		strcpy(CPMS2,"AT+CPMS=%s\r");
		strcpy(CMGD,"AT+CMGD=%d\r");
		strcpy(CPBS1,"AT+CPBS=%s\r");
		strcpy(CPBS2,"AT+CPBS?\r");
        strcpy(CPBR,"AT+CPBR=%d\r");
		strcpy(CPBR2,"");                  //未使用
		strcpy(CPBR3,"AT+CPBR=?\r");
        strcpy(CPBW1,"AT+CPBW=,%s,%d,%s\r");
		strcpy(CPBW2,"AT+CPBW=%d\r");
		strcpy(CPBW3,"AT+CPBW=%d,%s,%d,%s\r");
	} 
} NOKIA, *PNOKIA;