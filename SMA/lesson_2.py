import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, classification_report

data = pd.read_csv('data/train.csv')

data['text'] = data['text'].str.replace(r'\W', ' ').str.lower()

X = data['text']
y = data['class']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

vectorizer = TfidfVectorizer(max_features=5000)
X_train_tfidf = vectorizer.fit_transform(X_train)
X_test_tfidf = vectorizer.transform(X_test)

model = LogisticRegression()
model.fit(X_train_tfidf, y_train)
y_pred = model.predict(X_test_tfidf)
print("Accuracy:", accuracy_score(y_test, y_pred))
print("Classification Report:\n", classification_report(y_test, y_pred))
new_data = pd.read_csv('data/Soc_Net_Task_2_test_2.csv')

new_data['text'] = new_data['text'].str.replace(r'\W', ' ').str.lower()
new_X_tfidf = vectorizer.transform(new_data['text'])

predictions = model.predict(new_X_tfidf)

with open('data/output_file.txt', 'w') as f:
    f.write(','.join(map(str, predictions)))
