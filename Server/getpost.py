from flask import Flask, abort, request, jsonify
import api

app = Flask(__name__)

res = {}

@app.route('/',methods=['POST'])
def ddgd():
    r = request.json
    print(dir(request))
    if request.json['apicode'] == 1:
        res["statu"] = api.addUser(r["newAcnt"],r["newPwd"])
        return jsonify(res)


if __name__ == '__main__':
    app.run(host = '0.0.0.0',port = 5000)