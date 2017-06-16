from flask import Flask, render_template, request

app = Flask(__name__)


@app.route('/', methods=["POST", "GET"])
def index():
    inputData = ""
    datas = []

    if request.method == 'POST':
        inputData = request.form['query']
        datas = [inputData] * 5

    return render_template("index.html", datas=datas, inputData=inputData)

if __name__ == '__main__':
    app.run(debug=True)
