from flask import request, Blueprint
from Controller.animalController import get_animal

animal_route = Blueprint('animalRoute', __name__)
@animal_route.route('/animal', methods=['GET'])
def animal():
    return get_animal()
