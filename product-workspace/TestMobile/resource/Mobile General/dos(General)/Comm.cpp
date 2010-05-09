#include "windows.h"
//#include "stdafx.h"
#include "Comm.h"

// �����豸���
HANDLE hComm;

// �򿪴���
// pPort: �������ƻ��豸·��������"COM1"��"\\.\COM1"���ַ�ʽ�������ú���
// nBaudRate: ������
// nParity: ��żУ��
// nByteSize: �����ֽڿ��
// nStopBits: ֹͣλ
BOOL OpenComm(const char* pPort, int nBaudRate, int nParity, int nByteSize, int nStopBits)
{
	hComm = CreateFile(pPort,	// �������ƻ��豸·��
			GENERIC_READ | GENERIC_WRITE,	// ��д��ʽ
			0,				// ����ʽ����ռ
			NULL,			// Ĭ�ϵİ�ȫ������
			OPEN_EXISTING,	// ������ʽ
			0,				// ���������ļ�����FILE_FLAG_OVERLAPPED
			NULL);			// �������ģ���ļ�
	
	if(hComm == INVALID_HANDLE_VALUE) return FALSE;		// �򿪴���ʧ��

	SetCommMask(hComm, EV_RXCHAR|EV_TXEMPTY );//�����¼�����������

	DCB dcb;		// ���ڿ��ƿ�

	GetCommState(hComm, &dcb);		// ȡDCB

	dcb.BaudRate = nBaudRate;
	dcb.ByteSize = nByteSize;
	dcb.Parity = nParity;
	dcb.StopBits = nStopBits;

	SetCommState(hComm, &dcb);		// ����DCB

	SetupComm(hComm, 4096, 1024);	// �������������������С

	PurgeComm( hComm, PURGE_TXABORT | PURGE_RXABORT | PURGE_TXCLEAR | PURGE_RXCLEAR ); //��ɾ����롢���������


	COMMTIMEOUTS timeouts = {	// ���ڳ�ʱ���Ʋ���
		0,				// ���ַ������ʱʱ��: 100 ms(���), MAXWORD(Ĭ��), 1ms(дͼƬSetPicture,����ListShortMessage��SendShortMessage,SendUnSent,DeleteShortMessage������)
		0,					// ������ʱÿ�ַ���ʱ��: 1 ms (n���ַ��ܹ�Ϊn ms)
		0,				// ������(�����)����ʱʱ��: 500 ms,500ms,250ms
		1,					// д����ʱÿ�ַ���ʱ��: 1 ms (n���ַ��ܹ�Ϊn ms)
		100};				// ������(�����)д��ʱʱ��: 100 ms

	::SetCommTimeouts(hComm, &timeouts);	// ���ó�ʱ

	return TRUE;
}

// �رմ���
BOOL CloseComm()
{
	return CloseHandle(hComm);
}

// д����
// pData: ��д�����ݻ�����ָ��
// nLength: ��д�����ݳ���
void WriteComm(void* pData, int nLength)
{
	DWORD dwNumWrite;	// ���ڷ��������ݳ���

	PurgeComm( hComm, PURGE_TXABORT | PURGE_RXABORT | PURGE_TXCLEAR | PURGE_RXCLEAR ); //��ɾ����롢���������

	WriteFile(hComm, pData, (DWORD)nLength, &dwNumWrite, NULL);
}

// ������
// pData: ���������ݻ�����ָ��
// nLength: ������������ݳ���
// ����: ʵ�ʶ�������ݳ���
int ReadComm(void* pData)
{
	DWORD dwBytesRead=0;	// �����յ������ݳ���
	DWORD dwEvtMask=0 ;
	DWORD dwErrorFlags;
	//����Щ�����¼���Ҫ���ӣ�
	SetCommMask( hComm, EV_RXCHAR|EV_TXEMPTY ); 

	while(true)
	{
		// �ȴ�����ͨ���¼��ķ�����ⷵ�ص�dwEvtMask��֪��������ʲô�����¼���
		if(WaitCommEvent( hComm, &dwEvtMask, NULL ))
		// �������������ݵ���if ((dwEvtMask & EV_RXCHAR) == EV_RXCHAR)
		{ 
			COMSTAT ComStat ; DWORD dwLength;
			ClearCommError(hComm, &dwErrorFlags, &ComStat ) ;
			//���뻺�����ж������ݣ�
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

//���ڼ���ֻ��˿�ʱ��
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
	DWORD dwBytesRead=0;	// �����յ������ݳ���
	DWORD dwEvtMask=0 ;
	DWORD dwErrorFlags;
	//����Щ�����¼���Ҫ���ӣ�
	SetCommMask( hComm, EV_RXCHAR|EV_TXEMPTY ); 

	while(true)
	{
		// �ȴ�����ͨ���¼��ķ�����ⷵ�ص�dwEvtMask��֪��������ʲô�����¼���
		WaitCommEvent( hComm, &dwEvtMask, NULL );
		// �������������ݵ���
		if ((dwEvtMask & EV_RXCHAR) == EV_RXCHAR)//if (((dwEvtMask & EV_RXCHAR) == EV_RXCHAR)&&(dwEvtMask==1))
		{ 
			COMSTAT ComStat ; DWORD dwLength;
			ClearCommError(hComm, &dwErrorFlags, &ComStat ) ;
			//���뻺�����ж������ݣ�
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

