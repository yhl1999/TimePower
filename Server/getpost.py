from flask import Flask, abort, request, jsonify
import json
import api


app = Flask(__name__)


@app.route('/',methods=['POST'])
def home():
    r = request.json
    res = {}
    print(r)
    if request.json['apicode'] == 1:
        res["statu"] = api.addUser(r["newAcnt"],r["newPwd"])
    elif request.json['apicode'] == 2:
        res["statu"] = api.crtAct(r["userAcnt"],r["actType"],r["actInfo"],r["startT"])
    elif request.json['apicode'] == 3:
        res["statu"] = api.getRole(r["uid"],r["rid"])
    elif request.json['apicode'] == 4:
        res["statu"] = api.changePwd(r["userAcnt"],r["oldPwd"],r["newPwd"])
    elif request.json['apicode'] == 5:
        res["statu"] = api.changeNickname(r["userAcnt"],r["newName"])
    elif request.json['apicode'] == 6:
        res["statu"] = api.changeRole(r["userAcnt"],r["rid"])
    elif request.json['apicode'] == 7:
        res["statu"] = api.changeHpic(r["userAcnt"],r["newHpic"])
    elif request.json['apicode'] == 8:
        res["statu"] = api.actStatus(r["aid"],r["statuCode"])
    elif request.json['apicode'] == 9:
        res["roleList"] = api.usersRole(r["uid"])
    elif request.json['apicode'] == 10:
        res = api.getUserInfo(r["userAcnt"])
    elif request.json['apicode'] == 11:
        res["statu"] = api.getActStatu(r["aid"])
    else:
        #调用了错误的接口号
        res["error"] = 1
    return jsonify(res)

if __name__ == '__main__':
    app.run(host = '0.0.0.0',port = 5000)
    #app.run()