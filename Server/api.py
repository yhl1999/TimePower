import pymysql
import time
import datetime
from sqlalchemy import *
from sqlalchemy.orm import create_session
from sqlalchemy.ext.declarative import declarative_base
from configs import DB_URI

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

    def __init__(self,_type,info,status,profit,buff):
        self.type = _type
        self.info = info
        self.status = status
        self.profit = profit
        self.buff = buff

class Act_User(Base):
    __table__ = Table('activity_user',metadata,autoload = True)

    def __init__(self,user_id,activity_id,start_time):
        self.user_id = user_id
        self.activity_id = activity_id
        self.start_time = start_time

class User_Role(Base):
    __table__ = Table('user_role',metadata,autoload = True)

    def __init__(self,user_id,role_id,get_time):
        self.user_id = user_id
        self.role_id = role_id
        self.get_time = get_time
    
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
            print("账号创建成功")
            return 1 #账号创建成功
        else:
            print("账号创建失败")
            return 0 #账号创建失败
#修改金币数 未测试
def changeCoin(user_id,code,num):
    e = session.query(User).filter(User.id == user_id)
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

#修改头像 未测试
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

#修改昵称 未测试
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
        e.nickname = newName
        session.begin()
        session.commit()
        return 0

#创建活动 未测试
def crtAct(userAcnts,actType,actInfo,startT):
    _type = actType
    info = actInfo
    status = 0
    profit = calprofit(actType,actInfo)
    buff = 1
    #创建Activity表项
    newact = Activity(_type,info,status,profit,buff)
    session.begin()
    session.add(newact)
    session.commit()

    #创建activity—user表项
    for userAcnt in userAcnts:
        aid = newact.id
        uid = acnt_to_id(userAcnt)
        crtU_A(uid,aid,startT)


#初始收益计算 未测试
def calprofit(actType,actInfo):
    res = 50
    pass
    return res

#创建用户-活动表项 未测试
def crtU_A(uid,aid,startT):
    user_id = uid
    activity_id = aid
    start_time = startT
    newUA = Act_User(user_id,activity_id,start_time)
    session.begin()
    session.add(newUA)
    session.commit()

#账号查询id 未测试
def acnt_to_id(acnt):
    e = User.ifAcntExist(acnt)
    if isinstance(e,int):
        return -1
    else:
        return e.id

#获得角色 未测试
def getRole(uid,rid,getT):
    newrole = User_Role(uid,rid,getT)
    session.begin()
    session.add(newrole)
    session.commit()

#查询用户角色 未测试
def usersRole(uid):
    res = session.query(User_Role.role_id).filter(User_Role.user_id == uid).all()
    return res    

#修改主角色 未测试
def changeRole(userAcnt,rid):
    e = User.ifAcntExist(userAcnt)

    if (isinstance(int,e)):
        return False
    else :
        uid = acnt_to_id(userAcnt)
        rolelist = usersRole(uid)
        if rid in rolelist:
            e.role = rid
            session.begin()
            session.commit()
            return True
        else:
            return False
    
#活动结算 未测试
def actStatus(aid,statuCode):
    act = session.query(Activity).filter(Activity.id == aid)
    if(act.status == 0):
        act.status = statuCode
        if statuCode == 1:
            userlist = session.query(Act_User.user_id).filter(Act_User.activity_id == aid).all()
            pfit = int(act.profit * act.buff)
            for userid in userlist:
                changeCoin(userid,1,pfit)
        return True
    else :
        return False

#查询账号信息 未测试
def getUserInfo(userid):
    e = session.query(User).filter(User.id == userid)
    return {"username":e.username,"headpic":e.headpic,"coin":e.coin,"role":e.role}

#活动状态查询 未测试
def getActStatu(aid):
    e = session.query(Activity).filter(Activity.id == aid).one()
    return e.status
