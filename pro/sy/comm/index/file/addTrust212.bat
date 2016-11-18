REM 添加信任站点IP
REM http://10.10.11.212
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\ZoneMap\Ranges\Range1000" /v ":Range" /t REG_SZ /d 10.10.11.212 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\ZoneMap\Ranges\Range1000" /v "http" /t REG_DWORD /d 2 /f

REM 添加信任站点Domain
REM http://oanew/
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\ZoneMap\Domains\oatestnew" /v "http" /d "2" /t REG_DWORD /f

REM 始终在新选项卡中打开弹出窗口
reg add "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\TabbedBrowsing" /v "PopupsUseNewWindow" /d "2" /t REG_DWORD /f

REM 不使用浏览器兼容模式
reg add "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\CommandBar" /v "ShowCompatibilityViewButton" /d "0" /t REG_DWORD /f

REM 删除兼容模式
reg delete "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\CommandBar" /f

REM 删除兼容模式下添加的网址
reg delete "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\BrowserEmulation\ClearableListData" /v "UserFilter" /f

REM 禁用浏览器XSS筛选器
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1409" /d "3" /t REG_DWORD /f

REM 当创建新选项卡时，始终切换到新选项卡
reg add "HKEY_CURRENT_USER\Software\Microsoft\Internet Explorer\TabbedBrowsing" /v "OpenInForeground" /d "1" /t REG_DWORD /f

REM 每次访问网页时获取最新版本
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v "SyncMode5" /d "3" /t REG_DWORD /f

REM ActiveX的注册表项
REM 值         设置 
REM ------------------------------ 
REM 0        我的电脑 
REM 1        本地 Intranet 区域 
REM 2        受信任的站点区域 
REM 3        Internet 区域 
REM 4        受限制的站点区域 
 
REM 1001     下载已签名的 ActiveX 控件 
REM 1004     下载未签名的 ActiveX 控件 
REM 1200     运行 ActiveX 控件和插件 
REM 1201     对没有标记为安全的 ActiveX 控件进行初始化和脚本运行 
REM 1405     对标记为可安全执行脚本的 ActiveX 控件执行脚本 
REM 2201     ActiveX 控件自动提示

reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1001" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1004" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1200" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1201" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1405" /t REG_DWORD /d 0 /f
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2201" /t REG_DWORD /d 0 /f

REM 弹出窗口阻止程序的注册表项 
REM WshShell.RegWrite("HKCU\\Software\\Microsoft\\Internet Explorer\\New Windows\\PopupMgr","no"); 
reg add "HKCU\Software\Microsoft\Internet Explorer\New Windows" /v "PopupMgr" /t REG_SZ /d no /f