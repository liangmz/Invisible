#include "HANDLE.h"

int main()
{

	WSADATA wsa;
	if(WSAStartup(MAKEWORD(1,1),&wsa) != 0)
	{
		return 0;
	}

	SOCKET listen_ = socket(AF_INET,SOCK_DGRAM,IPPROTO_UDP);//失败就返回INVALID_SOCKET（Linux下失败返回-1）

	if(listen_ == INVALID_SOCKET)
	{
		cout << "INVALID_SOCKET." << endl;
		return 0;
	}

	SOCKADDR_IN serverAddr;
	serverAddr.sin_port = htons(12356);
	serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);
	serverAddr.sin_family = AF_INET;

	if(bind(listen_,(SOCKADDR*)&serverAddr,sizeof(SOCKADDR)) == SOCKET_ERROR )//如果函数执行成功，返回值为0，否则为SOCKET_ERROR。
	{
		cout << "SOCKET_ERROR." << endl;
		return 0 ;
	}
	listen(listen_,1);

	while(!msgQueue.empty())
	{
		msgQueue.pop();
	}

	//HANDLE KC = CreateThread(NULL , 0 , KeepConnected , NULL , 0 ,NULL);
	//HANDLE OP = CreateThread(NULL , 0 , Option , NULL , 0 , NULL);

	char revBuffer[1000];
	byte _revBuffer[1000];

	SOCKADDR_IN addrClient;
	int len = sizeof(SOCKADDR);

	int linkCount = 0;

	cout << "ok." << endl;

	while(true)
	{
		memset(revBuffer , 0 , sizeof(revBuffer));
		int revLength = recv(listen_ , revBuffer , sizeof(revBuffer) , 0);
		if(revLength == 0)
		{
			cout << "disconnect." << endl;
			break;
		}

		int i=0;
		while(revBuffer[i] != -52 && revBuffer[i] != 0)
		{i++;}
		revBuffer[i] = '\0';

		memcpy(_revBuffer , revBuffer, revLength);

		for(int i=1 ;i< revLength ;i++)
		{
			printf("_%d",(int)_revBuffer[i]);
		}

		if(_revBuffer[0] != revLength)
		{
			printf("-丢弃\n");
			continue;//丢弃
		}else printf("\n");

		MessageUnit msgUnit;
		msgUnit.msgType = _revBuffer[1];

		switch (_revBuffer[1])
		{
		case 1:{
			msgUnit.clickCode = _revBuffer[2];
			   }break;
		case 2:{
			msgUnit.dir = _revBuffer[2];
			msgUnit.dis = _revBuffer[3] + (_revBuffer[4] << 8) + (_revBuffer[5] << 16) + (_revBuffer[6] << 24);
			   }break;
		case 3:{
			msgUnit.dir = _revBuffer[2];
			msgUnit.dis = _revBuffer[3] + (_revBuffer[4] << 8);
			   }break;
		case 0:{
			time_t nowtime;  
			nowtime = time(NULL); //获取当前时间  
			keepQueue.push(nowtime);
			if(!keep)
			{
				HANDLE KC = CreateThread(NULL , 0 , KeepConnected , NULL , 0 ,NULL);
			}
			   }break;
		default:
			continue;
		}

		msgQueue.push(msgUnit);
	}
	closesocket(listen_);
	WSACleanup();

}