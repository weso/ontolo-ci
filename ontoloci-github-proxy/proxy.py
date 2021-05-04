from flask import Flask,request
import requests
import json

app = Flask(__name__)

@app.route('/')
def example():
    return "SUCCESS"

@app.route('/github-proxy',methods=['POST'])
def github_proxy():
    github_header = request.headers.get('X-GitHub-Event')
    url = 'http://localhost:8090/api/v1/github/'
    data = json.loads(request.data)
    requests.post(url,json=data,headers={'X-GitHub-Event':github_header})
    return "SUCCESS"


if __name__ == "__main__":
    app.run(host='0.0.0.0')