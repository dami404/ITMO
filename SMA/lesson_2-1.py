from csv import reader

from random import randrange

from rich import print
import torch
from aniemore.recognizers.text import TextRecognizer
from aniemore.models import HuggingFaceModel

model = HuggingFaceModel.Text.Bert_Tiny
device = 'cuda' if torch.cuda.is_available() else 'cpu'

tr = TextRecognizer(model=model, device=device)

text = 'если ты не прекратишь, я тебя убью!'
result = tr.recognize(text, return_single_label=True)
print(result)

emotions = {
    'anger': 0,
    'disgust': randrange(2),
    'enthusiasm': 1,
    'fear': randrange(2),
    'happiness': 1,
    'neutral': randrange(2),  # или 0 ?
    'sadness': 0
}

# 'anger': 0,
# 'disgust': 0,
# 'enthusiasm': 1,
# 'fear': 0,
# 'happiness': 1,
# 'neutral': 0,
# 'sadness': 0

arr = []
with open("./data/Soc_Net_Task_2_test_2.csv", "rt", newline="", encoding="utf-8") as f_in:
    r = reader(f_in)
    next(r)
    while r:
        try:
            _, text = next(r)
            result = tr.recognize(text, return_single_label=True)
            arr.append(emotions[result])
        except StopIteration:
            break

with open("data/lesson_2_answers.txt", "w") as f:
    s = ""
    for i in arr:
        s += str(i) + ','
    f.write(s)

