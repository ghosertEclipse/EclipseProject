# coding: utf-8

import re

from PyQt4.QtCore import QObject, SIGNAL

import twill

from httpclient import HttpClient

class Qidian(QObject):

    def __init__(self):
        QObject.__init__(self)

    def __emitStatus(self, status):
        QObject.emit(self, SIGNAL('qidianStatus'), status)

    def __emitVoteOk(self, userName, bookName, recVoteNum, advVoteNum):
        QObject.emit(self, SIGNAL('qidianVoteOk'), userName, bookName, recVoteNum, advVoteNum)

    def __voteRecommentation(self, bookId, recVoteNum, userName, bookName, isDebug = 0):
        "This method will get cookies from twill and start to vote use this cookie."
#        <Cookie cmfuToken=500E45B7AD7EDC79BECB50CC9C95D500D5B9F9B68F4563B599ECDB
#B461B0B9DB6ECDFEE2F5D2E231146CCE03EE76F4B527C27D6EDCB9F71EABCBAC4F3F1AF7C50F1732
#6F409366C98A40144AECB8EBC2CC6B7A23B3A0180861A87609677CF3990FECA8E3C71927A8D1F558
#84E25D300BF85C1ECA3D64BAD926BC169D91D5C28E879F6E68CE4CD8AD2FA1D29429E505B96304F7
#B317425AFD807043C7F4425C16A0ACDE2E3280FE10B48BACFAC10650D382F10D058B1C453AAD8247
#B3152B98B6041BC1FBA8DD7D4DEBD987BA62C12E95ED061B609DCDE398E6FBDD37FC28BAB827700C
#341D63D6EC277FB0568FB3048C for .qidian.com/>
#        <Cookie cui=1959856209 for .qidian.com/>
#        <Cookie ll=2009-02-07 18:44:51 for .qidian.com/>
#        <Cookie rt=2008-06-08 21:02:09 for .qidian.com/>

        # get cookie jar
        postCookies = ''
        for cookies in twill.get_browser().cj:
            postCookies = postCookies + re.findall(r'<Cookie (.*?) for', str(cookies))[0] + '; '
        if postCookies:
            postCookies = postCookies[0:-2]

        # add cookie and start to vote.
        httpclient = HttpClient('www.qidian.com', False, isDebug)
        if postCookies:
            httpclient.cookie = postCookies

        # Add headers as below, or the vote will fail.
        headers = {'User-Agent' : 'Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6',
    'Accept' : 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language' : 'zh-cn',
    'Accept-Encoding' : 'gzip,deflate',
    'Accept-Charset' : 'gb2312,utf-8;q=0.7,*;q=0.7',
    'Keep-Alive' : '300',
    'Connection' : 'keep-alive',
    'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8',
    'CMFUAJAX-Ver' : 'ver1.0',
    'Referer' : 'http://www.qidian.com/Book/{0}.aspx'.format(bookId),
    'Pragma' : 'no-cache',
    'Cache-Control' : 'no-cache'}

        # Begin to vote with the specified recVoteNum.
        for iRec in range(int(recVoteNum)):
            self.__emitStatus(u'正在使用用户 {0} 投票图书 {1} , 第 {2} 票。。。'.format(userName, bookName, iRec + 1))
            response, data = httpclient.post_request('/ajax.aspx?opName=RecomBook', params={'bookId' : bookId}, headers=headers, enable_redirect=False)
            self.__emitVoteOk(userName, bookName, '1', '0')

        httpclient.close()

    def getTicketList(self, userNamePwdList):
        """This method should pass in a list [(userName, userPwd)] and will return [(userName, 普通|初级VIP|高级VIP, 用户积分(大号|小号), 日推荐票, 日广告票)].
           Or [(userName, u'登陆失败', u'登陆失败', '-1', '-1')]
        """
        ticketList = []
        for userName, userPwd in userNamePwdList:
            try:
                self.__emitStatus(u'正在使用用户 {0} 登录。。。'.format(userName))

                twill_script = """
                go http://www.qidian.com/User/Login.aspx
                fv 1 txtUserName {0}
                fv 1 txtPwd {1}
                submit
                """.format(userName, userPwd)
                twill.execute_string(twill_script)

                # Check whether the current url has been go to the line below, if not, means user faile to login, the clause below will throw a exception.
                twill.commands.url('http://www.qidian.com/Default.aspx')

                self.__emitStatus(u'正在访问用户 {0} 的个人书屋。。。'.format(userName))

                twill.commands.go('http://www.qidian.com/User/Index.aspx')

                # Get html and begin to parse the html.
                html = twill.commands.show()
                html = unicode(html, 'gbk')

                userType = re.search(ur'用户类型.*?>(.*?)<.*?用户', html).groups()[0]
                userScore = re.search(ur'用户积分.*?>(\d+\.?\d*)<.*?分', html).groups()[0]
                recNum = re.search(ur'日推荐票.*?现余.*?>(\d+)<.*?次', html).groups()[0]
                advNum = re.search(ur'日广告票.*?现余.*?>(\d+)<.*?次', html).groups()[0]
                if userType == u'普通':
                    bigSmall = userScore + u'(大号)' if float(userScore) > 6000 else userScore + u'(小号)'
                elif userType == u'初级VIP':
                    bigSmall = userScore + u'(大号)' if float(userScore) > 1000 else userScore + u'(小号)'
                elif userType == u'高级VIP':
                    bigSmall = userScore + u'(大号)'

                ticketList.append((userName, userType, bigSmall, recNum, advNum))
            except Exception as e:
                print e
                self.__emitStatus(u'用户 {0} 登录失败，请检查用户名密码或联系作者更新本软件。。。'.format(userName))
                ticketList.append((userName, u'登陆失败', u'登陆失败', '-1', '-1'))
            finally:
                # Clear the cookies for the next user will login.
                twill.commands.clear_cookies()

        return ticketList

    def voteTickets(self, userBookMap):
        "userBookMap should be: {(userName, userPwd):[(bookName, bookId, recVoteNum, advVoteNum)]}"

        # Return if the map is empty.
        if not userBookMap:
            return

        for userName, userPwd in userBookMap.iterkeys():
            try:
                self.__emitStatus(u'正在使用用户 {0} 登录。。。'.format(userName))

                twill_script = """
                go http://www.qidian.com/User/Login.aspx
                fv 1 txtUserName {0}
                fv 1 txtPwd {1}
                submit
                """.format(userName, userPwd)
                twill.execute_string(twill_script)

                # Check whether the current url has been go to the line below, if not, means user faile to login, the clause below will throw a exception.
                twill.commands.url('http://www.qidian.com/Default.aspx')

                bookInfoList = userBookMap[(userName, userPwd)]
                for bookInfo in bookInfoList:
                    iepop = None
                    try:
                        bookName, bookId, recVoteNum, advVoteNum = bookInfo

                        # go to book page to vote recommendation tickets.
                        if int(recVoteNum) > 0:

                            self.__emitStatus(u'正在使用用户 {0} 投票图书 {1} 。。。'.format(userName, bookName))

                            self.__voteRecommentation(bookId, recVoteNum, userName, bookName)

                        # go to adv page to vote adv tickets.
                        if int(advVoteNum) > 0:

                            # get all the adv links from the adv page.
                            self.__emitStatus(u'正在使用用户 {0} 投广告票。。。'.format(userName))
                            twill.commands.go('http://www.qidian.com/user/affiliate.aspx')
                            html = twill.commands.show()

                            advLinks = re.findall(r'<A.*?/(.*?adid.*?)".*?<SPAN>(.*?)</SPAN>', html)
                            if not advLinks:
                                advLinks = re.findall(r"<a.*?/(.*?adid.*?)'.*?<span>(.*?)</span>", html)

                            # start to vote adv tickets.
                            for iAdv in range(int(advVoteNum)):
                                if not advLinks:
                                    break
                                advLink, advName = advLinks.pop(0)
                                twill.commands.follow(advName)
                                twill.commands.back()
                                self.__emitVoteOk(userName, bookName, '0', '1')
                                self.__emitStatus(u'正在使用用户 {0} 投广告票, 第 {1} 票。。。'.format(userName, iAdv + 1))
                    except Exception as e:
                        print e
                        self.__emitStatus(u'用户 {0} 的自动投票部分失败, 请尝试再次自动投票。。。'.format(userName))
                    finally:
                        pass
            except Exception as e:
                print e
                self.__emitStatus(u'用户 {0} 登录失败，请检查用户名密码或联系作者更新本软件。。。'.format(userName))
            finally:
                # Clear the cookies for the next user will login.
                twill.commands.clear_cookies()

    def getMockTicketList(self, userPwdList):
        " return [(userName, 普通|初级VIP|高级VIP, 用户积分(大号|小号), 日推荐票, 日广告票)]. "
        li = []
        for index, (userName, pwd) in enumerate(userPwdList):
            if index == 0:
                li.append((userName, u'登陆失败', u'登陆失败', '-1', '-1'))
                continue
            if index == 1:
                li.append((userName, u'高级VIP',u'65554.2(大号)', '6', '4'))
                continue
            if index == 2:
                li.append((userName, u'初级VIP', u'65554.2(小号)', '6', '4'))
                continue
            if index == 3:
                li.append((userName, u'普通', u'65554.2(大号)', '6', '4'))
                continue
            if index == 4:
                li.append((userName, u'普通', u'65554.2(小号)', '6', '4'))
                continue
            if index == 5:
                li.append((userName, u'普通', u'65554.2(小号)', '6', '4'))
                continue
            if index == 6:
                li.append((userName, u'初级VIP', u'65554.2(小号)', '0', '4'))
                continue
            if index == 7:
                li.append((userName, u'高级VIP', u'65554.2(小号)', '6', '4'))
                continue
        return li

if __name__=='__main__':
    import threading
    class MyThread(threading.Thread):

        def slot(self, str):
            print str

        def run(self):
            qidian = Qidian()
            qidian.connect(qidian, SIGNAL('qidianStatus'), self.slot)
            for userName, userType, bigSmall, recNum, advNum in qidian.getTicketList([('echotiancaii', '477628'), ('alundra1117', 'y111111')]):
                print userName, userType, bigSmall, recNum, advNum

    MyThread().start()

    #print qidian.getMockTicketList([('1', '1'), ('2', '2')])

    # alundra1116/y111111

    #userBookMap = {('alundra1117', 'y111111'):[(u'江湖遍地卖装备', '1049673', '1', '10')]}
    #qidian.voteTickets(userBookMap)
    

