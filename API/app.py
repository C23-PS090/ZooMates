from flask import Flask
from Routes.predictRoute import predict_route

app = Flask(__name__)

app.register_blueprint(predict_route)

if __name__ == '__main__':
    app.run()
