# coding: utf-8

from common import *

def validationForAddVote(bookName, bookId, userName, userType, exist_userName, exist_userType, recVoteNum, advVoteNum):

    # Don't add one user to a book twice, voteData.getData() means self.__map means {(bookName, bookId):[(userName, passwd), ...]}"
    voteData = appdata.getVoteData()
    voteDataMap = voteData.getData()
    if voteDataMap.has_key((bookName, bookId)):
        for temp_userName, temp_passwd in voteDataMap[(bookName, bookId)]:
            if userName == temp_userName:
                return u'\r\n用户 ' + userName + u' 已经建立和图书 ' + bookName + u' 建立投票关系。'

    # recVoteNum and advVote both should be >= 0
    if len(recVoteNum) == 0:
        return u'\r\n投票前请先点击\"刷新用户列表\"按钮。'
    if int(recVoteNum) == -1:
        return u'\r\n用户 ' + userName + u' 刷新失败, 投票前请先重新刷新该用户。'

    # Two 普通 for a book rule, Three VIP for a book rule
    if voteDataMap.has_key((bookName, bookId)):
        if not exist_userType:
            return u'\r\n发现无效用户 ' + exist_userName + u' 与图书 ' + bookName + u' 存在投票关系, 请先删除两者间的投票关系再投票'
            
        if u'普通' in userType:
            if u'VIP' in exist_userType:
                return u'\r\n您之前已使用 VIP 用户类型对图书 ' + bookName + u' 进行投票, 请使用同一种用户类型继续投票。当前用户类型为: ' + userType
            if len(voteDataMap[(bookName, bookId)]) >= 2:
                return u'\r\n为防止账号被封, 同一本图书最多使用两个普通用户类型进行投票。'
        if u'VIP' in userType:
            if u'普通' in exist_userType:
                return u'\r\n您之前已使用 普通 用户类型对图书 ' + bookName + u' 进行投票, 请使用同一种用户类型继续投票。当前用户类型为: ' + userType
            if len(voteDataMap[(bookName, bookId)]) >= 3:
                return u'\r\n为防止账号被封, 同一本图书最多使用三个VIP用户类型进行投票。'

    # ticket number should be greater than 0
    if int(recVoteNum) == 0 and int(advVoteNum) == 0:
        return u'\r\n用户 ' + userName + u' 可用推荐票, 广告票都为0。'
    
# userBig can vote all the tickets for one book while userSmall can vote one ticket for one book only.
def getRealRemainVoteNum(userBigSmall, recVoteNum, advVoteNum):
    'recVoteNum and advVoteNum should be greater than 0. The method will return (realRecVoteNum, realAdvVoteNum, remainRecVoteNum, remainAdvVoteNum)'
    if u'大号' in userBigSmall:
        realRecVoteNum = recVoteNum
    else:
        realRecVoteNum = '1' if int(recVoteNum) > 0 else '0'
    remainRecVoteNum = str(int(recVoteNum) - int(realRecVoteNum))
    realAdvVoteNum = advVoteNum
    remainAdvVoteNum = '0'
    return (realRecVoteNum, realAdvVoteNum, remainRecVoteNum, remainAdvVoteNum)

