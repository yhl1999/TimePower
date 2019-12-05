from flask import Flask, request

app = Flask(__name__)
@app.route('/', methods=['GET', 'POST'])
def home():
    print(request.json)
    return '123'

if __name__ == '__main__':
    app.run()