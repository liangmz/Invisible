#include <stdio.h>
#include <WinSock2.h>  
#pragma comment(lib,"ws2_32.lib")

#ifndef _FSTREAM_H_
#define _FSTREAM_H_
#include <fstream>
#endif

#ifndef _IOSTREAM_H_
#define _IOSTREAM_H_
#include <iostream>
using namespace std;
#endif


#ifndef _QUQUE_H_
#define _QUEUE_H_
#include <queue>
#endif

#include <time.h>  

typedef struct MessageUnit
{
	//��������
	int msgType;
	//�������
	int clickCode;
	//�Ƕ�
	int angle;
	//����
	int dir;
	//����
	int dis;
}MessageUnit;

#include <windows.h>
#include <process.h> 

DWORD WINAPI KeepConnected(LPVOID par);
DWORD WINAPI Option(LPVOID par);

extern queue<MessageUnit> msgQueue;//��Ϣ����
extern bool keep;//�ͻ��˿������� - ���ֿ���
extern bool _isOption;//��������
extern queue<long> keepQueue;


/*
MessageUnit ��ʽ

��1�ֽڣ�����
��2�ֽڣ�����
��3�ֽڣ�����A
��4�ֽڣ�����B
��5�ֽڣ�����C
��6�ֽڣ�����D
��7�ֽڣ�����E

*/
