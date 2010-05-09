// Comm.h: interface for the CComm class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(COMM_H__)
#define COMM_H__

BOOL OpenComm(const char* pPort, int nBaudRate=57600, int nParity=NOPARITY, int nByteSize=8, int nStopBits=ONESTOPBIT);
BOOL CloseComm();
void WriteComm(void* pData, int nLength);
int ReadComm(void* pData);
int sReadComm(void* pData);

#endif // !defined(COMM_H__)
