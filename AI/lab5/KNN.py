import pandas as pd
import queue
import math


# Определение Евклидова растояния между точками
def dist(x1, x2):
    s = 0
    for key in x1.keys():
        s += (x1[key] - x2[key])**2
    return math.sqrt(s)


# Определение точности модели по матрице ошибок
def accuracy(matrix):
    return (matrix['TP']+matrix['TN'])/(matrix['TP']+matrix['TN']+matrix['FN']+matrix['FP'])


# Определение матрицы ошибок для модели
def confusion_matrix(test, predicted):
    matrix = {'TP': 0, 'FP': 0, 'FN': 0, 'TN': 0}
    for i in range(len(test)):
        t = test.iloc[i]
        p = predicted.iloc[i]
        if t == 1:
            if p == 1:
                matrix['TP'] += 1
            else:
                matrix['FP'] += 1
        else:
            if p == 1:
                matrix['FN'] += 1
            else:
                matrix['TN'] += 1
    return matrix


class KNN:

    def __init__(self):
        self.classes = {0: [], 1: []}

    # Тренировка модели на данных
    def fit(self, x, y):
        for i in x.index:
            item = x.loc[i]
            self.classes[y.loc[i]].append(item)

    # Предсказание данных
    def predict(self, k, x):
        predicted = pd.DataFrame(columns=['Outcome'])
        for i in x.index:
            distances = queue.PriorityQueue()
            neighbours = {0: 0, 1: 0}
            for key in self.classes:
                for item in self.classes[key]:
                    distances.put((dist(item, x.loc[i]), key))
            for j in range(k):
                it = distances.get()
                neighbours[it[1]] += 1
            if neighbours[0] > neighbours[1]:
                predicted.loc[i] = 0
            else:
                predicted.loc[i] = 1
        return predicted['Outcome']
