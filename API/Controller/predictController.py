import logging 
from flask import Flask, jsonify, request
import tensorflow as tf
from tensorflow.keras.models import load_model
import numpy as np
import cv2
import mysql.connector


app = Flask(__name__)

model = load_model('Models/my_model.h5')
logging.basicConfig(level=logging.DEBUG)

@app.route('/predict', methods=['POST'])
def predictanimal():
    if 'file' not in request.files:
        logging.error('No file found')
        return jsonify({"error": 'No file found'})

    image = request.files['file']

    if image.filename == '':
        logging.error('No filename provided')
        return jsonify({'error': 'No filename provided'})

    try:
        # Read the image and convert it to the appropriate format
        img = cv2.imdecode(np.frombuffer(image.read(), np.uint8), cv2.IMREAD_COLOR)

        # Resize the image to the expected size of the model (224x224)
        resized_img = cv2.resize(img, (256, 256))

        # Preprocess the image
        processed_img = preprocess_image(resized_img)

        # Perform prediction using the model
        prediction = model.predict(processed_img)
        predicted_class = np.argmax(prediction)
        predicted_percentage = np.max(prediction) * 100

        class_labels = ['ant', 'beer', 'bee', 'beetl', 'buffalo', 'butterfly', 'camel', 'cat', 'caterpillar', 'centipde', 'chameleon', 'cheetah', 'chicken', 'cockroach',
                        'cow', 'crab', 'crocodile', 'deer', 'dog', 'dolphin', 'dragonfly', 'duck', 'eagle', 'echidna', 'eel', 'elephant', 'flamingo', 'fly', 'fox', 'frog',
                        'giraffe', 'goldfish', 'goose', 'gorilla', 'grasshopper', 'hamster', 'harborseal', 'hedgehog', 'hippopotamus', 'horse', 'hyena', 'iguana',
                        'jaguar', 'jellyfish', 'kangaroo', 'killerwhale', 'koala', 'komodo', 'leopard', 'lion', 'lionfish', 'lizard', 'llama', 'monkey', 'mosquito',
                        'moth', 'mouse', 'orangutan', 'ostrich', 'otter', 'owl', 'panda', 'parrot', 'peacock', 'penguin', 'pig', 'platypus', 'polarbear', 'rabbit',
                        'raccoon', 'rhinoceros', 'salamander', 'scorpion', 'searays', 'seaturtle', 'seaurchins', 'shark', 'sheep', 'slug', 'snail', 'snake', 'spider',
                        'squirrel', 'starfish', 'swan', 'tiger', 'turtle', 'whale', 'wolf', 'zebra']

        predicted_label = class_labels[predicted_class]

        # Establish a connection to the database
        cnx = mysql.connector.connect(
            host='localhost',
            user='root',
            password='',
            database='zoomates'
        )

        # Create a cursor object to execute SQL queries
        cursor = cnx.cursor()

        # Fetch the animal description from the database based on the predicted label
        query = "SELECT description FROM animal WHERE name_animal = %s"
        cursor.execute(query, (predicted_label,))
        result = cursor.fetchone()

        # Close the cursor and database connection
        cursor.close()
        cnx.close()

        if result:
            description = result[0]
            response = {
                'prediction': predicted_label,
                'confidence': round(predicted_percentage, 2),
                'description': description
            }
        else:
            response = {
                'prediction': predicted_label,
                'confidence': round(predicted_percentage, 2),
                'description': 'Description not found'
            }

        # Return the JSON response
        return jsonify(response)

    except Exception as e:
        logging.error(str(e))
        return jsonify({'error': 'Invalid image file'})

def preprocess_image(image):
    # Normalize the image
    image = image.astype('float32') / 255.0

    # Expand image dimensions (matching the input of the model)
    image = np.expand_dims(image, axis=0)

    return image
