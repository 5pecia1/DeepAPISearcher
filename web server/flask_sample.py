from flask import Flask, render_template, request

app = Flask(__name__)


@app.route('/', methods=["POST", "GET"])
def index():
    datas = []

    if request.method == 'POST':
        datas = [request.form['query']] * 5

    return render_template("index.html", datas=datas)

if __name__ == '__main__':
    app.run(debug=True)
