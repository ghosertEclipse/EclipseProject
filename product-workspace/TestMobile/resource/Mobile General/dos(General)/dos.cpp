#include <windows.h>
#include <iostream.h>
#include <stdio.h>
#include "Comm.h"
#include "Sms.h"
#include "Type.h"

//�ⲿ�ӿ�
bool OpenComms(char Comm[10], int BaudRate);
bool CloseComms();
bool MonitorPhone(char Comm[10], int& BaudRate);
int MonitorComm(char Comm[10]);

bool SendShortMessage(char SMSContent[161], 
					  char TargetNum[20], 
					  bool ReportStatus, 
					  int iValidTime, 
					  bool HandFree
					  );

bool GetMobileInfo(char NameOfManu[20], 
				   char NameOfTele[20], 
				   char VerOfSW[40], 
				   char IMEI[20], 
				   char BatteryCharge[5], 
				   char SignalQuality[5], 
				   char OwnTeleNum[20]
				   );

bool GetSIMInfo(char NumOfSIM[25], 
				char IMSI[20], 
				char SMSCNum[20]
				);

bool GetMETime(char Time[25]);

bool SetMETime(char Time[25]);

int SetSMSMemoGetSMSNum(char Type[8]);


typedef struct {           //�����ṹ
	int Index;             //�����ţ�δ��˳������
	int Status;            //��Ϣ״̬��0-δ����1-�Ѷ���2-δ����3-�ѷ�
	char TeleNum[20];      //���ͷ�����
	char Time[20];         //����ʱ���(Status=0,1ʱ��Ҫ��䣩
	bool ReportStatus;     //״̬����(Status=2,3ʱ��Ҫ��䣩
	int ValidTime;         //��Ч��(Status=2,3ʱ��Ҫ��䣩
	bool HandFree;         //����(Status=2,3ʱ��Ҫ��䣩
	char Message[161];     //һ����ʮһ�ַ��Ķ���Ϣ����
	unsigned char MessageSign[7];  //��������Ϣ��־
} RECSMS;
bool ListShortMessage(RECSMS *pSMS, int i);
int  SaveShortMessage(char SMSContent[161], 
					  char TargetNum[20], 
					  bool ReportStatus, 
					  int iValidTime, 
					  bool HandFree,
					  int Status
					  );
bool SendUnSent( int Index );
int GetSMSSpace( char Type[5] );
bool DeleteShortMessage( int Index );



bool SetMobileEncodeFormat( char Type[20] );
bool SetTeleBookMemo( char Type[5] );
int GetTeleBookNum();
int GetTeleBookSpace();
bool ReadTeleBook( int Index, char TeleNum[20], char Name[14], int& Vip );
bool WriteTeleBook( char TeleNum[20], char Name[14], int Vip );
bool DeleteTeleBook( int Index );
bool ModifyTeleBook( int Index, char TeleNum[20], char Name[14], int Vip);


void SetMobileType(int Type);
void SetMaxTeleIndex(int Index);
int GetMaxTeleIndex();



//�ڲ�����
bool IsUnicode(char SMSContent[161]);
bool GetSMSCNum(char SMSCNum[20]);
bool GetOneLine(char ATCommand[30],char Value[50]);
char* GetLine(char* Data, int i);
ATCOMMANDSET at;
SIEMENS siemens;
ERICSSON ericsson;
NOKIA nokia;
int MobileType;              // 1-SIEMENS; 2-SONY ERICSSON; 3-NOKIA
int MaxTeleIndex;            //�绰�����������



int main()
{

	SetMobileType(2);       // 1-SIEMENS; 2-SONY ERICSSON; 3-NOKIA

/*
	char Comm[10];
    int BaudRate;
	memset(Comm,0,sizeof(char));
	MonitorPhone(Comm, BaudRate);
	cout<<Comm<<endl<<BaudRate<<endl;
*/

/*
    cout<<MonitorComm("COM5");
*/

/*
    OpenComms("COM9",115200);

	char SMSContent[161];
	char TargetNum[20];
	bool ReportStatus;
	int iValidTime;
	bool HandFree;

	strcpy(SMSContent,"����һ�����������������˰���������̫ǿ�ˣ�������!!!");
	strcpy(TargetNum,"8613916939847");    //8613764294680
	ReportStatus=false;
	iValidTime=0;                         //(0-5���� 1-һСʱ 2-ʮ��Сʱ 3-һ�� 4-һ�� 5-���ֵ)
    HandFree=false;
	
	cout<<SendShortMessage(SMSContent, TargetNum, ReportStatus, iValidTime, HandFree);
*/

/*
	OpenComms("COM9",115200);

	char NameOfManu[20];
	char NameOfTele[20];
	char VerOfSW[40];
	char IMEI[20];
	char BatteryCharge[5];
	char SignalQuality[5];
	char OwnTeleNum[20];

	cout<<GetMobileInfo(NameOfManu, NameOfTele, VerOfSW, IMEI,BatteryCharge, SignalQuality, OwnTeleNum);
	cout<<NameOfManu<<endl;
	cout<<NameOfTele<<endl;
	cout<<VerOfSW<<endl;
	cout<<IMEI<<endl;
	cout<<BatteryCharge<<endl;
	cout<<SignalQuality<<endl;
	cout<<OwnTeleNum<<endl;

	char NumOfSIM[25];
	char IMSI[20];
	char SMSCNum[20];

	cout<<GetSIMInfo(NumOfSIM,IMSI,SMSCNum);
    cout<<NumOfSIM<<endl;
	cout<<IMSI<<endl;
	cout<<SMSCNum<<endl;
*/

/*
	OpenComms("COM5",115200);
    char Time[25];
	GetMETime(Time);
	SetMETime(Time);
*/

/*
	OpenComms("COM5",115200);
	
	int i=SetSMSMemoGetSMSNum("\"SM\"");
	if (i>0)
	{
		RECSMS* pSMS;
		pSMS=new RECSMS[i];

		memset(pSMS,0,i*sizeof(RECSMS));

		if(ListShortMessage(pSMS,i)==true)
		{
			for(int j=0; j<i; j++)
			{
				cout<<"Index:  "<<pSMS[j].Index<<endl;
				cout<<"Status: "<<pSMS[j].Status<<endl;            
				cout<<"TeleNum:"<<pSMS[j].TeleNum<<endl;      
				cout<<"Time:   "<<pSMS[j].Time<<endl;         
				cout<<"ReportStatus:"<<pSMS[j].ReportStatus<<endl;     
				cout<<"ValidTime:"<<pSMS[j].ValidTime<<endl;         
				cout<<"HandFree:"<<pSMS[j].HandFree<<endl;        
				cout<<"Message:"<<pSMS[j].Message<<endl;
				cout<<"MessageSign:"<<pSMS[j].MessageSign<<endl<<endl;
			}
		}
		delete[] pSMS;
	}
	else if(i==0)//�޶���Ϣ
	{
	}
*/

/*
    OpenComms("COM5",115200);

	char SMSContent[161];
	char TargetNum[20];
	bool ReportStatus;
	int iValidTime;
	bool HandFree;

	strcpy(SMSContent,"Hello!");
	strcpy(TargetNum,"8613916939847");    //8613764294680
	ReportStatus=true;
	iValidTime=3;                         //(0-5���� 1-һСʱ 2-ʮ��Сʱ 3-һ�� 4-һ�� 5-���ֵ)
    HandFree=true;
	
	SetSMSMemoGetSMSNum("\"ME\"");//��Ҫѡ��һ�¼�����

	cout<<SaveShortMessage(SMSContent, TargetNum, ReportStatus, iValidTime, HandFree, 2);
*/

/*
    OpenComms("COM5",115200);
    
	SetSMSMemoGetSMSNum("\"SM\"");//��Ҫѡ��һ�¼�����
	
	cout<<SendUnSent(5);
*/

/*
    OpenComms("COM5",115200);
    cout<<GetSMSSpace("\"SM\"");
*/
/*
    OpenComms("COM5",115200);
	SetSMSMemoGetSMSNum("\"SM\"");      //��Ҫѡ��һ�¼�����
    cout<<DeleteShortMessage(2);
*/

/*
    OpenComms( "COM6",115200 );
    if (MobileType==1)
        SetMobileEncodeFormat("\"UCS2\""); //   "\"GSM\""   "\"UTF-8\""
    if (MobileType==2)
        SetMobileEncodeFormat("\"UTF-8\""); //   "\"GSM\""   "\"UTF-8\""
	if (MobileType==3)
		SetMobileEncodeFormat("\"HEX\""); //   "\"GSM\""   "\"UTF-8\""

    SetTeleBookMemo( "\"SM\"") ;         //   "\"ME\""
	int nCount=GetTeleBookNum();
    cout<<nCount<<endl; 

  //���鶨���������ĸ�������ɵĽṹ
	int Index;
	char TeleNum[20];
	char Name[14];
	int Vip=0;

	int i=0;
	int j=1;
	for(; i!=nCount; j++)
	{
		if(ReadTeleBook(j, TeleNum, Name, Vip))
		{   //���������Ľṹ
			cout<<Name<<": "<<TeleNum<<" "<<Vip<<endl;
			i++;
		}	
	}
	SetMaxTeleIndex(j);

	cout<<GetTeleBookSpace()<<endl;
*/

/*
    OpenComms( "COM6",115200 );

    char TeleNum[20];
	char Name[14];
	int Vip;

	strcpy(TeleNum,"13916939847");
	strcpy(Name,"�ż�ΰ");
	Vip=1;
	
	if (MobileType==1)
        SetMobileEncodeFormat("\"UCS2\""); //   "\"GSM\""   "\"UTF-8\""
    if (MobileType==2)
        SetMobileEncodeFormat("\"UTF-8\""); //   "\"GSM\""   "\"UTF-8\""
	if (MobileType==3)
		SetMobileEncodeFormat("\"HEX\""); //   "\"GSM\""   "\"UTF-8\""

	SetTeleBookMemo( "\"SM\"") ;
    cout<<WriteTeleBook( TeleNum, Name, Vip);
*/

/*
    OpenComms( "COM7",115200 );
    if (MobileType==1)
        SetMobileEncodeFormat("\"UCS2\""); //   "\"GSM\""   "\"UTF-8\""
    if (MobileType==2)
        SetMobileEncodeFormat("\"UTF-8\""); //   "\"GSM\""   "\"UTF-8\""
	if (MobileType==3)
		SetMobileEncodeFormat("\"HEX\""); //   "\"GSM\""   "\"UTF-8\""

    SetTeleBookMemo( "\"ME\"") ;
    cout<<DeleteTeleBook(42);
*/

/*
    OpenComms( "COM5",115200 );
	int Index;
    char TeleNum[20];
	char Name[14];
	int Vip;

	Index=2;
	strcpy(TeleNum,"13916939847");
	strcpy(Name,"�ż�ΰ");
	Vip=0;

	if (MobileType==1)
        SetMobileEncodeFormat("\"UCS2\""); //   "\"GSM\""   "\"UTF-8\""
    if (MobileType==2)
        SetMobileEncodeFormat("\"UTF-8\""); //   "\"GSM\""   "\"UTF-8\""
	if (MobileType==3)
		SetMobileEncodeFormat("\"HEX\""); //   "\"GSM\""   "\"UTF-8\""

	SetTeleBookMemo( "\"SM\"") ;
    cout<<ModifyTeleBook( Index,TeleNum, Name, Vip);
*/

//	CloseComms();

	return 0;
}

void SetMobileType(int Type)
{
	if (Type==1)
		memcpy((void*)&at,(void*)&siemens, sizeof(siemens));
	if (Type==2)
		memcpy((void*)&at,(void*)&ericsson, sizeof(ericsson));
	if (Type==3)
		memcpy((void*)&at,(void*)&nokia, sizeof(nokia));
	MobileType=Type;
}

void SetMaxTeleIndex(int Index)
{
	MaxTeleIndex=Index;
}
int GetMaxTeleIndex()
{
	return MaxTeleIndex;
}

bool OpenComms(char Comm[10], int BaudRate)
{
	char cmd[30];
	char ans[30];
	int nLength;
	if (OpenComm(Comm,BaudRate))  //if (OpenComm(Comms,BaudRates[i]))
	{
		sprintf(cmd,at.CMGC);
		WriteComm(cmd, strlen(cmd));	// ��������
		Sleep(at.OpenSleep);             //Sleep(250) for SONY ERRICSSON T618
		nLength=sReadComm(ans);	// ��Ӧ������
		ans[nLength]='\0';
		if (strcmp(ans,cmd)==0)        // Designed for find blue tooth device T618
			nLength=sReadComm(ans);
		if (strstr(ans,"OK\r\n"))
			return true;
	}
	return false;
}

bool CloseComms()
{
	if (CloseComm())
		return true;
	else
		return false;
}

bool MonitorPhone(char Comm[10], int& BaudRate)
{
//	int BaudRates[4]={115200, 57600, 38400, 19200};

	int iComm;
	char cComm[10];
    char Comms[10];

	for (iComm=1; iComm<=15; iComm++)
	{
		strcpy(Comms,"COM");
		_itoa(iComm,cComm,10);
		strcat(Comms,cComm);

//		for (int i=0; i<=3; i++)
//		{
			if (OpenComms(Comms,115200))  //if (OpenComm(Comms,BaudRates[i]))
			{
					strcpy(Comm,Comms);
					BaudRate=115200;    //BaudRate=BaudRates[i];
					CloseComm();
					return true;
			}

			CloseComm();
//		}
	}

	return false;
}

//return value: 1-Available 2-Not Existing 5-Occupied
int MonitorComm(char Comm[10])
{
	HANDLE hComm;
		
		hComm = CreateFile(Comm,	// �������ƻ��豸·��
		GENERIC_READ | GENERIC_WRITE,	// ��д��ʽ
		0,				// ����ʽ����ռ
		NULL,			// Ĭ�ϵİ�ȫ������
		OPEN_EXISTING,	// ������ʽ
		0,				// ���������ļ�����FILE_FLAG_OVERLAPPED
		NULL);			// �������ģ���ļ�

	if(hComm == INVALID_HANDLE_VALUE)  // �򿪴���ʧ��
	{
		int i=GetLastError();
		CloseHandle(hComm);
		return i;
	}
	else
	{
		CloseHandle(hComm);
		return 1;
	}

}

bool SendShortMessage(char SMSContent[161], char TargetNum[20], bool ReportStatus, int iValidTime, bool HandFree)
{

	SM_PARAM pMsg;
	::memset(&pMsg, 0, sizeof(SM_PARAM));
	char SMSCNum[20];
	unsigned char ValidTime[6]={0x00, 0x0B, 0x8F, 0xA7, 0xC4, 0xFF};


	GetSMSCNum(SMSCNum);
	strcpy(pMsg.SCA,SMSCNum);           // ����Ϣ�������ĺ���(SMSC��ַ)  

	strcpy(pMsg.TP_UD,SMSContent);      // ԭʼ�û���Ϣ(����ǰ�������TP-UD)

    if (HandFree==true)                 //����ѡ��
	{
		if (IsUnicode(pMsg.TP_UD))
			pMsg.TP_DCS=GSM_MUCS2;
		else
			pMsg.TP_DCS=GSM_M7BIT;
	}
	else
	{
		if (IsUnicode(pMsg.TP_UD))
			pMsg.TP_DCS=GSM_UCS2;       // �û���Ϣ���뷽ʽ(TP-DCS)  GSM_7BIT;GSM_8BIT;GSM_UCS2;
		else
			pMsg.TP_DCS=GSM_7BIT;
	}

	pMsg.TP_PID =0;                     // �û���ϢЭ���ʶ(TP-PID)
	
	if (ReportStatus==true)             //����״̬�ظ� 0x11 ������ 0x31 ����
		pMsg.TP_ST=0x31;
	else
		pMsg.TP_ST=0x11;

	pMsg.TP_VT=ValidTime[iValidTime];                    //��Ч�� 0x00 ����� 0xFF ���ֵ
	
	strcpy(pMsg.TPA ,TargetNum);        // Ŀ������ظ�����(TP-DA��TP-RA)


    BOOL IsSend=gsmSendMessage(&pMsg);
	
	if (IsSend==TRUE)
		return true;
	else
		return false;

}

bool GetMobileInfo(char NameOfManu[20], char NameOfTele[20], char VerOfSW[40], char IMEI[20], char BatteryCharge[5], char SignalQuality[5], char OwnTeleNum[20])
{
	char Temp[100];
	int i;
	bool IsGet=false;

	IsGet=GetOneLine(at.CGMI,NameOfManu);
	if (IsGet==false) return IsGet;

	IsGet=GetOneLine(at.CGMM,NameOfTele);
    if (IsGet==false) return IsGet;

	IsGet=GetOneLine(at.CGMR,VerOfSW);
    if (IsGet==false) return IsGet;

	IsGet=GetOneLine(at.CGSN,IMEI);
    if (IsGet==false) return IsGet;

	IsGet=GetOneLine(at.CBC, Temp);
	if (IsGet==false) return IsGet;
    strcpy(BatteryCharge,&Temp[8]);
    

	IsGet=GetOneLine(at.CSQ,Temp);
	if (IsGet==false) return IsGet;
	for(i=6; i<=8; i++)
		if(Temp[i]==',') break;
	strncpy(SignalQuality,&Temp[6],i-6);
	SignalQuality[i-6]='\0';
	i=atoi(SignalQuality);
	_itoa(2*i+(-113), SignalQuality,10);
    

	IsGet=GetOneLine(at.CNUM,Temp);
	if (IsGet==false) return IsGet;
	if (strcmp(Temp,"OK")==0) return true;
    for(i=0; i<=100; i++)
		if(Temp[i]==',') break;
	for(int j=i+2; j<=100; j++)
		if(Temp[j]=='"') break;
	strncpy(OwnTeleNum,&Temp[i+2],j-i-2);
	OwnTeleNum[j-i-2]='\0';

	return true;
}

bool GetSIMInfo(char NumOfSIM[25], char IMSI[20], char SMSCNum[20])
{
    char Temp[100];
	bool IsGet=false;

	if (MobileType==1)
	{
		IsGet=GetOneLine(at.SCID,Temp);
		if (IsGet==false) return IsGet;
		strcpy(NumOfSIM,&Temp[7]);
	}
	else
		strcpy(NumOfSIM,"");

    IsGet=GetOneLine(at.CIMI,IMSI);
    if (IsGet==false) return IsGet;

	IsGet=GetSMSCNum(SMSCNum);
	if (IsGet==false) return IsGet;

	return true;
}

bool GetMETime(char Time[25])
{
	char Temp[30];
	bool IsGet=false;

	IsGet=GetOneLine(at.CCLK1,Temp);
	if (IsGet==false) return IsGet;
	strcpy(Time,&Temp[8]);
	Time[strlen(Time)-1]='\0';

	return true;
}

bool SetMETime(char Time[25])
{
	char cmd[30];
	char Temp[30];
	bool IsGet=false;

	sprintf(cmd,at.CCLK2,Time);
	IsGet=GetOneLine(cmd,Temp);
    if (IsGet==false) return IsGet;

	return true;
}

int SetSMSMemoGetSMSNum(char Type[5])
{
	char cmd[30];
	char Temp[30];
	bool IsGet=false;

	sprintf(cmd,at.CPMS,Type,Type);
	IsGet=GetOneLine(cmd,Temp);
    if (IsGet==false) return -1;

    int nb;
	return atoi(GetChar(Temp, ' ', ',', nb));

}

bool ListShortMessage(RECSMS *pSMS, int j)
{
	char cmd[30];

	int size=j*400;

	char* temp;
	temp=new char[size];
	temp[0]='\0';
    char* temp2=temp;

	char* ptr;

    
	sprintf(cmd,at.CMGL,4);    // AT+CMGL=4

	if (IsMultiATOK(cmd,temp2)==false) 
	{
		delete[] temp;
		return false;
	}
	

	for (int i=0; i<j; i++)
	{
		int nb;
        pSMS[i].Index=atoi(GetChar(temp2, ' ', ',', nb));
		pSMS[i].Status=atoi(GetChar(temp2, ',', ',', nb));
		temp2=temp2+nb;
		ptr=GetChar(temp2,'\n','\r', nb);
		temp2=temp2+nb;

		SM_PARAM pMsg;
		memset(&pMsg, 0, sizeof(SM_PARAM));

		gsmDecodePdu(pSMS[i].Status, ptr, &pMsg);
		
		strcpy(pSMS[i].TeleNum, pMsg.TPA);
		strcpy(pSMS[i].Message, pMsg.TP_UD);
		memcpy(pSMS[i].MessageSign, pMsg.TP_UDS,7);

		if (pSMS[i].Status==0 || pSMS[i].Status==1)
		strcpy(pSMS[i].Time, pMsg.TP_SCTS);
		else
		{
			unsigned char ValidTime[6]={0x00, 0x0B, 0x8F, 0xA7, 0xC4, 0xFF};
			
			if ((pMsg.TP_DCS & 0x10) != 0)
				pSMS[i].HandFree=true;
			else
				pSMS[i].HandFree=false; 

			if ((pMsg.TP_ST & 0x20) != 0)  
				pSMS[i].ReportStatus=true;
			else
				pSMS[i].ReportStatus=false;     //0x11

			for (int iValidTime=0; iValidTime<6; iValidTime++)
	        if (pMsg.TP_VT==ValidTime[iValidTime]) 
				pSMS[i].ValidTime=iValidTime;  //��Ч�� 0x00 ����� 0xFF ���ֵ
		}
		

	}

    delete[] temp;
	return true;

}

int SaveShortMessage(char SMSContent[161], char TargetNum[20], bool ReportStatus, int iValidTime, bool HandFree, int Status)
{

	SM_PARAM pMsg;
	::memset(&pMsg, 0, sizeof(SM_PARAM));
	char SMSCNum[20];
	unsigned char ValidTime[6]={0x00, 0x0B, 0x8F, 0xA7, 0xC4, 0xFF};


	GetSMSCNum(SMSCNum);
	strcpy(pMsg.SCA,SMSCNum);           // ����Ϣ�������ĺ���(SMSC��ַ)  

	strcpy(pMsg.TP_UD,SMSContent);      // ԭʼ�û���Ϣ(����ǰ�������TP-UD)

    if (HandFree==true)                 //����ѡ��
	{
		if (IsUnicode(pMsg.TP_UD))
			pMsg.TP_DCS=GSM_MUCS2;
		else
			pMsg.TP_DCS=GSM_M7BIT;
	}
	else
	{
		if (IsUnicode(pMsg.TP_UD))
			pMsg.TP_DCS=GSM_UCS2;       // �û���Ϣ���뷽ʽ(TP-DCS)  GSM_7BIT;GSM_8BIT;GSM_UCS2;
		else
			pMsg.TP_DCS=GSM_7BIT;
	}

	pMsg.TP_PID =0;                     // �û���ϢЭ���ʶ(TP-PID)
	
	if (ReportStatus==true)             //����״̬�ظ� 0x11 ������ 0x31 ����
		pMsg.TP_ST=0x31;
	else
		pMsg.TP_ST=0x11;

	pMsg.TP_VT=ValidTime[iValidTime];                    //��Ч�� 0x00 ����� 0xFF ���ֵ
	
	strcpy(pMsg.TPA ,TargetNum);        // Ŀ������ظ�����(TP-DA��TP-RA)


	int Index=gsmSaveMessage(&pMsg, Status);

	return Index;

}

bool SendUnSent( int Index )
{

	char cmd[30];
    char ans[100];
	memset(ans,0,100);

	sprintf(cmd,at.CMSS,Index);

	if (IsMultiATOK(cmd,ans)==true)
		return true;
	else
		return false;

}

int GetSMSSpace( char Type[5] )
{
	char cmd[30];
	char Temp[30];
	bool IsGet=false;

	sprintf(cmd,at.CPMS2,Type);
	IsGet=GetOneLine(cmd,Temp);
    if (IsGet==false) return -1;

    int nb;
	return atoi(GetChar(Temp, ',', ',', nb));
}

bool DeleteShortMessage( int Index )
{
	if(gsmDeleteMessage(Index))
		return true;
	else
		return false;
}

bool SetMobileEncodeFormat( char Type[20])
{
	char cmd[30];
	char ans[30];

	memset(ans,0,30);

	sprintf(cmd,at.CSCS,Type);

	if (IsMultiATOK(cmd,ans)==true)
		return true;

	return false;

}

bool SetTeleBookMemo(char Type[5])
{
	char cmd[30];
	char ans[30];

	memset(ans,0,30);

	sprintf(cmd,at.CPBS1,Type);

	if (IsMultiATOK(cmd,ans)==true)
		return true;

	return false;
}

int GetTeleBookNum()
{
	if (MobileType==1 || MobileType==3)
	{
		char temp[30];
		int nb;
		if(GetOneLine(at.CPBS2, temp))
			return atoi(GetChar(temp,',',',',nb));
		return -1;
	}

	if (MobileType==2)
	{
		char cmd[30];
		int i=GetTeleBookSpace();
		char *ans=new char[50*i];
		memset(ans,0,50*i);
		sprintf(cmd,at.CPBR2,1,i);
		if (IsMultiATOK(cmd,ans)==true)
		{
			i=0;
			char* pdest;
			for (int j=0; ; j=j+6)
			{
				pdest=strstr(&ans[j],"+CPBR:");
				if (pdest==NULL) break;
				j=pdest-ans;
				i++;
			}
			delete[] ans;
			return i;
		}
		
		return -1;
	}
	return -1;
}

int GetTeleBookSpace()
{
		char temp[30];
		if(GetOneLine(at.CPBR3, temp))
		{   
			int nb;
			return atoi(GetChar(temp,'-',')',nb));
		}
		return -1;
}

bool ReadTeleBook( int Index, char TeleNum[20], char Name[14], int& Vip )
{
	char cmd[30];
	char ans[80];

	memset(ans,0,80);

	sprintf(cmd,at.CPBR,Index);

	if (IsMultiATOK(cmd,ans)==true)
	{
		int nb=-1;
		char temp[100];
		unsigned char buf[256];
		strcpy(TeleNum,GetChar(ans,'\"','\"',nb));
		if (nb==-1) return false;
		strcpy(temp,GetChar(&ans[nb+1],'\"','\"',nb));

		if(strstr(temp,"0021")!=NULL)
		{
			temp[strlen(temp)-4]='\0';
			Vip=1;
		}
		else
			Vip=0;

		if (MobileType==1 || MobileType==3)
		{
			// UCS2����
			int nDstLength = gsmString2Bytes(temp, buf, strlen(temp));			// ��ʽת��
			gsmDecodeUcs2(buf,Name, nDstLength);	// ת����TP-DU
		}
		
		if (MobileType==2)
		{
			// UTF-8����
			UTF8ToMutiByte(temp,strlen(temp),Name,100);
		}
        
		return true;

	}

	return false;
}

bool WriteTeleBook( char TeleNum[20], char Name[14], int Vip)
{
	char TeleNumR[22];
	strcpy(TeleNumR,"\"");
	strcat(TeleNumR,TeleNum);
	strcat(TeleNumR,"\"");

	if (MobileType==3) Vip=0;

    char NameR[100];
    char pDst[100];
	if (MobileType==1 || MobileType==3)
	{
		unsigned char buf[256];
		int nLength=gsmEncodeUcs2(Name, buf, strlen(Name));
		gsmBytes2String(buf, pDst, nLength);		// ת���ö����ݵ�Ŀ��PDU��
		strcpy(NameR,"\"");
		strcat(NameR,pDst);
		if (Vip==1) strcat(NameR,"0021");
		strcat(NameR,"\"");
	}
	
	if (MobileType==2)
	{
		MutiByteToUTF8(Name,strlen(Name),pDst,100);
		strcpy(NameR,"\"");
		strcat(NameR,pDst);
//		if (Vip==1) strcat(NameR,"0021");
		strcat(NameR,"\"");
	}
	
	char cmd[30];
	char ans[100];
    memset(ans,0,100);

	if (MobileType==1 || MobileType==2)
		sprintf(cmd,at.CPBW1,TeleNumR,129,NameR);
	if (MobileType==3)
		sprintf(cmd,at.CPBW3,GetMaxTeleIndex(),TeleNumR,129,NameR);

	SetMaxTeleIndex(GetMaxTeleIndex()+1);

	if (IsMultiATOK(cmd,ans)==true)
		return true;

	return false;
}


bool DeleteTeleBook( int Index )
{
	char cmd[30];
	char ans[30];
    memset(ans,0,30);

	sprintf(cmd,at.CPBW2,Index);

	if (Index==GetMaxTeleIndex())
		SetMaxTeleIndex(GetMaxTeleIndex()-1);

	if (IsMultiATOK(cmd,ans)==true)
		return true;

	return false;
}

bool ModifyTeleBook( int Index, char TeleNum[20], char Name[14], int Vip)
{
	char TeleNumR[22];
	strcpy(TeleNumR,"\"");
	strcat(TeleNumR,TeleNum);
	strcat(TeleNumR,"\"");

	char NameR[100];
    char pDst[100];
	if (MobileType==1 || MobileType==3)
	{
		unsigned char buf[256];
		int nLength=gsmEncodeUcs2(Name, buf, strlen(Name));
		gsmBytes2String(buf, pDst, nLength);		// ת���ö����ݵ�Ŀ��PDU��
		strcpy(NameR,"\"");
		strcat(NameR,pDst);
		if (Vip==1) strcat(NameR,"0021");
		strcat(NameR,"\"");
	}
	
	if (MobileType==2)
	{
		MutiByteToUTF8(Name,strlen(Name),pDst,100);
		strcpy(NameR,"\"");
		strcat(NameR,pDst);
//		if (Vip==1) strcat(NameR,"0021");
		strcat(NameR,"\"");
	}


	
	char cmd[30];
	char ans[100];
    memset(ans,0,100);

	sprintf(cmd,at.CPBW3,Index,TeleNumR,129,NameR);

	if (IsMultiATOK(cmd,ans)==true)
		return true;

	return false;
}




bool IsUnicode(char SMSContent[161])
{

		int i,j;

		unsigned char c;

        i=strlen(SMSContent);

        for (j=0;j<=i;j++)
		{
			c=SMSContent[j];

			//���ĺ���ͳ��  ͳ�Ʊ��� Cnumber
			
			if(c>=128)
			{
				return true;
			}
		
		}

		return false;
}

bool GetSMSCNum(char SMSCNum[20])
{
	char ans[100];
    bool IsGet=false;

	if(SetMobileEncodeFormat("\"GSM\"")==false) return false;

	IsGet=GetOneLine(at.CSCA,ans);
	if (IsGet==false) return false;

	for(int i=0; i<100; i++)
	{
		if (ans[i]==34)
		{
			for ( int j=i+1; j<100; j++)
				if (ans[j]==34) break;

				ans[j]='\0';

				if (j-i-1>19) return false;

				//ȥ��+��
				if (ans[i+1]=='+')
					strcpy(SMSCNum,&ans[i+2]);
				else
					strcpy(SMSCNum,&ans[i+1]);

			break;
		}
	}
	return true;
}

bool GetOneLine(char ATCommand[30], char Value[50])
{
	char Temp[100];
	memset(Temp,0,100);

	int iStrlen=strlen(ATCommand)+1;

    if(IsMultiATOK(ATCommand, Temp)==true)
	{   
		int nb;
		strcpy(Value,GetChar(Temp,'\n','\r',nb));
		Value[nb-iStrlen-1]='\0';
		return true;
	}
	else
		return false;
}

char* GetLine(char* Data, int i)
{
	int na=0;
	int nb=0;
	for(int j=0; j<i-1; j++)
	{
		GetChar(&Data[na],'\n','\r',nb);
		na=nb+na+1;
	}
	return GetChar(&Data[na],'\n','\r',nb);
}
