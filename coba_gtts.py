import csv
import os
from gtts import gTTS
from playsound import playsound

with open('D:\\Testing model\\animal_des.csv', newline='') as csvfile:
  data = csv.DictReader(csvfile)
  for row in data:
    if row['name'] == 'dog':
      description = row['description']
      print (description)

voice = gTTS(description, lang="en")
voice.save("D:\\Testing model\\response.mp3")
playsound("D:\\Testing model\\response.mp3")
os.remove("D:\\Testing model\\response.mp3")