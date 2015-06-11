#include "HANDLE.h"

queue<MessageUnit> msgQueue;//消息队列
bool keep = false;//客户端开启开关 - 保持开启
bool _isOption = false;//操作开关
queue<long> keepQueue;

