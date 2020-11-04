from flask import Flask,request
import requests
import json

app = Flask(__name__)

@app.route('/')
def example():
    return "SUCCESS"

@app.route('/github-proxy',methods=['POST'])
def github_proxi():
    print(json.loads(request.data))
    requests.post('http://156.35.82.22:80/api/v1/github/', json=json.loads(request.data))
    return "SUCCESS"


if __name__ == "__main__":
    app.run(host='156.35.82.22')