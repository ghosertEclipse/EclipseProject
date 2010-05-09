
typedef struct {
	int  OpenSleep;         // �ӳ�ʱ��
	char CMGC[15];			// �����Ƿ�֧�ַ���Ϣָ��
	char CSCS[15];          // �����ֻ����Ա���
	char CSCA[15];          // ȡ�����ķ������
	char CMGS[15];          // ���Ͷ���Ϣ
	char CGMI[15];          // ������
	char CGMM[15];          // �ֻ��ͺ�
	char CGMR[15];          // ����汾
	char CGSN[15];          // IMEI
	char CBC[15];           // ����
	char CSQ[15];           // �ź���
	char CNUM[15];          // ��������
	char SCID[15];          // SIM������
	char CIMI[15];          // IMSI
	char CCLK1[15];         // ���ʱ��
	char CCLK2[20];         // ����ʱ��
	char CPMS[20];          // ���ö��Ŵ洢��
	char CMGL[15];          // ��ȡ����Ϣ
    char CMGW[20];          // �������Ϣ
    char CMSS[15];          // ����δ����Ϣ
	char CPMS2[15];         // ��ö���������С
	char CMGD[15];          // ɾ����Ϣ
	char CPBS1[15];         // ���õ绰���洢�������ص绰������
	char CPBS2[15];         // ��õ绰������
	char CPBR[15];          // ��ȡ�绰��
	char CPBR2[15];         // ���ص绰������
	char CPBR3[15];         // ���ص绰���ռ�
	char CPBW1[20];         // д�绰��
	char CPBW2[15];         // ɾ���绰��
	char CPBW3[25];         // �޸ĵ绰��
} ATCOMMANDSET;

typedef struct _SIEMENS{
	int  OpenSleep;         // �ӳ�ʱ��
	char CMGC[15];			// �����Ƿ�֧�ַ���Ϣָ��
	char CSCS[15];          // �����ֻ����Ա���
	char CSCA[15];          // ȡ�����ķ������
	char CMGS[15];          // ���Ͷ���Ϣ
	char CGMI[15];          // ������
	char CGMM[15];          // �ֻ��ͺ�
	char CGMR[15];          // ����汾
	char CGSN[15];          // IMEI
	char CBC[15];           // ����
	char CSQ[15];           // �ź���
	char CNUM[15];          // ��������
	char SCID[15];          // SIM������
	char CIMI[15];          // IMSI
	char CCLK1[15];         // ���ʱ��
	char CCLK2[20];         // ����ʱ��
	char CPMS[20];          // ���ö��Ŵ洢��
	char CMGL[15];          // ��ȡ����Ϣ
    char CMGW[20];          // �������Ϣ
    char CMSS[15];          // ����δ����Ϣ
	char CPMS2[15];         // ��ö���������С
	char CMGD[15];          // ɾ����Ϣ
	char CPBS1[15];         // ���õ绰���洢�������ص绰������
	char CPBS2[15];         // ��õ绰������
	char CPBR[15];          // ��ȡ�绰��
	char CPBR2[15];         // ���ص绰������
	char CPBR3[15];         // ���ص绰���ռ�
	char CPBW1[20];         // д�绰��
	char CPBW2[15];         // ɾ���绰��
	char CPBW3[25];         // �޸ĵ绰��

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
		strcpy(CPBR2,"");               //δʹ��
		strcpy(CPBR3,"AT+CPBR=?\r");
        strcpy(CPBW1,"AT+CPBW=,%s,%d,%s\r");
		strcpy(CPBW2,"AT+CPBW=%d\r");
		strcpy(CPBW3,"AT+CPBW=%d,%s,%d,%s\r");
	} 
} SIEMENS, *PSIEMENS;


typedef struct _ERICSSON{
	int  OpenSleep;         // �ӳ�ʱ��
	char CMGC[15];			// �����Ƿ�֧�ַ���Ϣָ��
	char CSCS[15];          // �����ֻ����Ա���
	char CSCA[15];          // ȡ�����ķ������
	char CMGS[15];          // ���Ͷ���Ϣ
	char CGMI[15];          // ������
	char CGMM[15];          // �ֻ��ͺ�
	char CGMR[15];          // ����汾
	char CGSN[15];          // IMEI
	char CBC[15];           // ����
	char CSQ[15];           // �ź���
	char CNUM[15];          // ��������
	char SCID[15];          // SIM������
	char CIMI[15];          // IMSI
	char CCLK1[15];         // ���ʱ��
	char CCLK2[20];         // ����ʱ��
	char CPMS[20];          // ���ö��Ŵ洢��
	char CMGL[15];          // ��ȡ����Ϣ
    char CMGW[20];          // �������Ϣ
    char CMSS[15];          // ����δ����Ϣ
	char CPMS2[15];         // ��ö���������С
	char CMGD[15];          // ɾ����Ϣ
	char CPBS1[15];         // ���õ绰���洢�������ص绰������
	char CPBS2[15];         // ��õ绰������
	char CPBR[15];          // ��ȡ�绰��
	char CPBR2[15];         // ���ص绰������
	char CPBR3[15];         // ���ص绰���ռ�
	char CPBW1[20];         // д�绰��
	char CPBW2[15];         // ɾ���绰��
	char CPBW3[25];         // �޸ĵ绰��

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
		strcpy(SCID,"");                   //��δ�ҵ�   
		strcpy(CIMI,"AT+CIMI\r");
		strcpy(CCLK1,"AT+CCLK?\r");
		strcpy(CCLK2,"AT+CCLK=\"%s\"\r");
		strcpy(CPMS,"AT+CPMS=%s,%s\r");
		strcpy(CMGL,"AT+CMGL=%d\r");       //�Ƿ�����ͬ���ܵ���չָ��?����:AT^SMGL������
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
	int  OpenSleep;         // �ӳ�ʱ��
	char CMGC[15];			// �����Ƿ�֧�ַ���Ϣָ��
	char CSCS[15];          // �����ֻ����Ա���
	char CSCA[15];          // ȡ�����ķ������
	char CMGS[15];          // ���Ͷ���Ϣ
	char CGMI[15];          // ������
	char CGMM[15];          // �ֻ��ͺ�
	char CGMR[15];          // ����汾
	char CGSN[15];          // IMEI
	char CBC[15];           // ����
	char CSQ[15];           // �ź���
	char CNUM[15];          // ��������
	char SCID[15];          // SIM������
	char CIMI[15];          // IMSI
	char CCLK1[15];         // ���ʱ��
	char CCLK2[20];         // ����ʱ��
	char CPMS[20];          // ���ö��Ŵ洢��
	char CMGL[15];          // ��ȡ����Ϣ
    char CMGW[20];          // �������Ϣ
    char CMSS[15];          // ����δ����Ϣ
	char CPMS2[15];         // ��ö���������С
	char CMGD[15];          // ɾ����Ϣ
	char CPBS1[15];         // ���õ绰���洢�������ص绰������
	char CPBS2[15];         // ��õ绰������
	char CPBR[15];          // ��ȡ�绰��
	char CPBR2[15];         // ���ص绰������
	char CPBR3[15];         // ���ص绰���ռ�
	char CPBW1[20];         // д�绰��
	char CPBW2[15];         // ɾ���绰��
	char CPBW3[25];         // �޸ĵ绰��

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
		strcpy(SCID,"");                   //��δ�ҵ�
		strcpy(CIMI,"AT+CIMI\r");          //8250;8210��֧��
		strcpy(CCLK1,"AT+CCLK?\r");        //8250;8210��֧��
		strcpy(CCLK2,"AT+CCLK=\"%s\"\r");  //8250;8210��֧��
		strcpy(CPMS,"AT+CPMS=%s,%s\r");
		strcpy(CMGL,"AT+CMGL=%d\r");       //�Ƿ�����ͬ���ܵ���չָ��?����:AT^SMGL������
		strcpy(CMGW,"AT+CMGW=%d,%d\r");
		strcpy(CMSS,"AT+CMSS=%d\r");
		strcpy(CPMS2,"AT+CPMS=%s\r");
		strcpy(CMGD,"AT+CMGD=%d\r");
		strcpy(CPBS1,"AT+CPBS=%s\r");
		strcpy(CPBS2,"AT+CPBS?\r");
        strcpy(CPBR,"AT+CPBR=%d\r");
		strcpy(CPBR2,"");                  //δʹ��
		strcpy(CPBR3,"AT+CPBR=?\r");
        strcpy(CPBW1,"AT+CPBW=,%s,%d,%s\r");
		strcpy(CPBW2,"AT+CPBW=%d\r");
		strcpy(CPBW3,"AT+CPBW=%d,%s,%d,%s\r");
	} 
} NOKIA, *PNOKIA;