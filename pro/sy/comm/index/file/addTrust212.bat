REM �������վ��IP
REM http://10.10.11.212
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\ZoneMap\Ranges\Range1000" /v ":Range" /t REG_SZ /d 10.10.11.212 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\ZoneMap\Ranges\Range1000" /v "http" /t REG_DWORD /d 2 /f

REM �������վ��Domain
REM http://oanew/
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\ZoneMap\Domains\oatestnew" /v "http" /d "2" /t REG_DWORD /f

REM ʼ������ѡ��д򿪵�������
reg add "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\TabbedBrowsing" /v "PopupsUseNewWindow" /d "2" /t REG_DWORD /f

REM ��ʹ�����������ģʽ
reg add "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\CommandBar" /v "ShowCompatibilityViewButton" /d "0" /t REG_DWORD /f

REM ɾ������ģʽ
reg delete "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\CommandBar" /f

REM ɾ������ģʽ����ӵ���ַ
reg delete "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\BrowserEmulation\ClearableListData" /v "UserFilter" /f

REM ���������XSSɸѡ��
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1409" /d "3" /t REG_DWORD /f

REM ��������ѡ�ʱ��ʼ���л�����ѡ�
reg add "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\TabbedBrowsing" /v "OpenInForeground" /d "1" /t REG_DWORD /f

REM ÿ�η�����ҳʱ��ȡ���°汾
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v "SyncMode5" /d "3" /t REG_DWORD /f

REM ActiveX��ע�����
REM ֵ         ���� 
REM ------------------------------ 
REM 0        �ҵĵ��� 
REM 1        ���� Intranet ���� 
REM 2        �����ε�վ������ 
REM 3        Internet ���� 
REM 4        �����Ƶ�վ������ 
 
REM 1001     ������ǩ���� ActiveX �ؼ� 
REM 1004     ����δǩ���� ActiveX �ؼ� 
REM 1200     ���� ActiveX �ؼ��Ͳ�� 
REM 1201     ��û�б��Ϊ��ȫ�� ActiveX �ؼ����г�ʼ���ͽű����� 
REM 1405     �Ա��Ϊ�ɰ�ȫִ�нű��� ActiveX �ؼ�ִ�нű� 
REM 2201     ActiveX �ؼ��Զ���ʾ

reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1001" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1004" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1200" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1201" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1405" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2201" /t REG_DWORD /d 0 /f

REM ����������ֹ�����ע����� 
REM WshShell.RegWrite("HKCU\\Software\\Microsoft\\Internet Explorer\\New Windows\\PopupMgr","no"); 
reg add "HKCU\Software\Microsoft\Internet Explorer\New Windows" /v "PopupMgr" /t REG_SZ /d no /f