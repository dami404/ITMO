import pandas as pd
from KNN import KNN, accuracy, confusion_matrix
import random


def printRes(t, p):
    matrix = confusion_matrix(t, p)
    print(f'Точность: {accuracy(matrix)}')
    print(f'Матрица ошибок: {matrix}\n')


# Загружаем датасет
pd.set_option('display.max_columns', 11)
pd.set_option('display.width', 800)
data = pd.read_csv('diabetes.csv')

# Предобработка данных
data = data.astype(float)
keys = data.keys()
values = {}
for i in range(len(keys)-1):
    values[keys[i]] = [0, 0]
for i in range(len(data)):
    item = data.iloc[i]
    for key in values:
        val = item[key]
        if val > 0:
            values[key][0] += val
            values[key][1] += 1
mediums = {}
for key in values:
    mediums[key] = values[key][0]/values[key][1]
for key in mediums:
    data.loc[(data[key] == 0), key] = mediums[key]
data['Glucose'] /= 10
data['BloodPressure'] /= 10
data['SkinThickness'] /= 10
data['Insulin'] /= 100
data['BMI'] /= 10
data['Age'] /= 10

# Визуализация статистик
print(data)
print(data.describe())
print(data.corr())

# Разделение на тренировочную и тестовую выборки
train = data.sample(frac=0.8, random_state=0)
test = data.drop(train.index)

# Тренировка первой модели
x_train1 = train[['Age', 'Insulin', 'Glucose']]
y_train = train['Outcome']
knn1 = KNN()
knn1.fit(x_train1, y_train)

# Тренировка второй модели
count = random.randint(1, len(train.keys())-1)
indexes = [i for i in range(len(train.keys())-1)]
params = []
for i in range(count):
    j = random.randint(0, len(indexes)-1)
    params.append(train.keys()[indexes[j]])
    indexes.pop(j)
print(params)
x_train2 = train[params]
knn2 = KNN()
knn2.fit(x_train2, y_train)

# Тестовые выборки с нужными параметрами
x_test1 = test[['Age', 'Insulin', 'Glucose']]
x_test2 = test[params]
y_test = test['Outcome']

# Анализ работы моделей при к = 5
print('При k = 5')
predicted1 = knn1.predict(5, x_test1)
predicted2 = knn2.predict(5, x_test2)
print('Модель 1')
printRes(y_test, predicted1)
print('Модель 2')
printRes(y_test, predicted2)

# Анализ работы моделей при к = 7
print('При k = 7')
predicted1 = knn1.predict(7, x_test1)
predicted2 = knn2.predict(7, x_test2)
print('Модель 1')
printRes(y_test, predicted1)
print('Модель 2')
printRes(y_test, predicted2)

# Анализ работы моделей при к = 11
print('При k = 11')
predicted1 = knn1.predict(11, x_test1)
predicted2 = knn2.predict(11, x_test2)
print('Модель 1')
printRes(y_test, predicted1)
print('Модель 2')
printRes(y_test, predicted2)
