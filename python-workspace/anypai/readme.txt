1. I'm now using basic version of AutoHotkey, so make sure .ahk is acsii based, not utf-8, otherwise, Chinese will be weird.
2. I'm using basic version of AutoHotkey, since IE_InjectJS function is based on basic version not AutoHotkey_L version.
3. For basic version of AutoHotkey, make sure using SendU/SendA function rather than Send function to send words, or Chinese may be weird.

PyQt may fail to work for https sites.
Solutions: You need to install OpenSSL Win32 or Win64 binaries.
Keep libeay32.dll and ssleay32.dll in your current path running main python OR the same place where your QtNetwork4.dll or QtNetworkd4.dll is located.
libeay32.dll and ssleay32.dll are getting from below:
1. http://www.slproweb.com/products/Win32OpenSSL.html
2. Download the latest "light" Win32 or Win64 installation package, for example "Win32 OpenSSL v0.9.8l Light".
3. Install it to any location. Ignore "Microsoft Visual C++ 2008 Redistributables" warning (click OK) and select copying OpenSSL DLLs to "The OpenSSL binaries (\bin) directory".
4. Copy libeay32.dll and ssleay32.dll from the \bin folder

Make sure you have some way to put npaliedit.dll to "user's Application Data\Mozilla\plugins" folder (Create this folder manually if missing ?)
Otherwise, We can't pass the taobao security activex control check. This dll is coming from npaliedit.exe which is suggested to install missing plugins when you visit taobao login by firefox.
Or, just install npaliedit.exe on a machine without mozilla ? Will this work for our qt webkit ? (TODO)
For linux: just "sh aliedit.sh" to install this security activex control, it's getting from the page where you pay the zhifubao, it will ask you to download and install this.
