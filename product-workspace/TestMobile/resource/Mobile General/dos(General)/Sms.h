// Comm.h: interface for the CComm class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(SMS_H__)
#define SMS_H__

// 用户信息编码方式
#define GSM_7BIT		0
#define GSM_8BIT		4
#define GSM_UCS2		8
#define GSM_M7BIT       16
#define GSM_MUCS2       24

// 短消息参数结构，编码/解码共用
// 其中，字符串以0结尾
typedef struct {
	char SCA[20];			// 短消息服务中心号码(SMSC地址)
	char TPA[20];			// 目标号码或回复号码(TP-DA或TP-RA)
	char TP_PID;			// 用户信息协议标识(TP-PID)
	char TP_DCS;			// 用户信息编码方式(TP-DCS)
	char TP_SCTS[16];		// 服务时间戳字符串(TP_SCTS), 接收时用到
	char TP_UD[161];		// 原始用户信息(编码前或解码后的TP-UD)
	unsigned char TP_UDS[7];// 连锁短消息标志
	char index;				// 短消息序号，在读取时用到
	unsigned char TP_ST;    //请求状态回复 0x11 不请求 0x31 请求
	unsigned char TP_VT;    //有效期 0x00 五分钟 0xFF 最大值
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
