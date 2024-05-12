import json

# Загрузка данных из файла
def load_data(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        data = json.load(file)
    return data

# Пример функции для предсказания меток NER
def predict_ner(sentences):
    predictions = []
    for sentence in sentences:
        for token in sentence:
            # Ваш алгоритм предсказания меток NER
            # В данном примере предполагаем, что все метки равны 'O'
            predictions.append('O')
    return predictions

# Загрузка данных
file_path = './data/Soc_Net_Task_3_File_2.json'
data = load_data(file_path)

# Получение предсказаний
predictions = predict_ner(data['sentences'])

# Вывод предсказаний для всех токенов
print(", ".join(predictions))
