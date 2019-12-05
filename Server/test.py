import api
import requests
session = api.session
User = api.User

def show(rest):
    for i in rest:
        print(i.account +" "+i.password+" "+str(i.coin))
    print ('-'*15)

#addUser测试
def tst_addUser():
    act = "tst_adduser2.3"
    pwd = "123456"
    rest = session.query(User).all()
    show(rest)

    api.addUser(act,pwd)

    rest = session.query(User).all()
    show(rest)




#changeCoin测试
def tst_changeCoin():
    act = "tst_changeCoin2.4"
    pwd = "123456"
    rest = session.query(User).all()
    show(rest)

    User.addUser(act,pwd)
    #增加金币
    User.changeCoin(act,1,100)

    rest = session.query(User).all()
    show(rest)
    #减少金币
    User.changeCoin(act,0,50)

    rest = session.query(User).all()
    show(rest)

#changePwd测试
def tst_changePwd():
    act = "tst_changePwd1.0"
    pwd = "123456"
    npwd = "abcde"
    User.addUser(act,pwd)
    rest = session.query(User).all()
    show(rest)
    
    User.changePwd(act+"s",pwd,npwd)
    rest = session.query(User).all()
    show(rest)

    User.changePwd(act,pwd,npwd)
    rest = session.query(User).all()
    show(rest)

    User.changePwd(act,pwd,npwd)
    rest = session.query(User).all()
    show(rest)
#tst_addUser()
#tst_changeCoin()
#tst_changePwd()

def test1():
    d = {'apicode':1,'newAcnt':"testnohub2",'newPwd':"abc"}
    r = requests.post("http://121.36.56.36:5000/",json = d)
    print(r.text)

def test2():
    d = {'apicode':10,'userAcnt':"testnohub2"}
    r = requests.post("http://121.36.56.36:5000/",json = d)
    print(r)
    print(r.text)
test2()