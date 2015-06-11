#include "HANDLE.h"

int _DIR_X = 0;//鼠标移动方向
int _DIR_Y = 0;//

double _DIR_LV_X = 2;
double _DIR_LV_Y = 2;

DWORD WINAPI _MouseX(LPVOID par);
DWORD WINAPI _MouseY(LPVOID par);
double lvToSp(int lv);


DWORD WINAPI Option(LPVOID par)
{
	_isOption = true;

	HANDLE MX = CreateThread(NULL , 0 , _MouseX , NULL , 0 ,NULL);
	HANDLE MY = CreateThread(NULL , 0 , _MouseY , NULL , 0 ,NULL);

	while (keep)
	{
		while (!msgQueue.empty())
		{
			MessageUnit msgUnit = msgQueue.front();
			msgQueue.pop();

			if(msgUnit.msgType == 1)//点击
			{
				switch (msgUnit.clickCode)
				{
				case 11 :{//左键
					mouse_event(MOUSEEVENTF_LEFTDOWN,NULL,NULL,NULL,NULL);
					mouse_event(MOUSEEVENTF_LEFTUP,NULL,NULL,NULL,NULL);
						 }break;
				case 12:{//右键
					mouse_event(MOUSEEVENTF_RIGHTDOWN,NULL,NULL,NULL,NULL);
					mouse_event(MOUSEEVENTF_RIGHTUP,NULL,NULL,NULL,NULL);
						}break;
				case 13:{//中间键
					mouse_event(MOUSEEVENTF_MIDDLEDOWN,NULL,NULL,NULL,NULL);
					mouse_event(MOUSEEVENTF_MIDDLEUP,NULL,NULL,NULL,NULL);
						}break;
				case 14:{//空格
					keybd_event(32,0,0,0);
					keybd_event(32,0,KEYEVENTF_KEYUP,0);
						}break;
				case 15:{//回车
					keybd_event(108,0,0,0);
					keybd_event(108,0,KEYEVENTF_KEYUP,0);
						}break;
				default:
					break;
				}
			}
			else if(msgUnit.msgType == 2)//触控
			{
				int _dir = msgUnit.dir == 0 ? 1 : -1;
				mouse_event(MOUSEEVENTF_WHEEL,NULL,NULL,WHEEL_DELTA * _dir,NULL);;
			}
			else if(msgUnit.msgType == 3)//传感
			{
				int lv = msgUnit.angle <= 15 ? 0 : (msgUnit.angle <= 40 ? 1 : (msgUnit.angle <= 65 ? 2 : 3));
				switch (msgUnit.dir)
				{
				case 0 :{
					_DIR_Y = 1;
					_DIR_LV_Y = lvToSp(lv);
						}break;//上
				case 2 :{
					_DIR_Y = -1;
					_DIR_LV_Y = lvToSp(lv);
						}break;//下
				case 3 :{
					_DIR_X = -1;
					_DIR_LV_X = lvToSp(lv);
						}break;//左
				case 1 :{
					_DIR_X = 1;
					_DIR_LV_X = lvToSp(lv);
						}break;//右
				default:
					break;
				}
			}
		}
	}
	_isOption = false;

	_DIR_X = 0;
	_DIR_Y = 0;

	return NULL;
}



DWORD WINAPI _MouseX(LPVOID par)
{
	while(keep)
	{
		POINT k;
		GetCursorPos(&k);
		SetCursorPos(k.x + _DIR_X , k.y);
		Sleep(_DIR_LV_X);
	}
	return NULL;
}

DWORD WINAPI _MouseY(LPVOID par)
{
	while(keep)
	{
		POINT k;
		GetCursorPos(&k);
		SetCursorPos(k.x , k.y + _DIR_Y);
		Sleep(_DIR_LV_Y);
	}
	return NULL;
}

double lvToSp(int lv)
{
	switch (lv)
	{
	case 0:return 2;
	case 1:return 1.6;
	case 2:return 1.3;
	case 3:return 1;
	default:
		return 2;
	}
}




