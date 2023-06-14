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
Machine Learning : In the machine learning section we started by searching and collecting a dataset of animal images on Kaggle. The data we got were 90 animal classes consisting of train data, validation data and test data. The next stage is that we preprocess the data starting from adjusting the file format, image size, augmentation and we divide the training, validation and testing datasets with a ratio of 7:2:1. Then we proceed to the next stage, namely model building, where we use the Convolutional Neural Network model with transfer learning with the transfer learning model that we use, namely InceptionV3. The results of the training model that we obtained were achieving an accuracy of 86% with the same testing results. After the training process is complete, we convert the h5 model to tensorflow lite and also tensorflow js. Another feature of this application is that we use the voice feature provided by Google, namely Google Text to Speech.

Mobile Development : In mobile development, I utilize Retrofit to establish a connection with the API created by our cloud computing team members. The API facilitates the uploading of image data captured using the CameraX library. Additionally, our machine learning team has contributed to the development of an augmented reality (AR) feature. Regarding the user interface and user experience (UI/UX), we have prioritized simplicity to ensure that our users can effortlessly engage with the core functionalities of the application.

Cloud Computing : This api uses the flask framework in python which reads the h.5 model and then describes and creates an image and convulsion matrix according to the final model of machine learning. This fire captures the image and then matches it with the model of 90 animal classes after that it produces an accuracy of how many percent of the animal matches the results of the model that has been made. after that this fire also retrieves the description of the animal that is in the database with the select command according to the animal's name from the scan of the animal itself. Then we develop this fire with a compute engine because it is cheap and easy to change its specifications according to client requests such as being able to change the ram cpu and hard disk as needed with flexibility and for the database I tarok on the sql cloud for an estimated monthly cost of 50$.

# Dataset Link 
https://drive.google.com/file/d/1p1bjyxmaB3C6Qy9o_4Fdg1i4e7UCqHvR/view?usp=drive_link 
