import pymysql
import time
import datetime
import random
from sqlalchemy import *
from sqlalchemy.orm import create_session
from sqlalchemy.ext.declarative import declarative_base
from configs import DB_URI
import configs
from que import QUEUE

engine = create_engine(DB_URI)
Base = declarative_base()
metadata = MetaData(bind=engine)
session = create_session(bind = engine)

class User(Base):
    __table__ = Table('user',metadata,autoload = True)

    #构造函数
    def __init__(self,account,password,username,rdate,headpic,coin,role):
        self.account = account
        self.password = password
        self.username = username
        self.rdate = rdate
        self.headpic = headpic
        self.coin = coin
        self.role = role

class Role(Base):
    __table__ = Table('role',metadata,autoload = True)

class Activity(Base):
    __table__ = Table('activity',metadata,autoload = True)

    def __init__(self,_type,info,status,profit,buff,startdate):
        self.type = _type
        self.info = info
        self.status = status
        self.profit = profit
        self.buff = buff
        self.startdate = startdate

class Act_User(Base):
    __table__ = Table('activity_user',metadata,autoload = True)

    def __init__(self,user_id,activity_id):
        self.user_id = user_id
        self.activity_id = activity_id

class User_Role(Base):
    __table__ = Table('user_role',metadata,autoload = True)

    def __init__(self,user_id,role_id,getdate):
        self.user_id = user_id
        self.role_id = role_id
        self.getdate = getdate
    
#账号存在性检验
def ifAcntExist(userAcnt):
    acnt = session.query(User).filter(User.account==userAcnt).all()
    l = len(acnt)
    if l == 1 :
        return acnt[0]
    else :
        return l



#创建新用户 测试完毕
def addUser(newAcnt,newPwd):
    if ifAcntExist(newAcnt) !=0 :
        print("账号存在")
        return -1 #账号已存在
    else:
        nowdate = datetime.datetime.now()
        nickname = newAcnt
        headpic = ""
        coin = 0
        role = 1
        newuser = User(newAcnt,newPwd,nickname,nowdate,headpic,coin,role)
        session.begin()
        session.add(newuser)    
        if session.commit() == None :
            getRole(newuser.id,1)
            print("账号创建成功")
            return 1 #账号创建成功
        else:
            print("账号创建失败")
            return 0 #账号创建失败
#修改金币数 未测试
def changeCoin(user_id,code,num):
    e = session.query(User).filter(User.id == user_id).first()
    if code == 1:
        e.coin +=num
    elif code == 0:
        e.coin -=num
    else:
        print("操作码错误")
        return False
    session.begin()
    session.commit()
    return True
        
#修改密码 测试完毕
def changePwd(userAcnt,oldPwd,newPwd):
    e = ifAcntExist(userAcnt)
    if isinstance(e,int):
        if e>1:
            print("数据错误！存在多个相同账号！")
            return 3
        elif e==0 :
            print("账号不存在")
            return 2
    else:
        if e.password == oldPwd :
            e.password = newPwd
            session.begin()
            session.commit()
            return 0
        else:
            print("原密码错误")
            return 1

#修改头像 测试完毕
def changeHpic(userAcnt,newHpic):
    e = ifAcntExist(userAcnt)
    if isinstance(e,int):
        if e>1:
            print("数据错误！存在多个相同账号！")
            return 3
        elif e==0 :
            print("账号不存在")
            return 2
    else:
        e.headpic = newHpic
        session.begin()
        session.commit()
        return 0

#修改昵称 测试完毕
def changeNickname(userAcnt,newName):
    e = ifAcntExist(userAcnt)
    if isinstance(e,int):
        if e>1:
            print("数据错误！存在多个相同账号！")
            return 3
        elif e==0 :
            print("账号不存在")
            return 2
    else:
        e.username = newName
        session.begin()
        session.commit()
        return 0

#修改主角色 测试完毕
def changeRole(userAcnt,rid):
    e = ifAcntExist(userAcnt)

    if (isinstance(e,int)):
        return False
    else :
        rolelist = userRoles(userAcnt)
        print(rolelist)
        if rid in rolelist:
            e.role = rid
            session.begin()
            session.commit()
            return True
        else:
            return False

#创建活动表项
def crtAct(actType,actInfo,actstatu = 0):
    _type = actType
    info = actInfo
    status = actstatu
    profit = calprofit(actType,actInfo)
    buff = 1
    aid = -1
    startT = datetime.datetime.now()
    #创建Activity表项
    newact = Activity(_type,info,status,profit,buff,startT)
    session.begin()
    session.add(newact)
    session.commit()
    aid = newact.id
    return aid

#创建单人活动 测试完毕
def crtOneAct(userAcnts,actType,actInfo):
    aid = crtAct(actType,actInfo)
    #创建activity—user表项
    if aid == -1:
        return False
    for userAcnt in userAcnts:
        uid = acnt_to_id(userAcnt)
        crtU_A(uid,aid)
    return True

#初始收益计算 未测试
def calprofit(actType,actInfo):
    res = 50
    pass
    return res

#创建用户-活动表项 测试完毕
def crtU_A(uid,aid):
    user_id = uid
    activity_id = aid
    newUA = Act_User(user_id,activity_id)
    session.begin()
    session.add(newUA)
    session.commit()

#账号查询id 测试完毕
def acnt_to_id(acnt):
    e = ifAcntExist(acnt)
    if isinstance(e,int):
        return -1
    else:
        return e.id

#获得角色 未测试
def getRole(uid,rid):
    getdate = datetime.datetime.now()
    newrole = User_Role(uid,rid,getdate)
    session.begin()
    session.add(newrole)
    if session.commit() == None:
        return True
    else :
        return False

#查询用户角色 测试完毕
def userRoles(userAcnt):
    uid = acnt_to_id(userAcnt)
    r = session.query(User_Role.role_id).filter(User_Role.user_id == uid).all()
    res = []
    for i in r:
        res.append(i[0])
    return res    

    
#活动结算 测试完毕
def actStatus(aid,statuCode):
    act = session.query(Activity).filter(Activity.id == aid).first()
    if(act.status == 0):
        act.status = statuCode
        if statuCode == 1:
            userlist = session.query(Act_User.user_id).filter(Act_User.activity_id == aid).all()
            pfit = int(act.profit * act.buff)
            for userid in userlist:
                changeCoin(userid[0],1,pfit)
            return pfit
        elif statuCode == -1:
            return 0
        else :
            return -1
    else :
        return -1

#查询账号信息 测试完毕
def getUserInfo(userAcnt):
    userid = acnt_to_id(userAcnt)
    e = session.query(User).filter(User.id == userid).first()
    print(e)
    return {"username":e.username,"headpic":e.headpic,"coin":e.coin,"role":e.role}

#活动状态查询 测试完毕
def getActStatu(aid):
    e = session.query(Activity).filter(Activity.id == aid).first()
    return e.status


#用户历史活动查询 测试完毕
def getActHistory(userAcnt):
    uid = acnt_to_id(userAcnt)
    aidlist = session.query(Act_User.activity_id).filter(Act_User.user_id ==uid).all()
    actList = []
    for i in aidlist:
        act = session.query(Activity).filter(Activity.id == i[0]).first()
        if act.status!= 0 :
            actList.append({'startT':act.startdate,'actInfo':act.info,'profit':act.profit*act.buff})
    return actList


#用户登录 测试完毕
def login(userAcnt,userPwd):
    e = ifAcntExist(userAcnt)
    if isinstance(e,int):
        if e>1:
            print("数据错误！存在多个相同账号！")
            return 3
        elif e==0 :
            print("账号不存在")
            return 2
    else:
        if e.password == userPwd:
            return 1
        else :
            return 0

#抽取人物 测试完毕
def randRole(userAcnt):
    e = ifAcntExist(userAcnt)
    if e==0 :
        return -1
    else:
        if e.coin < configs.CardValue:
            return 0
        else :
            uid = acnt_to_id(userAcnt)
            rlist = session.query(Role).all()
            num = len(rlist)
            x = random.randint(0,num-1)
            rid = rlist[x].id
            getRole(uid,rid)
            changeCoin(uid,0,configs.CardValue)
            return rid

#创建队伍活动 未测试
def crtTeamActivity(userAcnt,actType,actInfo):
    aid = -1
    aid = crtAct(actType,actInfo,-2)
    uid = acnt_to_id(userAcnt)
    crtU_A(uid,aid)
    if aid == -1:
        return -1
    else:
        return QUEUE.add(aid)

#加入队伍 测试完毕 
def joinActivity(userAcnt,teamindex):
    uid = acnt_to_id(userAcnt)
    aid = QUEUE.check(teamindex)
    
    if changeActStu(aid,0) :
        crtU_A(uid,aid)
        QUEUE.delt(aid)
        return 1
    else :
        return -1

#修改活动状态 测试完毕
def changeActStu(aid,statuCode):
    e = session.query(Activity).filter(Activity.id == aid).first()
    if e:
        if e.status == -2 and (statuCode==0 or statuCode == -1) :
            e.status = statuCode
            return True
        elif e.status == 0 and (statuCode == -1 or statuCode == 1):
            e.status = statuCode
            return True
        else :
            return False
    else :
        #活动不存在
        return False

#结束组队 测试完毕
def endTeamActivity(teamindex):
    aid = QUEUE.check(teamindex)
    QUEUE.delt(aid)
    if aid == -1:
        return -1
    else:
        if changeActStu(aid,-1):
            return 1
        else :
            return 0
