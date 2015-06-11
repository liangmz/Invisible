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
	//数据类型
	int msgType;
	//点击代码
	int clickCode;
	//角度
	int angle;
	//方向
	int dir;
	//距离
	int dis;
}MessageUnit;

#include <windows.h>
#include <process.h> 

DWORD WINAPI KeepConnected(LPVOID par);
DWORD WINAPI Option(LPVOID par);

extern queue<MessageUnit> msgQueue;//消息队列
extern bool keep;//客户端开启开关 - 保持开启
extern bool _isOption;//操作开关
extern queue<long> keepQueue;


/*
MessageUnit 格式

第1字节：长度
第2字节：类型
第3字节：数据A
第4字节：数据B
第5字节：数据C
第6字节：数据D
第7字节：数据E

*/
