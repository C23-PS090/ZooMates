# Bangkit Team C23-PS090
Capstone Bangkit 2023

# Member
1. (MD) A146DSX3702 – Muhammad Willdhan Arya Putra – Universitas Bengkulu - [Active]
2. (CC) C146DSX2709 – Andhika Amarullah – Universitas Bengkulu - [Active]
3. (CC) C303DSX2647 – Dalbert Setiadi Salim – Universitas Pelita Harapan - [Active]
4. (ML) M151DSX1780 – Mochammad Yosi Pratikno – Universitas Brawijaya - [Active]
5. (ML) M266DSY2494 – Indah Purnamasari – Universitas Musamus Merauke - [Active]
6. (ML) M282DSX0420 – Diaz Wahyu Sya'bana – Universitas Negeri Malang - [Active]

# Theme : Education (for Kids)

# Project Title : ZooMates (Animal Detection)

# Project Summary
Our team has been dedicated to creating an animal detection application called ZooMates. Our main function is to make this application as educational advice for early childhood in getting to know the world of animals without having to be boring and at the same time introducing current technological sophistication. With the ZooMates application, you can detect animals in the form of pictures or animals directly seen with the camera feature in the application. In addition, this application also supports the Augmented Reality feature on several animals and not all of them have been made because these are still in the development stage.

# Step
Machine Learning : In the machine learning section we started by searching and collecting a dataset of animal images on Kaggle. The data we got were 90 animal classes consisting of train data, validation data and test data. The next stage is that we preprocess the data starting from adjusting the file format, image size, augmentation and we divide the training, validation and testing datasets with a ratio of 7:2:1. Then we proceed to the next stage, namely model building, where we use the Convolutional Neural Network model with transfer learning with the transfer learning model that we use, namely InceptionV3. The results of the training model that we obtained were achieving an accuracy of 86% with the same testing results. After the training process is complete, we convert the h5 model to tensorflow lite and also tensorflow js.

Mobile Development : In mobile development, I utilize Retrofit to establish a connection with the API created by our cloud computing team members. The API facilitates the uploading of image data captured using the CameraX library. Additionally, our machine learning team has contributed to the development of an augmented reality (AR) feature. Regarding the user interface and user experience (UI/UX), we have prioritized simplicity to ensure that our users can effortlessly engage with the core functionalities of the application.

Cloud Computing : This API uses the flask framework in python which reads the h.5 model and then describes and creates an image and convulsion matrix according to the final model of machine learning. This fire captures the image and then matches it with the model of 90 animal classes after that it produces an accuracy of how many percent of the animal matches the results of the model that has been made. after that this API also retrieves the description of the animal that is in the database with the select command according to the animal's name from the scan of the animal itself. Then we develop this API with a compute engine,cloud sql and firewall because it is cheap and easy to change its specifications according to client requests such as being able to change the ram cpu and hard disk as needed with flexibility and for the database I tarok on the sql cloud for an estimated monthly cost of 80$.

# Dataset Link
Here is the link for the animal image dataset we used in this project:
https://drive.google.com/drive/folders/1F6RbFzfshm80cYmX0ReiSAmvD4KXxzUO?usp=sharing

# Machine Learning Model
For our machine learning model in tflite, tfjs, and h5 formats can be found here:
https://drive.google.com/drive/u/4/folders/1KfpVHk3mbOy7T4IBGfuqlQ7aCH_nMUQw

# Full API Backend
https://drive.google.com/drive/folders/1rScRp9FxdP3CQKG8gBGOQ1EsjFQWQjBB?usp=sharing

# Application
Our application source code in this repository is without the 3d animal model but if you want to use the 3d model for AR, you can found the full zip file or build apk on this link:
https://drive.google.com/drive/u/4/folders/17jRFm8g1irAYO7WyQvF-5Q_ZPtBH-j91
Extract the zip model in Zoomates\app\src\main\assets\models and run the app.

For the AR features in our application, we use code from the ARCore public repository and add a little code to create dynamic 3d model calls according to the predicted results. We are using version 1.37.0 for kotlin and we have also included a link to the ARCore repository below:
https://github.com/google-ar/arcore-android-sdk

We will also obey to the ARCore Additional Terms of Service which can be found below and this link:
https://developers.google.com/ar/developers/terms 

For now, Zoomates only provides 19 animals that can be displayed in AR as shown in the list below:
['cat', 'crab', 'crocodile', 'dog', 'dolphin', 'duck', 'elephant', 'fox', 'frog', 'goldfish', 'goose', 'grasshopper', 'harborseal', 'hyena', 'iguana', 'kangaroo', 'killerwhale', 'lizard', 'monkey']
Actually these models are free models which are published on several websites like free3d.com and sketchfab.com.
