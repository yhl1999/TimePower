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
        res["statu"] = api.crtAct(r["userAcnts"],r["actType"],r["actInfo"])
    elif request.json['apicode'] == 3:
        res["statu"] = api.login(r["userAcnt"],r["userPwd"])
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
        res["roleList"] = api.userRoles(r["userAcnt"])
    elif request.json['apicode'] == 10:
        res = api.getUserInfo(r["userAcnt"])
    elif request.json['apicode'] == 11:
        res["statu"] = api.getActStatu(r["aid"])
    elif request.json['apicode'] == 12:
        res["actList"] = api.getActHistory(r["userAcnt"])
    else:
        #调用了错误的接口号
        res["error"] = 1
    return jsonify(res)

if __name__ == '__main__':
    app.run(host = '0.0.0.0',port = 5000)
    #app.run()