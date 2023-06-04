from flask import request, Blueprint
from Controller.predictController import predictanimal

predict_route = Blueprint('predictRoute', __name__)
@predict_route.route('/predict', methods=['POST'])
def predict():
    return predictanimal(request)
