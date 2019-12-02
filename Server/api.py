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
        if User.ifAcntExist(newAcnt) !=0 :
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

    #修改金币数 测试完毕
    def changeCoin(userAcnt,code,num):
        e = User.ifAcntExist(userAcnt)
        
        if isinstance(e,int):
            if e>1:
                print("数据错误！存在多个相同账号！")
                return 3
            elif e==0 :
                print("账号不存在")
                return 2
        else :           
            if code == 1:
                e.coin +=num
            elif code == 0:
                e.coin -=num
            else:
                print("操作码错误")
                return 1
            session.begin()
            session.commit()
            return 0
            
    
    #修改密码 测试完毕
    def changePwd(userAcnt,oldPwd,newPwd):
        e = User.ifAcntExist(userAcnt)

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
        e = User.ifAcntExist(userAcnt)

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

    def changeNickname(userAcnt,newName):
        e = User.ifAcntExist(userAcnt)

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
    
class Role(Base):
    __table__ = Table('role',metadata,autoload = True)

class Activity(Base):
    __table__ = Table('activity',metadata,autoload = True)

class Act_User(Base):
    __table__ = Table('activity-user',metadata,autoload = True)

class User_Role(Base):
    __table__ = Table('user-role',metadata,autoload = True)