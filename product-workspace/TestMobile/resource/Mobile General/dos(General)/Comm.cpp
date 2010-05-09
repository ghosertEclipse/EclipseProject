#include "windows.h"
//#include "stdafx.h"
#include "Comm.h"

// 串口设备句柄
HANDLE hComm;

// 打开串口
// pPort: 串口名称或设备路径，可用"COM1"或"\\.\COM1"两种方式，建议用后者
// nBaudRate: 波特率
// nParity: 奇偶校验
// nByteSize: 数据字节宽度
// nStopBits: 停止位
BOOL OpenComm(const char* pPort, int nBaudRate, int nParity, int nByteSize, int nStopBits)
{
	hComm = CreateFile(pPort,	// 串口名称或设备路径
			GENERIC_READ | GENERIC_WRITE,	// 读写方式
			0,				// 共享方式：独占
			NULL,			// 默认的安全描述符
			OPEN_EXISTING,	// 创建方式
			0,				// 不需设置文件属性FILE_FLAG_OVERLAPPED
			NULL);			// 不需参照模板文件
	
	if(hComm == INVALID_HANDLE_VALUE) return FALSE;		// 打开串口失败

	SetCommMask(hComm, EV_RXCHAR|EV_TXEMPTY );//设置事件驱动的类型

	DCB dcb;		// 串口控制块

	GetCommState(hComm, &dcb);		// 取DCB

	dcb.BaudRate = nBaudRate;
	dcb.ByteSize = nByteSize;
	dcb.Parity = nParity;
	dcb.StopBits = nStopBits;

	SetCommState(hComm, &dcb);		// 设置DCB

	SetupComm(hComm, 4096, 1024);	// 设置输入输出缓冲区大小

	PurgeComm( hComm, PURGE_TXABORT | PURGE_RXABORT | PURGE_TXCLEAR | PURGE_RXCLEAR ); //清干净输入、输出缓冲区


	COMMTIMEOUTS timeouts = {	// 串口超时控制参数
		0,				// 读字符间隔超时时间: 100 ms(最初), MAXWORD(默认), 1ms(写图片SetPicture,其中ListShortMessage与SendShortMessage,SendUnSent,DeleteShortMessage有问题)
		0,					// 读操作时每字符的时间: 1 ms (n个字符总共为n ms)
		0,				// 基本的(额外的)读超时时间: 500 ms,500ms,250ms
		1,					// 写操作时每字符的时间: 1 ms (n个字符总共为n ms)
		100};				// 基本的(额外的)写超时时间: 100 ms

	::SetCommTimeouts(hComm, &timeouts);	// 设置超时

	return TRUE;
}

// 关闭串口
BOOL CloseComm()
{
	return CloseHandle(hComm);
}

// 写串口
// pData: 待写的数据缓冲区指针
// nLength: 待写的数据长度
void WriteComm(void* pData, int nLength)
{
	DWORD dwNumWrite;	// 串口发出的数据长度

	PurgeComm( hComm, PURGE_TXABORT | PURGE_RXABORT | PURGE_TXCLEAR | PURGE_RXCLEAR ); //清干净输入、输出缓冲区

	WriteFile(hComm, pData, (DWORD)nLength, &dwNumWrite, NULL);
}

// 读串口
// pData: 待读的数据缓冲区指针
// nLength: 待读的最大数据长度
// 返回: 实际读入的数据长度
int ReadComm(void* pData)
{
	DWORD dwBytesRead=0;	// 串口收到的数据长度
	DWORD dwEvtMask=0 ;
	DWORD dwErrorFlags;
	//有哪些串口事件需要监视？
	SetCommMask( hComm, EV_RXCHAR|EV_TXEMPTY ); 

	while(true)
	{
		// 等待串口通信事件的发生检测返回的dwEvtMask，知道发生了什么串口事件：
		if(WaitCommEvent( hComm, &dwEvtMask, NULL ))
		// 缓冲区中有数据到达if ((dwEvtMask & EV_RXCHAR) == EV_RXCHAR)
		{ 
			COMSTAT ComStat ; DWORD dwLength;
			ClearCommError(hComm, &dwErrorFlags, &ComStat ) ;
			//输入缓冲区有多少数据？
			dwLength = ComStat.cbInQue ; 
			if (dwLength > 0) 
			{
				ReadFile( hComm, pData, (DWORD)dwLength, &dwBytesRead, NULL );
				return (int)dwLength;
			}
			//else
				//break;
		}
		else
			break;
	}
	
	return (int)dwBytesRead;

}

//仅在监测手机端口时用
int sReadComm(void* pData)
{
	DWORD dwBytesRead=0;
	DWORD dwEvtMask=0 ;
	DWORD dwErrorFlags;
	
	SetCommMask( hComm, EV_RXCHAR|EV_TXEMPTY ); 
	if(WaitCommEvent( hComm, &dwEvtMask, NULL ))
	{ 
		COMSTAT ComStat ; DWORD dwLength;
		ClearCommError(hComm, &dwErrorFlags, &ComStat ) ;
		dwLength = ComStat.cbInQue ; 
		if (dwLength > 0) 
		{
			ReadFile( hComm, pData, (DWORD)dwLength, &dwBytesRead, NULL );
			return (int)dwLength;
		}
	}
	
	return (int)dwBytesRead;
}

/*
int ReadComm(void* pData)
{
	DWORD dwBytesRead=0;	// 串口收到的数据长度
	DWORD dwEvtMask=0 ;
	DWORD dwErrorFlags;
	//有哪些串口事件需要监视？
	SetCommMask( hComm, EV_RXCHAR|EV_TXEMPTY ); 

	while(true)
	{
		// 等待串口通信事件的发生检测返回的dwEvtMask，知道发生了什么串口事件：
		WaitCommEvent( hComm, &dwEvtMask, NULL );
		// 缓冲区中有数据到达
		if ((dwEvtMask & EV_RXCHAR) == EV_RXCHAR)//if (((dwEvtMask & EV_RXCHAR) == EV_RXCHAR)&&(dwEvtMask==1))
		{ 
			COMSTAT ComStat ; DWORD dwLength;
			ClearCommError(hComm, &dwErrorFlags, &ComStat ) ;
			//输入缓冲区有多少数据？
			dwLength = ComStat.cbInQue ; 
			if (dwLength > 0) 
			{
				ReadFile( hComm, pData, (DWORD)dwLength, &dwBytesRead, NULL );
				return (int)dwLength;
			}
		}
		else
			break;
	}
	
	return (int)dwBytesRead;

}
*/

