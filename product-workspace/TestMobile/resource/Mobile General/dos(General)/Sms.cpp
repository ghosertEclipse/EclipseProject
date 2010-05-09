#include "windows.h"
//#include "stdafx.h"
#include "stdio.h"
#include "Sms.h"
#include "Comm.h"
#include "Type.h"

extern ATCOMMANDSET at;
extern int MobileType;              // 1-SIEMENS; 2-SONY ERICSSON; 3-NOKIA

// �ɴ�ӡ�ַ���ת��Ϊ�ֽ�����
// �磺"C8329BFD0E01" --> {0xC8, 0x32, 0x9B, 0xFD, 0x0E, 0x01}
// pSrc: Դ�ַ���ָ��
// pDst: Ŀ������ָ��
// nSrcLength: Դ�ַ�������
// ����: Ŀ�����ݳ���
int gsmString2Bytes(const char* pSrc, unsigned char* pDst, int nSrcLength)
{
	for(int i=0; i<nSrcLength; i+=2)
	{
		// �����4λ
		if(*pSrc>='0' && *pSrc<='9')
		{
			*pDst = (*pSrc - '0') << 4;
		}
		else
		{
			*pDst = (*pSrc - 'A' + 10) << 4;
		}

		pSrc++;

		// �����4λ
		if(*pSrc>='0' && *pSrc<='9')
		{
			*pDst |= *pSrc - '0';
		}
		else
		{
			*pDst |= *pSrc - 'A' + 10;
		}

		pSrc++;
		pDst++;
	}

	// ����Ŀ�����ݳ���
	return nSrcLength / 2;
}

// �ֽ�����ת��Ϊ�ɴ�ӡ�ַ���
// �磺{0xC8, 0x32, 0x9B, 0xFD, 0x0E, 0x01} --> "C8329BFD0E01" 
// pSrc: Դ����ָ��
// pDst: Ŀ���ַ���ָ��
// nSrcLength: Դ���ݳ���
// ����: Ŀ���ַ�������
int gsmBytes2String(const unsigned char* pSrc, char* pDst, int nSrcLength)
{
	const char tab[]="0123456789ABCDEF";	// 0x0-0xf���ַ����ұ�

	for(int i=0; i<nSrcLength; i++)
	{
		*pDst++ = tab[*pSrc >> 4];		// �����4λ
		*pDst++ = tab[*pSrc & 0x0f];	// �����4λ
		pSrc++;
	}

	// ����ַ����Ӹ�������
	*pDst = '\0';

	// ����Ŀ���ַ�������
	return nSrcLength * 2;
}

// 7bit����
// pSrc: Դ�ַ���ָ��
// pDst: Ŀ����봮ָ��
// nSrcLength: Դ�ַ�������
// ����: Ŀ����봮����
int gsmEncode7bit(const char* pSrc, unsigned char* pDst, int nSrcLength)
{
	int nSrc;		// Դ�ַ����ļ���ֵ
	int nDst;		// Ŀ����봮�ļ���ֵ
	int nChar;		// ��ǰ���ڴ���������ַ��ֽڵ���ţ���Χ��0-7
	unsigned char nLeft;	// ��һ�ֽڲ��������

	// ����ֵ��ʼ��
	nSrc = 0;
	nDst = 0;

	// ��Դ��ÿ8���ֽڷ�Ϊһ�飬ѹ����7���ֽ�
	// ѭ���ô�����̣�ֱ��Դ����������
	// ������鲻��8�ֽڣ�Ҳ����ȷ����
	while(nSrc<nSrcLength)
	{
		// ȡԴ�ַ����ļ���ֵ�����3λ
		nChar = nSrc & 7;

		// ����Դ����ÿ���ֽ�
		if(nChar == 0)
		{
			// ���ڵ�һ���ֽڣ�ֻ�Ǳ�����������������һ���ֽ�ʱʹ��
			nLeft = *pSrc;
		}
		else
		{
			// ���������ֽڣ������ұ߲��������������ӣ��õ�һ��Ŀ������ֽ�
			*pDst = (*pSrc << (8-nChar)) | nLeft;

			// �����ֽ�ʣ�µ���߲��֣���Ϊ�������ݱ�������
			nLeft = *pSrc >> nChar;

			// �޸�Ŀ�괮��ָ��ͼ���ֵ
			pDst++;
			nDst++;
		}

		// �޸�Դ����ָ��ͼ���ֵ
		pSrc++;
		nSrc++;
	}

	// ����Ŀ�괮����
	return nDst;
}

// 7bit����
// pSrc: Դ���봮ָ��
// pDst: Ŀ���ַ���ָ��
// nSrcLength: Դ���봮����
// ����: Ŀ���ַ�������
int gsmDecode7bit(const unsigned char* pSrc, char* pDst, int nSrcLength)
{
	int nSrc;		// Դ�ַ����ļ���ֵ
	int nDst;		// Ŀ����봮�ļ���ֵ
	int nByte;		// ��ǰ���ڴ���������ֽڵ���ţ���Χ��0-6
	unsigned char nLeft;	// ��һ�ֽڲ��������

	// ����ֵ��ʼ��
	nSrc = 0;
	nDst = 0;
	
	// �����ֽ���źͲ������ݳ�ʼ��
	nByte = 0;
	nLeft = 0;

	// ��Դ����ÿ7���ֽڷ�Ϊһ�飬��ѹ����8���ֽ�
	// ѭ���ô�����̣�ֱ��Դ���ݱ�������
	// ������鲻��7�ֽڣ�Ҳ����ȷ����
	while(nSrc<nSrcLength)
	{
		// ��Դ�ֽ��ұ߲��������������ӣ�ȥ�����λ���õ�һ��Ŀ������ֽ�
		*pDst = ((*pSrc << nByte) | nLeft) & 0x7f;

		// �����ֽ�ʣ�µ���߲��֣���Ϊ�������ݱ�������
		nLeft = *pSrc >> (7-nByte);

		// �޸�Ŀ�괮��ָ��ͼ���ֵ
		pDst++;
		nDst++;

		// �޸��ֽڼ���ֵ
		nByte++;

		// ����һ������һ���ֽ�
		if(nByte == 7)
		{
			// ����õ�һ��Ŀ������ֽ�
			*pDst = nLeft;

			// �޸�Ŀ�괮��ָ��ͼ���ֵ
			pDst++;
			nDst++;

			// �����ֽ���źͲ������ݳ�ʼ��
			nByte = 0;
			nLeft = 0;
		}

		// �޸�Դ����ָ��ͼ���ֵ
		pSrc++;
		nSrc++;
	}

	// ����ַ����Ӹ�������
	*pDst = '\0';

	// ����Ŀ�괮����
	return nDst;
}

// 8bit����
// pSrc: Դ�ַ���ָ��
// pDst: Ŀ����봮ָ��
// nSrcLength: Դ�ַ�������
// ����: Ŀ����봮����
int gsmEncode8bit(const char* pSrc, unsigned char* pDst, int nSrcLength)
{
	// �򵥸���
	memcpy(pDst, pSrc, nSrcLength);

	return nSrcLength;
}

// 8bit����
// pSrc: Դ���봮ָ��
// pDst: Ŀ���ַ���ָ��
// nSrcLength: Դ���봮����
// ����: Ŀ���ַ�������
int gsmDecode8bit(const unsigned char* pSrc, char* pDst, int nSrcLength)
{
	// �򵥸���
	memcpy(pDst, pSrc, nSrcLength);

	// ����ַ����Ӹ�������
	*pDst = '\0';

	return nSrcLength;
}

// UCS2����
// pSrc: Դ�ַ���ָ��
// pDst: Ŀ����봮ָ��
// nSrcLength: Դ�ַ�������
// ����: Ŀ����봮����
int gsmEncodeUcs2(const char* pSrc, unsigned char* pDst, int nSrcLength)
{
	int nDstLength;		// UNICODE���ַ���Ŀ
	WCHAR wchar[128];	// UNICODE��������

	// �ַ���-->UNICODE��
	nDstLength = ::MultiByteToWideChar(CP_ACP, 0, pSrc, nSrcLength, wchar, 128);

	// �ߵ��ֽڶԵ������
	for(int i=0; i<nDstLength; i++)
	{
		*pDst++ = wchar[i] >> 8;		// �������λ�ֽ�
		*pDst++ = wchar[i] & 0xff;		// �������λ�ֽ�
	}

	// ����Ŀ����봮����
	return nDstLength * 2;
}

// UCS2����
// pSrc: Դ���봮ָ��
// pDst: Ŀ���ַ���ָ��
// nSrcLength: Դ���봮����
// ����: Ŀ���ַ�������
int gsmDecodeUcs2(const unsigned char* pSrc, char* pDst, int nSrcLength)
{
	int nDstLength;		// UNICODE���ַ���Ŀ
	WCHAR wchar[128];	// UNICODE��������

	// �ߵ��ֽڶԵ���ƴ��UNICODE
	for(int i=0; i<nSrcLength/2; i++)
	{
		wchar[i] = *pSrc++ << 8;	// �ȸ�λ�ֽ�
		wchar[i] |= *pSrc++;		// ���λ�ֽ�
	}

	// UNICODE��-->�ַ���
	nDstLength = ::WideCharToMultiByte(CP_ACP, 0, wchar, nSrcLength/2, pDst, 160, NULL, NULL);

	// ����ַ����Ӹ�������
	pDst[nDstLength] = '\0';

	// ����Ŀ���ַ�������
	return nDstLength;
}

// ����˳����ַ���ת��Ϊ�����ߵ����ַ�����������Ϊ��������'F'�ճ�ż��
// �磺"8613851872468" --> "683158812764F8"
// pSrc: Դ�ַ���ָ��
// pDst: Ŀ���ַ���ָ��
// nSrcLength: Դ�ַ�������
// ����: Ŀ���ַ�������
int gsmInvertNumbers(const char* pSrc, char* pDst, int nSrcLength)
{
	int nDstLength;		// Ŀ���ַ�������
	char ch;			// ���ڱ���һ���ַ�

	// ���ƴ�����
	nDstLength = nSrcLength;

	// �����ߵ�
	for(int i=0; i<nSrcLength;i+=2)
	{
		ch = *pSrc++;		// �����ȳ��ֵ��ַ�
		*pDst++ = *pSrc++;	// ���ƺ���ֵ��ַ�
		*pDst++ = ch;		// �����ȳ��ֵ��ַ�
	}

	// Դ��������������
	if(nSrcLength & 1)
	{
		*(pDst-2) = 'F';	// ��'F'
		nDstLength++;		// Ŀ�괮���ȼ�1
	}

	// ����ַ����Ӹ�������
	*pDst = '\0';

	// ����Ŀ���ַ�������
	return nDstLength;
}

// �����ߵ����ַ���ת��Ϊ����˳����ַ���
// �磺"683158812764F8" --> "8613851872468"
// pSrc: Դ�ַ���ָ��
// pDst: Ŀ���ַ���ָ��
// nSrcLength: Դ�ַ�������
// ����: Ŀ���ַ�������
int gsmSerializeNumbers(const char* pSrc, char* pDst, int nSrcLength)
{
	int nDstLength;		// Ŀ���ַ�������
	char ch;			// ���ڱ���һ���ַ�

	// ���ƴ�����
	nDstLength = nSrcLength;

	// �����ߵ�
	for(int i=0; i<nSrcLength;i+=2)
	{
		ch = *pSrc++;		// �����ȳ��ֵ��ַ�
		*pDst++ = *pSrc++;	// ���ƺ���ֵ��ַ�
		*pDst++ = ch;		// �����ȳ��ֵ��ַ�
	}

	// �����ַ���'F'��
	if(*(pDst-1) == 'F')
	{
		pDst--;
		nDstLength--;		// Ŀ���ַ������ȼ�1
	}

	// ����ַ����Ӹ�������
	*pDst = '\0';

	// ����Ŀ���ַ�������
	return nDstLength;
}

// PDU���룬���ڱ��ơ����Ͷ���Ϣ
// pSrc: ԴPDU����ָ��
// pDst: Ŀ��PDU��ָ��
// ����: Ŀ��PDU������
int gsmEncodePdu(const SM_PARAM* pSrc, char* pDst)
{
	int nLength;			// �ڲ��õĴ�����
	int nDstLength;			// Ŀ��PDU������
	unsigned char buf[256];	// �ڲ��õĻ�����

	// SMSC��ַ��Ϣ��
	nLength = strlen(pSrc->SCA);	// SMSC��ַ�ַ����ĳ���	
	if (nLength==0)
	{
		buf[0]=(char)(0);
        nDstLength = gsmBytes2String(buf, pDst, 1);		// ת��2���ֽڵ�Ŀ��PDU��
	}
	else
	{
		buf[0] = (char)((nLength & 1) == 0 ? nLength : nLength + 1) / 2 + 1;	// SMSC��ַ��Ϣ����
		buf[1] = 0x91;		// �̶�: �ù��ʸ�ʽ����
		nDstLength = gsmBytes2String(buf, pDst, 2);		// ת��2���ֽڵ�Ŀ��PDU��
	}
	nDstLength += gsmInvertNumbers(pSrc->SCA, &pDst[nDstLength], nLength);	// ת��SMSC���뵽Ŀ��PDU��

	// TPDU�λ���������Ŀ���ַ��
	nLength = strlen(pSrc->TPA);	// TP-DA��ַ�ַ����ĳ���
	buf[0] = pSrc->TP_ST;			// �Ƿ��Ͷ���(TP-MTI=01)��TP-VP����Ը�ʽ(TP-VPF=10)
	buf[1] = 0;						// TP-MR=0
	buf[2] = (char)nLength;			// Ŀ���ַ���ָ���(TP-DA��ַ�ַ�����ʵ����)
	buf[3] = 0x91;					// �̶�: �ù��ʸ�ʽ����
	nDstLength += gsmBytes2String(buf, &pDst[nDstLength], 4);		// ת��4���ֽڵ�Ŀ��PDU��
	nDstLength += gsmInvertNumbers(pSrc->TPA, &pDst[nDstLength], nLength);	// ת��TP-DA��Ŀ��PDU��

	// TPDU��Э���ʶ�����뷽ʽ���û���Ϣ��
	nLength = strlen(pSrc->TP_UD);	// �û���Ϣ�ַ����ĳ���
	buf[0] = pSrc->TP_PID;			// Э���ʶ(TP-PID)
	buf[1] = pSrc->TP_DCS;			// �û���Ϣ���뷽ʽ(TP-DCS)
	buf[2] = pSrc->TP_VT;			// ��Ч��(TP-VP)Ϊ5���ӻ����ֵ

	if(pSrc->TP_DCS == GSM_7BIT || pSrc->TP_DCS == GSM_M7BIT)	
	{
		// 7-bit���뷽ʽ
		buf[3] = nLength;			// ����ǰ����
		nLength = gsmEncode7bit(pSrc->TP_UD, &buf[4], nLength+1) + 4;	// ת��TP-DA��Ŀ��PDU��
	}
	else if(pSrc->TP_DCS == GSM_UCS2 || pSrc->TP_DCS==GSM_MUCS2)
	{
		// UCS2���뷽ʽ
		buf[3] = gsmEncodeUcs2(pSrc->TP_UD, &buf[4], nLength);	// ת��TP-DA��Ŀ��PDU��
		nLength = buf[3] + 4;		// nLength���ڸö����ݳ���
	}
	else
	{
		// 8-bit���뷽ʽ
		buf[3] = gsmEncode8bit(pSrc->TP_UD, &buf[4], nLength);	// ת��TP-DA��Ŀ��PDU��
		nLength = buf[3] + 4;		// nLength���ڸö����ݳ���
	}
	nDstLength += gsmBytes2String(buf, &pDst[nDstLength], nLength);		// ת���ö����ݵ�Ŀ��PDU��

	// ����Ŀ���ַ�������
	return nDstLength;
}

// PDU���룬���ڽ��ա��Ķ�����Ϣ
// pSrc: ԴPDU��ָ��
// pDst: Ŀ��PDU����ָ��
// ����: �û���Ϣ������
int gsmDecodePdu(int Status, const char* pSrc, SM_PARAM* pDst)
{
	int nDstLength;			// Ŀ��PDU������
	unsigned char tmp;		// �ڲ��õ���ʱ�ֽڱ���
	unsigned char buf[256];	// �ڲ��õĻ�����
	unsigned char Internet=0x00; // ���ʺ����ʽ��ʶ

	// SMSC��ַ��Ϣ��
	gsmString2Bytes(pSrc, &tmp, 2);	// ȡ����
	if (tmp==0)
		pSrc +=2;
	else
	{
		tmp = (tmp - 1) * 2;	// SMSC���봮����
		pSrc += 4;			// ָ����ƣ�������SMSC��ַ��ʽ
		gsmSerializeNumbers(pSrc, pDst->SCA, tmp);	// ת��SMSC���뵽Ŀ��PDU��
		pSrc += tmp;		// ָ�����
	}

	gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_ST, 2);	// ȡ״̬����(TP-ST)

	if (Status==2 || Status==3)//δ������Ϣ�ѷ�����Ϣ
	{
		pSrc += 4;		// ָ�����
		// �����ظ���ַ��ȡ�ظ���ַ��Ϣ
		gsmString2Bytes(pSrc, &tmp, 2);	// ȡ����
		if(tmp & 1) tmp += 1;	// ������ż��
		

		gsmString2Bytes(&pSrc[2], &Internet,2);
		pSrc += 4;			// ָ����ƣ������˻ظ���ַ(TP-RA)��ʽ
		if (Internet==0x91 ) 
		{
			memcpy(pDst->TPA,"+",1);
			gsmSerializeNumbers(pSrc, &pDst->TPA[1], tmp);	// ȡTP-RA����
		}
		else
			gsmSerializeNumbers(pSrc, pDst->TPA, tmp);	// ȡTP-RA����
		

		pSrc += tmp;		// ָ�����
		// TPDU��Э���ʶ�����뷽ʽ���û���Ϣ��
		gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_PID, 2);	// ȡЭ���ʶ(TP-PID)
		pSrc += 2;		// ָ�����
		gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_DCS, 2);	// ȡ���뷽ʽ(TP-DCS)
		pSrc += 2;		// ָ�����
		gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_VT, 2);	// ȡ��Чʱ��(TP-VT)
		pSrc += 2;		// ָ�����
		gsmString2Bytes(pSrc, &tmp, 2);	// �û���Ϣ����(TP-UDL)
		pSrc += 2;		// ָ�����

		if ((pDst->TP_ST & 0x40)!=0)  // ȥ��ͷ��Ϣ
		{
			gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_UDS , 14);	// ȡ��������Ϣ��־
			tmp=tmp-7;
			pSrc += 14;
		}

		if(pDst->TP_DCS == GSM_7BIT || pDst->TP_DCS == GSM_M7BIT)	
		{
			// 7-bit����
			nDstLength = gsmString2Bytes(pSrc, buf, tmp & 7 ? (int)tmp * 7 / 4 + 2 : (int)tmp * 7 / 4);	// ��ʽת��
			gsmDecode7bit(buf, pDst->TP_UD, nDstLength);	// ת����TP-DU
			nDstLength = tmp;
		}
		else if(pDst->TP_DCS == GSM_UCS2 || pDst->TP_DCS == GSM_MUCS2)
		{
			// UCS2����
			nDstLength = gsmString2Bytes(pSrc, buf, tmp * 2);			// ��ʽת��
			nDstLength = gsmDecodeUcs2(buf, pDst->TP_UD, nDstLength);	// ת����TP-DU
		}
		else
		{
			// 8-bit����
			nDstLength = gsmString2Bytes(pSrc, buf, tmp * 2);			// ��ʽת��
			nDstLength = gsmDecode8bit(buf, pDst->TP_UD, nDstLength);	// ת����TP-DU
		}
		
		// ����Ŀ���ַ�������
		return nDstLength;
		
	}

	if (pDst->TP_ST==6)                     //״̬�ظ��������ʽ
	{
		pSrc += 4;
		// �����ظ���ַ��ȡ�ظ���ַ��Ϣ
		gsmString2Bytes(pSrc, &tmp, 2);	// ȡ����
		if(tmp & 1) tmp += 1;	// ������ż��


		gsmString2Bytes(&pSrc[2], &Internet,2);
		pSrc += 4;			// ָ����ƣ������˻ظ���ַ(TP-RA)��ʽ
		if (Internet==0x91 ) 
		{
			memcpy(pDst->TPA,"+",1);
			gsmSerializeNumbers(pSrc, &pDst->TPA[1], tmp);	// ȡTP-RA����
		}
		else
			gsmSerializeNumbers(pSrc, pDst->TPA, tmp);	// ȡTP-RA����


		pSrc += tmp;		// ָ�����
		gsmSerializeNumbers(pSrc, pDst->TP_SCTS, 14);		// ����ʱ����ַ���(TP_SCTS) 
	    pSrc += 14;		// ָ�����
        gsmSerializeNumbers(pSrc, pDst->TP_UD, 14);		// ����ʱ����ַ���(TP_SCTS) 
		pSrc += 14;

		return 14;
	}

	pSrc += 2;		// ָ�����
	
	// �����ظ���ַ��ȡ�ظ���ַ��Ϣ
	gsmString2Bytes(pSrc, &tmp, 2);	// ȡ����
	if(tmp & 1) tmp += 1;	// ������ż��
	
	
	gsmString2Bytes(&pSrc[2], &Internet,2);
	pSrc += 4;			// ָ����ƣ������˻ظ���ַ(TP-RA)��ʽ
	if (Internet==0x91 ) 
	{
		memcpy(pDst->TPA,"+",1);
		gsmSerializeNumbers(pSrc, &pDst->TPA[1], tmp);	// ȡTP-RA����
	}
	else
		gsmSerializeNumbers(pSrc, pDst->TPA, tmp);	// ȡTP-RA����
	
	
	pSrc += tmp;		// ָ�����
	
	
	// TPDU��Э���ʶ�����뷽ʽ���û���Ϣ��
	gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_PID, 2);	// ȡЭ���ʶ(TP-PID)
	pSrc += 2;		// ָ�����
	gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_DCS, 2);	// ȡ���뷽ʽ(TP-DCS)
	pSrc += 2;		// ָ�����
	gsmSerializeNumbers(pSrc, pDst->TP_SCTS, 14);		// ����ʱ����ַ���(TP_SCTS) 
	pSrc += 14;		// ָ�����
	gsmString2Bytes(pSrc, &tmp, 2);	// �û���Ϣ����(TP-UDL)
	pSrc += 2;		// ָ�����

	if ((pDst->TP_ST & 0x40)!=0)  // ȥ��ͷ��Ϣ
	{
		gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_UDS , 14);	// ȡ��������Ϣ��־
		tmp=tmp-7;
		pSrc += 14;
	}

	if(pDst->TP_DCS == GSM_7BIT)	
	{
		// 7-bit����
		nDstLength = gsmString2Bytes(pSrc, buf, tmp & 7 ? (int)tmp * 7 / 4 + 2 : (int)tmp * 7 / 4);	// ��ʽת��
		gsmDecode7bit(buf, pDst->TP_UD, nDstLength);	// ת����TP-DU
		nDstLength = tmp;
	}
	else if(pDst->TP_DCS == GSM_UCS2)
	{
		// UCS2����
		nDstLength = gsmString2Bytes(pSrc, buf, tmp * 2);			// ��ʽת��
		nDstLength = gsmDecodeUcs2(buf, pDst->TP_UD, nDstLength);	// ת����TP-DU
	}
	else
	{
		// 8-bit����
		nDstLength = gsmString2Bytes(pSrc, buf, tmp * 2);			// ��ʽת��
		nDstLength = gsmDecode8bit(buf, pDst->TP_UD, nDstLength);	// ת����TP-DU
	}
	
	// ����Ŀ���ַ�������
	return nDstLength;
}


// ���Ͷ���Ϣ
// pSrc: ԴPDU����ָ��
BOOL gsmSendMessage(const SM_PARAM* pSrc)
{
	int nPduLength;		// PDU������
	unsigned char nSmscLength;	// SMSC������
	char cmd[128];		// ���
	char pdu[512];		// PDU��
	char ans[100];		// Ӧ��

	nPduLength = gsmEncodePdu(pSrc, pdu);	// ����PDU����������PDU��
	strcat(pdu, "\x01a");		// ��Ctrl-Z����

	gsmString2Bytes(pdu, &nSmscLength, 2);	// ȡPDU���е�SMSC��Ϣ����
	nSmscLength++;		// ���ϳ����ֽڱ���

	// �����еĳ��ȣ�������SMSC��Ϣ���ȣ��������ֽڼ�
	sprintf(cmd, at.CMGS, nPduLength / 2 - nSmscLength);	// ��������

//	TRACE("%s", cmd);
//	TRACE("%s\n", pdu);

	memset(ans,0,100);

	// �����ܷ��ҵ�"\r\n> "�����ɹ����
	if (IsMultiChar(cmd,ans,"\r\n> ")==true)
	{
		char temp[400];
		memset(temp,0,400);

//		Sleep(3500);
		IsMultiATOK(pdu, temp);
		return TRUE;
/*		if(IsMultiATOK(pdu, temp2)==false)    // �õ��϶��ش𣬼������PDU��
		{
			delete[] temp;
			return FALSE;
		}
		else
		{
			delete[] temp;
			return TRUE;
		}
*/
	}

	return FALSE;
}

// ��ȡ����Ϣ
// ��+CMGL����+CMGR����һ���Զ���ȫ������Ϣ
// pMsg: ����Ϣ�������������㹻��
// ����: ����Ϣ����
int gsmReadMessage(SM_PARAM* pMsg)
{
	int nLength;		// �����յ������ݳ���
	int nMsg;			// ����Ϣ����ֵ
	char* ptr;			// �ڲ��õ�����ָ��
	char cmd[16];		// ���
	char ans[1024];		// Ӧ��

	nMsg = 0;
	ptr = ans;

	sprintf(cmd, "AT+CMGL\r");	// ��������

	WriteComm(cmd, strlen(cmd));	// ������

	nLength = ReadComm(ans);	// ��Ӧ������

	// �����ܷ��ҵ�"+CMS ERROR"�����ɹ����
	if(nLength > 0 && strncmp(ans, "+CMS ERROR", 10) != 0)
	{
		// ѭ����ȡÿһ������Ϣ, ��"+CMGL:"��ͷ
		while((ptr = strstr(ptr, "+CMGL:")) != NULL)
		{
			ptr += 6;		// ����"+CMGL:"
			sscanf(ptr, "%d", &pMsg->index);	// ��ȡ���
//			TRACE("  index=%d\n",pMsg->index);

			ptr = strstr(ptr, "\r\n");	// ����һ��
			ptr += 2;		// ����"\r\n"
			
//			gsmDecodePdu(ptr, pMsg);	// PDU������

			pMsg++;		// ׼������һ������Ϣ
			nMsg++;		// ����Ϣ������1
		}
	}

	return nMsg;
}

// ɾ������Ϣ
// index: ����Ϣ��ţ���1��ʼ
BOOL gsmDeleteMessage(const int index)
{
	char cmd[16];		// ���
	char ans[100];		// Ӧ��

	memset(ans,0,100);

	sprintf(cmd, at.CMGD, index);	// ��������

	if (IsMultiATOK(cmd,ans)==true)
		return TRUE;

	return FALSE;
}

// �������Ϣ
// pSrc: ԴPDU����ָ��
int gsmSaveMessage(const SM_PARAM* pSrc, int Status)
{
	int nPduLength;		// PDU������
	unsigned char nSmscLength;	// SMSC������
	char cmd[128];		// ���
	char pdu[512];		// PDU��
	char ans[100];		// Ӧ��
	int Index;          //����������

	nPduLength = gsmEncodePdu(pSrc, pdu);	// ����PDU����������PDU��
	strcat(pdu, "\x01a");		// ��Ctrl-Z����

	gsmString2Bytes(pdu, &nSmscLength, 2);	// ȡPDU���е�SMSC��Ϣ����
	nSmscLength++;		// ���ϳ����ֽڱ���

	// �����еĳ��ȣ�������SMSC��Ϣ���ȣ��������ֽڼ�
	sprintf(cmd, at.CMGW, nPduLength / 2 - nSmscLength, Status);	// ��������

//	TRACE("%s", cmd);
//	TRACE("%s\n", pdu);

	memset(ans,0,100);

	// �����ܷ��ҵ�"\r\n> "�����ɹ����
	if (IsMultiChar(cmd,ans,"\r\n> ")==true)
	{
		char temp[400];
		memset(temp,0,400);
		if(IsMultiATOK(pdu, temp)==true)    // �õ��϶��ش𣬼������PDU��
		{
			int nb;
			Index=atoi(GetChar(temp,' ','\r',nb));
			return Index;
		}
		else
		{
			return -1;
		}

	}

	return -1;
}

//�����������ӳٵ��ַ�
//����ָ��������ֵ���ɹ�Ϊ�棬����Ϊ��
bool IsMultiChar(char ATCommand[], char* Value, char nCompare[])
{
	int nLength;
	char ans[512];
	
	WriteComm(ATCommand,strlen(ATCommand));
	while(true)
	{
		nLength=ReadComm(ans);
		if (nLength==0) 
			return false;
		ans[nLength]='\0';
		strcat(Value,ans);
		if(strstr(Value,nCompare))
			return true;
		if(strstr(Value,"ERROR"))
			return false;
	}
}

//���������Ĵ��ڶ�д
//����ָ��������ֵ���ɹ�Ϊ�棬����Ϊ��
bool IsMultiATOK(char ATCommand[], char* Value)
{
	int nLength;
	char ans[512];
	
	WriteComm(ATCommand,strlen(ATCommand));
//	Sleep(2000);
	while(true)
	{
		nLength=ReadComm(ans);
		if (nLength==0) 
			return false;
		ans[nLength]='\0';
		strcat(Value,ans);
		if(strstr(Value,"OK\r\n"))
			return true;
		if(strstr(Value,"ERROR"))
			return false;
	}
}
//���ַ���ab��ȡ�ַ�a���ַ�b֮����ַ����������ַ�b���ַ���ab�е�λ�á�
char *GetChar(char* ab, char a, char b, int& nb)
{
	int z=strlen(ab);

	char* Temp;
    Temp=new char[z];

	for(int i=0; i<z; i++)
		if (ab[i]==a) break;
	for(int j=i+1; j<z; j++)
		if (ab[j]==b) break;

	if (j>=z) 
		return "Null";
	else
	{
		z=j-i-1;
	    for(int k=0; k<z; k++)
			Temp[k]=ab[++i];
		Temp[k]='\0';

		nb=j;

		return Temp;

	}

}

/*qp����*/

void QuotedPrint(unsigned char* sour,int nCount)
{
	unsigned char first;
	unsigned char second;
	unsigned char* sourcpy=new unsigned char[nCount];
	
	for(int i=0; i<nCount; i++)
		sourcpy[i]=sour[i];
	
	for(int j=0; j<nCount; j++)
	{
			first=sourcpy[j]>>4;
			
			second=sourcpy[j]&15;
			
			if(first>9) first+=55;
			
			else first+=48;
			
			if(second>9) second+=55;
			
			else second+=48;
			
			sour[j*3]='=';
			sour[j*3+1]=first;
			sour[j*3+2]=second;

	}
	delete[] sourcpy;
	sour[3*nCount]='\0';
}



/*UnQuotedPrint����*/

void UnQuotedPrint(unsigned char* sour, int nCount)
{
	unsigned char first;
	unsigned char second;

	for( int i=0; i<nCount/3; i++)
	{
		first=sour[i*3+1];
		second=sour[i*3+2];
		
		if(first>=65) first-=55;
		
		else first-=48;
		
		if(second>=65) second-=55;
		
		else second-=48;
		
		sour[i]=NULL;
		
		sour[i]=first<<4;
		
		sour[i]|=second;
	}
	sour[nCount/3]='\0';
	
}

int  UTF8ToMutiByte(const char *src,int slen,char *dest,int dlen)
{
	unsigned short buffer[1024];

	int len = MultiByteToWideChar(CP_UTF8,0,src,slen,buffer,1024);

	len = WideCharToMultiByte(CP_ACP,0,buffer,len,dest,dlen,NULL,NULL);

	dest[len]='\0';

	return len;

}

int  MutiByteToUTF8(const char *src,int slen,char *dest,int dlen)
{
	unsigned short buffer[1024];

	int len = MultiByteToWideChar(CP_ACP,0,src,slen,buffer,1024);

	len = WideCharToMultiByte(CP_UTF8,0,buffer,len,dest,dlen,NULL,NULL);

	dest[len]='\0';

	return len;
}





