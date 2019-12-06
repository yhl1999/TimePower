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

localhost = "http://localhost:5000/"
cloud = "http://121.36.56.36:5000/"
def test1(url):
    d = {'apicode':1,'newAcnt':"testnohub2",'newPwd':"abc"}
    r = requests.post(url,json = d)
    print(r.text)
#单人创建
def test2_1(url):
    d = {'apicode':2,'userAcnts':["testaccount1"],"actType":1,'actInfo':""}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)
#组队创建
def test2_2(url):
    d = {'apicode':2,'userAcnts':["testaccount2","testaccount3"],"actType":1,'actInfo':""}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test3(url):
    d = {'apicode':3,'userAcnt':'testaccount2','userPwd':'newPwd'}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test4(url):
    d = {'apicode':4,'userAcnt':'testaccount2','oldPwd':'abc','newPwd':'newPwd'}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test5(url):
    d = {'apicode':5,'userAcnt':'testaccount2','newName':'changeNickname'}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test6(url):
    d = {'apicode':6,'userAcnt':'testaccount2','rid':1}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test7(url):
    d = {'apicode':7,'userAcnt':'testaccount2','newHpic':"/pic"}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test8(url):
    d = {'apicode':8,'aid':1,'statuCode':-1}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test9(url):
    d = {'apicode':9,'userAcnt':'testaccount2'}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test10(url):
    d = {'apicode':10,'userAcnt':"testnohub2"}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test11(url):
    d = {'apicode':11,'aid':1}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test12(url):
    d = {'apicode':12,'userAcnt':'testaccount2'}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)  

def test13(url):
    d = {'apicode':13,'userAcnt':'Lyh'}
    r = requests.post(url,json = d)
    print(r)
    print(r.text) 

def test14(url):
    d = {'apicode':14,'userAcnt':'Lyh','actType':1,'actInfo':''}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test15(url):
    d = {'apicode':15,'userAcnt':'Lyh','teamIndex':0}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)

def test16(url):
    d = {'apicode':16,'teamIndex':0}
    r = requests.post(url,json = d)
    print(r)
    print(r.text)
test14(localhost)
test14(localhost)
test14(localhost)
test16(localhost)
test14(localhost)
