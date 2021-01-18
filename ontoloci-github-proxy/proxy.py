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
    url = 'http://156.35.82.21:8089/api/v1/github/'+ github_header
    data = json.loads(request.data)
    requests.post(url,json=data)
    return "SUCCESS"


if __name__ == "__main__":
    app.run(host='156.35.82.21')