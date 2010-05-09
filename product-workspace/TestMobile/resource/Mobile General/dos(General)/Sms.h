// Comm.h: interface for the CComm class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(SMS_H__)
#define SMS_H__

// �û���Ϣ���뷽ʽ
#define GSM_7BIT		0
#define GSM_8BIT		4
#define GSM_UCS2		8
#define GSM_M7BIT       16
#define GSM_MUCS2       24

// ����Ϣ�����ṹ������/���빲��
// ���У��ַ�����0��β
typedef struct {
	char SCA[20];			// ����Ϣ�������ĺ���(SMSC��ַ)
	char TPA[20];			// Ŀ������ظ�����(TP-DA��TP-RA)
	char TP_PID;			// �û���ϢЭ���ʶ(TP-PID)
	char TP_DCS;			// �û���Ϣ���뷽ʽ(TP-DCS)
	char TP_SCTS[16];		// ����ʱ����ַ���(TP_SCTS), ����ʱ�õ�
	char TP_UD[161];		// ԭʼ�û���Ϣ(����ǰ�������TP-UD)
	unsigned char TP_UDS[7];// ��������Ϣ��־
	char index;				// ����Ϣ��ţ��ڶ�ȡʱ�õ�
	unsigned char TP_ST;    //����״̬�ظ� 0x11 ������ 0x31 ����
	unsigned char TP_VT;    //��Ч�� 0x00 ����� 0xFF ���ֵ
} SM_PARAM;

int gsmBytes2String(const unsigned char* pSrc, char* pDst, int nSrcLength);
int gsmString2Bytes(const char* pSrc, unsigned char* pDst, int nSrcLength);
int gsmEncode7bit(const char* pSrc, unsigned char* pDst, int nSrcLength);
int gsmDecode7bit(const unsigned char* pSrc, char* pDst, int nSrcLength);
int gsmEncode8bit(const char* pSrc, unsigned char* pDst, int nSrcLength);
int gsmDecode8bit(const unsigned char* pSrc, char* pDst, int nSrcLength);
int gsmEncodeUcs2(const char* pSrc, unsigned char* pDst, int nSrcLength);
int gsmDecodeUcs2(const unsigned char* pSrc, char* pDst, int nSrcLength);
int gsmInvertNumbers(const char* pSrc, char* pDst, int nSrcLength);
int gsmSerializeNumbers(const char* pSrc, char* pDst, int nSrcLength);
int gsmEncodePdu(const SM_PARAM* pSrc, char* pDst);
int gsmDecodePdu(int Status, const char* pSrc, SM_PARAM* pDst);

BOOL gsmSendMessage(const SM_PARAM* pSrc);
int gsmReadMessage(SM_PARAM* pMsg);
BOOL gsmDeleteMessage(const int index);
int gsmSaveMessage(const SM_PARAM* pSrc, int Status);

bool IsMultiATOK(char ATCommand[], char* Value);
bool IsMultiChar(char ATCommand[], char* Value, char nCompare[]);
char* GetChar(char* ab, char a, char b, int& nb);

void QuotedPrint(unsigned char* sour,int nCount);
void UnQuotedPrint(unsigned char* sour,int nCount);
int  UTF8ToMutiByte(const char *src,int slen,char *dest,int dlen);
int  MutiByteToUTF8(const char *src,int slen,char *dest,int dlen);

#endif // !defined(SMS_H__)
