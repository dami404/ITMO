import json


def load_data(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        data = json.load(file)
    return data


def predict_ner(sentences):
    predictions = []
    for sentence in sentences:
        for token in sentence:
            predictions.append('O')
    return predictions


file_path = './data/Soc_Net_Task_3_File_2.json'
data = load_data(file_path)

predictions = predict_ner(data['sentences'])


print(", ".join(predictions))
