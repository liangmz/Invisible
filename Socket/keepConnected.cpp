#include "HANDLE.h"


DWORD WINAPI KeepConnected(LPVOID par)
{
	time_t pretime;  
	pretime = time(NULL); //获取当前时间  
 
	while (true)
	{
		long now = keepQueue.front();
		keepQueue.pop();

		if(now - pretime > 10)
		{
			keep = false;
			break;
		}
		keep = true;
		pretime = now;

		if(!_isOption)
		{
			HANDLE OP = CreateThread(NULL , 0 , Option , NULL , 0 , NULL);
		}

		Sleep(10000);

	}
	return NULL;
}

