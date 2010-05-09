#include "windows.h"
//#include "stdafx.h"
#include "stdio.h"
#include "Sms.h"
#include "Comm.h"
#include "Type.h"

extern ATCOMMANDSET at;
extern int MobileType;              // 1-SIEMENS; 2-SONY ERICSSON; 3-NOKIA

// 可打印字符串转换为字节数据
// 如："C8329BFD0E01" --> {0xC8, 0x32, 0x9B, 0xFD, 0x0E, 0x01}
// pSrc: 源字符串指针
// pDst: 目标数据指针
// nSrcLength: 源字符串长度
// 返回: 目标数据长度
int gsmString2Bytes(const char* pSrc, unsigned char* pDst, int nSrcLength)
{
	for(int i=0; i<nSrcLength; i+=2)
	{
		// 输出高4位
		if(*pSrc>='0' && *pSrc<='9')
		{
			*pDst = (*pSrc - '0') << 4;
		}
		else
		{
			*pDst = (*pSrc - 'A' + 10) << 4;
		}

		pSrc++;

		// 输出低4位
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

	// 返回目标数据长度
	return nSrcLength / 2;
}

// 字节数据转换为可打印字符串
// 如：{0xC8, 0x32, 0x9B, 0xFD, 0x0E, 0x01} --> "C8329BFD0E01" 
// pSrc: 源数据指针
// pDst: 目标字符串指针
// nSrcLength: 源数据长度
// 返回: 目标字符串长度
int gsmBytes2String(const unsigned char* pSrc, char* pDst, int nSrcLength)
{
	const char tab[]="0123456789ABCDEF";	// 0x0-0xf的字符查找表

	for(int i=0; i<nSrcLength; i++)
	{
		*pDst++ = tab[*pSrc >> 4];		// 输出低4位
		*pDst++ = tab[*pSrc & 0x0f];	// 输出高4位
		pSrc++;
	}

	// 输出字符串加个结束符
	*pDst = '\0';

	// 返回目标字符串长度
	return nSrcLength * 2;
}

// 7bit编码
// pSrc: 源字符串指针
// pDst: 目标编码串指针
// nSrcLength: 源字符串长度
// 返回: 目标编码串长度
int gsmEncode7bit(const char* pSrc, unsigned char* pDst, int nSrcLength)
{
	int nSrc;		// 源字符串的计数值
	int nDst;		// 目标编码串的计数值
	int nChar;		// 当前正在处理的组内字符字节的序号，范围是0-7
	unsigned char nLeft;	// 上一字节残余的数据

	// 计数值初始化
	nSrc = 0;
	nDst = 0;

	// 将源串每8个字节分为一组，压缩成7个字节
	// 循环该处理过程，直至源串被处理完
	// 如果分组不到8字节，也能正确处理
	while(nSrc<nSrcLength)
	{
		// 取源字符串的计数值的最低3位
		nChar = nSrc & 7;

		// 处理源串的每个字节
		if(nChar == 0)
		{
			// 组内第一个字节，只是保存起来，待处理下一个字节时使用
			nLeft = *pSrc;
		}
		else
		{
			// 组内其它字节，将其右边部分与残余数据相加，得到一个目标编码字节
			*pDst = (*pSrc << (8-nChar)) | nLeft;

			// 将该字节剩下的左边部分，作为残余数据保存起来
			nLeft = *pSrc >> nChar;

			// 修改目标串的指针和计数值
			pDst++;
			nDst++;
		}

		// 修改源串的指针和计数值
		pSrc++;
		nSrc++;
	}

	// 返回目标串长度
	return nDst;
}

// 7bit解码
// pSrc: 源编码串指针
// pDst: 目标字符串指针
// nSrcLength: 源编码串长度
// 返回: 目标字符串长度
int gsmDecode7bit(const unsigned char* pSrc, char* pDst, int nSrcLength)
{
	int nSrc;		// 源字符串的计数值
	int nDst;		// 目标解码串的计数值
	int nByte;		// 当前正在处理的组内字节的序号，范围是0-6
	unsigned char nLeft;	// 上一字节残余的数据

	// 计数值初始化
	nSrc = 0;
	nDst = 0;
	
	// 组内字节序号和残余数据初始化
	nByte = 0;
	nLeft = 0;

	// 将源数据每7个字节分为一组，解压缩成8个字节
	// 循环该处理过程，直至源数据被处理完
	// 如果分组不到7字节，也能正确处理
	while(nSrc<nSrcLength)
	{
		// 将源字节右边部分与残余数据相加，去掉最高位，得到一个目标解码字节
		*pDst = ((*pSrc << nByte) | nLeft) & 0x7f;

		// 将该字节剩下的左边部分，作为残余数据保存起来
		nLeft = *pSrc >> (7-nByte);

		// 修改目标串的指针和计数值
		pDst++;
		nDst++;

		// 修改字节计数值
		nByte++;

		// 到了一组的最后一个字节
		if(nByte == 7)
		{
			// 额外得到一个目标解码字节
			*pDst = nLeft;

			// 修改目标串的指针和计数值
			pDst++;
			nDst++;

			// 组内字节序号和残余数据初始化
			nByte = 0;
			nLeft = 0;
		}

		// 修改源串的指针和计数值
		pSrc++;
		nSrc++;
	}

	// 输出字符串加个结束符
	*pDst = '\0';

	// 返回目标串长度
	return nDst;
}

// 8bit编码
// pSrc: 源字符串指针
// pDst: 目标编码串指针
// nSrcLength: 源字符串长度
// 返回: 目标编码串长度
int gsmEncode8bit(const char* pSrc, unsigned char* pDst, int nSrcLength)
{
	// 简单复制
	memcpy(pDst, pSrc, nSrcLength);

	return nSrcLength;
}

// 8bit解码
// pSrc: 源编码串指针
// pDst: 目标字符串指针
// nSrcLength: 源编码串长度
// 返回: 目标字符串长度
int gsmDecode8bit(const unsigned char* pSrc, char* pDst, int nSrcLength)
{
	// 简单复制
	memcpy(pDst, pSrc, nSrcLength);

	// 输出字符串加个结束符
	*pDst = '\0';

	return nSrcLength;
}

// UCS2编码
// pSrc: 源字符串指针
// pDst: 目标编码串指针
// nSrcLength: 源字符串长度
// 返回: 目标编码串长度
int gsmEncodeUcs2(const char* pSrc, unsigned char* pDst, int nSrcLength)
{
	int nDstLength;		// UNICODE宽字符数目
	WCHAR wchar[128];	// UNICODE串缓冲区

	// 字符串-->UNICODE串
	nDstLength = ::MultiByteToWideChar(CP_ACP, 0, pSrc, nSrcLength, wchar, 128);

	// 高低字节对调，输出
	for(int i=0; i<nDstLength; i++)
	{
		*pDst++ = wchar[i] >> 8;		// 先输出高位字节
		*pDst++ = wchar[i] & 0xff;		// 后输出低位字节
	}

	// 返回目标编码串长度
	return nDstLength * 2;
}

// UCS2解码
// pSrc: 源编码串指针
// pDst: 目标字符串指针
// nSrcLength: 源编码串长度
// 返回: 目标字符串长度
int gsmDecodeUcs2(const unsigned char* pSrc, char* pDst, int nSrcLength)
{
	int nDstLength;		// UNICODE宽字符数目
	WCHAR wchar[128];	// UNICODE串缓冲区

	// 高低字节对调，拼成UNICODE
	for(int i=0; i<nSrcLength/2; i++)
	{
		wchar[i] = *pSrc++ << 8;	// 先高位字节
		wchar[i] |= *pSrc++;		// 后低位字节
	}

	// UNICODE串-->字符串
	nDstLength = ::WideCharToMultiByte(CP_ACP, 0, wchar, nSrcLength/2, pDst, 160, NULL, NULL);

	// 输出字符串加个结束符
	pDst[nDstLength] = '\0';

	// 返回目标字符串长度
	return nDstLength;
}

// 正常顺序的字符串转换为两两颠倒的字符串，若长度为奇数，补'F'凑成偶数
// 如："8613851872468" --> "683158812764F8"
// pSrc: 源字符串指针
// pDst: 目标字符串指针
// nSrcLength: 源字符串长度
// 返回: 目标字符串长度
int gsmInvertNumbers(const char* pSrc, char* pDst, int nSrcLength)
{
	int nDstLength;		// 目标字符串长度
	char ch;			// 用于保存一个字符

	// 复制串长度
	nDstLength = nSrcLength;

	// 两两颠倒
	for(int i=0; i<nSrcLength;i+=2)
	{
		ch = *pSrc++;		// 保存先出现的字符
		*pDst++ = *pSrc++;	// 复制后出现的字符
		*pDst++ = ch;		// 复制先出现的字符
	}

	// 源串长度是奇数吗？
	if(nSrcLength & 1)
	{
		*(pDst-2) = 'F';	// 补'F'
		nDstLength++;		// 目标串长度加1
	}

	// 输出字符串加个结束符
	*pDst = '\0';

	// 返回目标字符串长度
	return nDstLength;
}

// 两两颠倒的字符串转换为正常顺序的字符串
// 如："683158812764F8" --> "8613851872468"
// pSrc: 源字符串指针
// pDst: 目标字符串指针
// nSrcLength: 源字符串长度
// 返回: 目标字符串长度
int gsmSerializeNumbers(const char* pSrc, char* pDst, int nSrcLength)
{
	int nDstLength;		// 目标字符串长度
	char ch;			// 用于保存一个字符

	// 复制串长度
	nDstLength = nSrcLength;

	// 两两颠倒
	for(int i=0; i<nSrcLength;i+=2)
	{
		ch = *pSrc++;		// 保存先出现的字符
		*pDst++ = *pSrc++;	// 复制后出现的字符
		*pDst++ = ch;		// 复制先出现的字符
	}

	// 最后的字符是'F'吗？
	if(*(pDst-1) == 'F')
	{
		pDst--;
		nDstLength--;		// 目标字符串长度减1
	}

	// 输出字符串加个结束符
	*pDst = '\0';

	// 返回目标字符串长度
	return nDstLength;
}

// PDU编码，用于编制、发送短消息
// pSrc: 源PDU参数指针
// pDst: 目标PDU串指针
// 返回: 目标PDU串长度
int gsmEncodePdu(const SM_PARAM* pSrc, char* pDst)
{
	int nLength;			// 内部用的串长度
	int nDstLength;			// 目标PDU串长度
	unsigned char buf[256];	// 内部用的缓冲区

	// SMSC地址信息段
	nLength = strlen(pSrc->SCA);	// SMSC地址字符串的长度	
	if (nLength==0)
	{
		buf[0]=(char)(0);
        nDstLength = gsmBytes2String(buf, pDst, 1);		// 转换2个字节到目标PDU串
	}
	else
	{
		buf[0] = (char)((nLength & 1) == 0 ? nLength : nLength + 1) / 2 + 1;	// SMSC地址信息长度
		buf[1] = 0x91;		// 固定: 用国际格式号码
		nDstLength = gsmBytes2String(buf, pDst, 2);		// 转换2个字节到目标PDU串
	}
	nDstLength += gsmInvertNumbers(pSrc->SCA, &pDst[nDstLength], nLength);	// 转换SMSC号码到目标PDU串

	// TPDU段基本参数、目标地址等
	nLength = strlen(pSrc->TPA);	// TP-DA地址字符串的长度
	buf[0] = pSrc->TP_ST;			// 是发送短信(TP-MTI=01)，TP-VP用相对格式(TP-VPF=10)
	buf[1] = 0;						// TP-MR=0
	buf[2] = (char)nLength;			// 目标地址数字个数(TP-DA地址字符串真实长度)
	buf[3] = 0x91;					// 固定: 用国际格式号码
	nDstLength += gsmBytes2String(buf, &pDst[nDstLength], 4);		// 转换4个字节到目标PDU串
	nDstLength += gsmInvertNumbers(pSrc->TPA, &pDst[nDstLength], nLength);	// 转换TP-DA到目标PDU串

	// TPDU段协议标识、编码方式、用户信息等
	nLength = strlen(pSrc->TP_UD);	// 用户信息字符串的长度
	buf[0] = pSrc->TP_PID;			// 协议标识(TP-PID)
	buf[1] = pSrc->TP_DCS;			// 用户信息编码方式(TP-DCS)
	buf[2] = pSrc->TP_VT;			// 有效期(TP-VP)为5分钟或最大值

	if(pSrc->TP_DCS == GSM_7BIT || pSrc->TP_DCS == GSM_M7BIT)	
	{
		// 7-bit编码方式
		buf[3] = nLength;			// 编码前长度
		nLength = gsmEncode7bit(pSrc->TP_UD, &buf[4], nLength+1) + 4;	// 转换TP-DA到目标PDU串
	}
	else if(pSrc->TP_DCS == GSM_UCS2 || pSrc->TP_DCS==GSM_MUCS2)
	{
		// UCS2编码方式
		buf[3] = gsmEncodeUcs2(pSrc->TP_UD, &buf[4], nLength);	// 转换TP-DA到目标PDU串
		nLength = buf[3] + 4;		// nLength等于该段数据长度
	}
	else
	{
		// 8-bit编码方式
		buf[3] = gsmEncode8bit(pSrc->TP_UD, &buf[4], nLength);	// 转换TP-DA到目标PDU串
		nLength = buf[3] + 4;		// nLength等于该段数据长度
	}
	nDstLength += gsmBytes2String(buf, &pDst[nDstLength], nLength);		// 转换该段数据到目标PDU串

	// 返回目标字符串长度
	return nDstLength;
}

// PDU解码，用于接收、阅读短消息
// pSrc: 源PDU串指针
// pDst: 目标PDU参数指针
// 返回: 用户信息串长度
int gsmDecodePdu(int Status, const char* pSrc, SM_PARAM* pDst)
{
	int nDstLength;			// 目标PDU串长度
	unsigned char tmp;		// 内部用的临时字节变量
	unsigned char buf[256];	// 内部用的缓冲区
	unsigned char Internet=0x00; // 国际号码格式标识

	// SMSC地址信息段
	gsmString2Bytes(pSrc, &tmp, 2);	// 取长度
	if (tmp==0)
		pSrc +=2;
	else
	{
		tmp = (tmp - 1) * 2;	// SMSC号码串长度
		pSrc += 4;			// 指针后移，忽略了SMSC地址格式
		gsmSerializeNumbers(pSrc, pDst->SCA, tmp);	// 转换SMSC号码到目标PDU串
		pSrc += tmp;		// 指针后移
	}

	gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_ST, 2);	// 取状态报告(TP-ST)

	if (Status==2 || Status==3)//未发短消息已发短消息
	{
		pSrc += 4;		// 指针后移
		// 包含回复地址，取回复地址信息
		gsmString2Bytes(pSrc, &tmp, 2);	// 取长度
		if(tmp & 1) tmp += 1;	// 调整奇偶性
		

		gsmString2Bytes(&pSrc[2], &Internet,2);
		pSrc += 4;			// 指针后移，忽略了回复地址(TP-RA)格式
		if (Internet==0x91 ) 
		{
			memcpy(pDst->TPA,"+",1);
			gsmSerializeNumbers(pSrc, &pDst->TPA[1], tmp);	// 取TP-RA号码
		}
		else
			gsmSerializeNumbers(pSrc, pDst->TPA, tmp);	// 取TP-RA号码
		

		pSrc += tmp;		// 指针后移
		// TPDU段协议标识、编码方式、用户信息等
		gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_PID, 2);	// 取协议标识(TP-PID)
		pSrc += 2;		// 指针后移
		gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_DCS, 2);	// 取编码方式(TP-DCS)
		pSrc += 2;		// 指针后移
		gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_VT, 2);	// 取有效时间(TP-VT)
		pSrc += 2;		// 指针后移
		gsmString2Bytes(pSrc, &tmp, 2);	// 用户信息长度(TP-UDL)
		pSrc += 2;		// 指针后移

		if ((pDst->TP_ST & 0x40)!=0)  // 去除头信息
		{
			gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_UDS , 14);	// 取连锁短消息标志
			tmp=tmp-7;
			pSrc += 14;
		}

		if(pDst->TP_DCS == GSM_7BIT || pDst->TP_DCS == GSM_M7BIT)	
		{
			// 7-bit解码
			nDstLength = gsmString2Bytes(pSrc, buf, tmp & 7 ? (int)tmp * 7 / 4 + 2 : (int)tmp * 7 / 4);	// 格式转换
			gsmDecode7bit(buf, pDst->TP_UD, nDstLength);	// 转换到TP-DU
			nDstLength = tmp;
		}
		else if(pDst->TP_DCS == GSM_UCS2 || pDst->TP_DCS == GSM_MUCS2)
		{
			// UCS2解码
			nDstLength = gsmString2Bytes(pSrc, buf, tmp * 2);			// 格式转换
			nDstLength = gsmDecodeUcs2(buf, pDst->TP_UD, nDstLength);	// 转换到TP-DU
		}
		else
		{
			// 8-bit解码
			nDstLength = gsmString2Bytes(pSrc, buf, tmp * 2);			// 格式转换
			nDstLength = gsmDecode8bit(buf, pDst->TP_UD, nDstLength);	// 转换到TP-DU
		}
		
		// 返回目标字符串长度
		return nDstLength;
		
	}

	if (pDst->TP_ST==6)                     //状态回复的特殊格式
	{
		pSrc += 4;
		// 包含回复地址，取回复地址信息
		gsmString2Bytes(pSrc, &tmp, 2);	// 取长度
		if(tmp & 1) tmp += 1;	// 调整奇偶性


		gsmString2Bytes(&pSrc[2], &Internet,2);
		pSrc += 4;			// 指针后移，忽略了回复地址(TP-RA)格式
		if (Internet==0x91 ) 
		{
			memcpy(pDst->TPA,"+",1);
			gsmSerializeNumbers(pSrc, &pDst->TPA[1], tmp);	// 取TP-RA号码
		}
		else
			gsmSerializeNumbers(pSrc, pDst->TPA, tmp);	// 取TP-RA号码


		pSrc += tmp;		// 指针后移
		gsmSerializeNumbers(pSrc, pDst->TP_SCTS, 14);		// 服务时间戳字符串(TP_SCTS) 
	    pSrc += 14;		// 指针后移
        gsmSerializeNumbers(pSrc, pDst->TP_UD, 14);		// 服务时间戳字符串(TP_SCTS) 
		pSrc += 14;

		return 14;
	}

	pSrc += 2;		// 指针后移
	
	// 包含回复地址，取回复地址信息
	gsmString2Bytes(pSrc, &tmp, 2);	// 取长度
	if(tmp & 1) tmp += 1;	// 调整奇偶性
	
	
	gsmString2Bytes(&pSrc[2], &Internet,2);
	pSrc += 4;			// 指针后移，忽略了回复地址(TP-RA)格式
	if (Internet==0x91 ) 
	{
		memcpy(pDst->TPA,"+",1);
		gsmSerializeNumbers(pSrc, &pDst->TPA[1], tmp);	// 取TP-RA号码
	}
	else
		gsmSerializeNumbers(pSrc, pDst->TPA, tmp);	// 取TP-RA号码
	
	
	pSrc += tmp;		// 指针后移
	
	
	// TPDU段协议标识、编码方式、用户信息等
	gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_PID, 2);	// 取协议标识(TP-PID)
	pSrc += 2;		// 指针后移
	gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_DCS, 2);	// 取编码方式(TP-DCS)
	pSrc += 2;		// 指针后移
	gsmSerializeNumbers(pSrc, pDst->TP_SCTS, 14);		// 服务时间戳字符串(TP_SCTS) 
	pSrc += 14;		// 指针后移
	gsmString2Bytes(pSrc, &tmp, 2);	// 用户信息长度(TP-UDL)
	pSrc += 2;		// 指针后移

	if ((pDst->TP_ST & 0x40)!=0)  // 去除头信息
	{
		gsmString2Bytes(pSrc, (unsigned char*)&pDst->TP_UDS , 14);	// 取连锁短消息标志
		tmp=tmp-7;
		pSrc += 14;
	}

	if(pDst->TP_DCS == GSM_7BIT)	
	{
		// 7-bit解码
		nDstLength = gsmString2Bytes(pSrc, buf, tmp & 7 ? (int)tmp * 7 / 4 + 2 : (int)tmp * 7 / 4);	// 格式转换
		gsmDecode7bit(buf, pDst->TP_UD, nDstLength);	// 转换到TP-DU
		nDstLength = tmp;
	}
	else if(pDst->TP_DCS == GSM_UCS2)
	{
		// UCS2解码
		nDstLength = gsmString2Bytes(pSrc, buf, tmp * 2);			// 格式转换
		nDstLength = gsmDecodeUcs2(buf, pDst->TP_UD, nDstLength);	// 转换到TP-DU
	}
	else
	{
		// 8-bit解码
		nDstLength = gsmString2Bytes(pSrc, buf, tmp * 2);			// 格式转换
		nDstLength = gsmDecode8bit(buf, pDst->TP_UD, nDstLength);	// 转换到TP-DU
	}
	
	// 返回目标字符串长度
	return nDstLength;
}


// 发送短消息
// pSrc: 源PDU参数指针
BOOL gsmSendMessage(const SM_PARAM* pSrc)
{
	int nPduLength;		// PDU串长度
	unsigned char nSmscLength;	// SMSC串长度
	char cmd[128];		// 命令串
	char pdu[512];		// PDU串
	char ans[100];		// 应答串

	nPduLength = gsmEncodePdu(pSrc, pdu);	// 根据PDU参数，编码PDU串
	strcat(pdu, "\x01a");		// 以Ctrl-Z结束

	gsmString2Bytes(pdu, &nSmscLength, 2);	// 取PDU串中的SMSC信息长度
	nSmscLength++;		// 加上长度字节本身

	// 命令中的长度，不包括SMSC信息长度，以数据字节计
	sprintf(cmd, at.CMGS, nPduLength / 2 - nSmscLength);	// 生成命令

//	TRACE("%s", cmd);
//	TRACE("%s\n", pdu);

	memset(ans,0,100);

	// 根据能否找到"\r\n> "决定成功与否
	if (IsMultiChar(cmd,ans,"\r\n> ")==true)
	{
		char temp[400];
		memset(temp,0,400);

//		Sleep(3500);
		IsMultiATOK(pdu, temp);
		return TRUE;
/*		if(IsMultiATOK(pdu, temp2)==false)    // 得到肯定回答，继续输出PDU串
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

// 读取短消息
// 用+CMGL代替+CMGR，可一次性读出全部短消息
// pMsg: 短消息缓冲区，必须足够大
// 返回: 短消息条数
int gsmReadMessage(SM_PARAM* pMsg)
{
	int nLength;		// 串口收到的数据长度
	int nMsg;			// 短消息计数值
	char* ptr;			// 内部用的数据指针
	char cmd[16];		// 命令串
	char ans[1024];		// 应答串

	nMsg = 0;
	ptr = ans;

	sprintf(cmd, "AT+CMGL\r");	// 生成命令

	WriteComm(cmd, strlen(cmd));	// 输出命令串

	nLength = ReadComm(ans);	// 读应答数据

	// 根据能否找到"+CMS ERROR"决定成功与否
	if(nLength > 0 && strncmp(ans, "+CMS ERROR", 10) != 0)
	{
		// 循环读取每一条短消息, 以"+CMGL:"开头
		while((ptr = strstr(ptr, "+CMGL:")) != NULL)
		{
			ptr += 6;		// 跳过"+CMGL:"
			sscanf(ptr, "%d", &pMsg->index);	// 读取序号
//			TRACE("  index=%d\n",pMsg->index);

			ptr = strstr(ptr, "\r\n");	// 找下一行
			ptr += 2;		// 跳过"\r\n"
			
//			gsmDecodePdu(ptr, pMsg);	// PDU串解码

			pMsg++;		// 准备读下一条短消息
			nMsg++;		// 短消息计数加1
		}
	}

	return nMsg;
}

// 删除短消息
// index: 短消息序号，从1开始
BOOL gsmDeleteMessage(const int index)
{
	char cmd[16];		// 命令串
	char ans[100];		// 应答串

	memset(ans,0,100);

	sprintf(cmd, at.CMGD, index);	// 生成命令

	if (IsMultiATOK(cmd,ans)==true)
		return TRUE;

	return FALSE;
}

// 保存短消息
// pSrc: 源PDU参数指针
int gsmSaveMessage(const SM_PARAM* pSrc, int Status)
{
	int nPduLength;		// PDU串长度
	unsigned char nSmscLength;	// SMSC串长度
	char cmd[128];		// 命令串
	char pdu[512];		// PDU串
	char ans[100];		// 应答串
	int Index;          //返回索引号

	nPduLength = gsmEncodePdu(pSrc, pdu);	// 根据PDU参数，编码PDU串
	strcat(pdu, "\x01a");		// 以Ctrl-Z结束

	gsmString2Bytes(pdu, &nSmscLength, 2);	// 取PDU串中的SMSC信息长度
	nSmscLength++;		// 加上长度字节本身

	// 命令中的长度，不包括SMSC信息长度，以数据字节计
	sprintf(cmd, at.CMGW, nPduLength / 2 - nSmscLength, Status);	// 生成命令

//	TRACE("%s", cmd);
//	TRACE("%s\n", pdu);

	memset(ans,0,100);

	// 根据能否找到"\r\n> "决定成功与否
	if (IsMultiChar(cmd,ans,"\r\n> ")==true)
	{
		char temp[400];
		memset(temp,0,400);
		if(IsMultiATOK(pdu, temp)==true)    // 得到肯定回答，继续输出PDU串
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

//持续接受有延迟的字符
//输入指令，输出串口值。成功为真，否则为假
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

//大数据量的串口读写
//输入指令，输出串口值。成功为真，否则为假
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
//从字符串ab中取字符a和字符b之间的字符串，返回字符b在字符串ab中的位置。
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

/*qp编码*/

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



/*UnQuotedPrint解码*/

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





