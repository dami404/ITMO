import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import precision_score, recall_score, f1_score

# Загрузка данных
data = pd.read_csv("./data/Task_1_prepprocessed.csv")

# Удаление строк с пропущенными значениями
data.dropna(inplace=True)

# Разделение на признаки (X) и целевую переменную (y)
X = data['body']
y = data['class']

# Разделение на тренировочный и тестовый наборы данных
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25, random_state=94)

# Использование TfidfVectorizer() на столбце body
tfidf_vect = TfidfVectorizer()
X_train_tfidf = tfidf_vect.fit_transform(X_train)
X_test_tfidf = tfidf_vect.transform(X_test)

# Обучение и оценка RandomForestClassifier с TfidfVectorizer на столбце body
rf_classifier = RandomForestClassifier(n_estimators=8, n_jobs=10, random_state=94)
rf_classifier.fit(X_train_tfidf, y_train)
y_pred = rf_classifier.predict(X_test_tfidf)

precision_1 = precision_score(y_test, y_pred, average='macro')
recall_1 = recall_score(y_test, y_pred, average='macro')
f1_1 = f1_score(y_test, y_pred, average='macro')

print("Метрики для TfidfVectorizer на столбце body:")
print("Precision (macro avg):", round(precision_1,3))
print("Recall (macro avg):", round(recall_1,3))
print("F-score (macro avg):", round(f1_1,3))

# Использование TfidfVectorizer() на объединенных в результате конкатенации столбцах subject и body
data['date'] = pd.to_datetime(data['date'])

# Извлечение дня недели
data['weekday'] = data['date'].dt.dayofweek

# Объединение столбцов 'subject' и 'body' с добавлением столбца 'weekday'
# X = data['subject'] + ' ' + data['body'] + ' ' + data['weekday'].astype(str)
X = data['subject'] + ' ' + data['body'] + ' ' + data['date'].astype(str)

y = data['class']

# Разделение на тренировочный и тестовый наборы данных
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25, random_state=94)

# Использование TfidfVectorizer() на объединенных столбцах subject и body с добавленным столбцом weekday
tfidf_vect = TfidfVectorizer()
X_train_tfidf = tfidf_vect.fit_transform(X_train)
X_test_tfidf = tfidf_vect.transform(X_test)

# Обучение и оценка RandomForestClassifier с TfidfVectorizer на объединенных данных
rf_classifier = RandomForestClassifier(n_estimators=8, n_jobs=10, random_state=94)
rf_classifier.fit(X_train_tfidf, y_train)
y_pred = rf_classifier.predict(X_test_tfidf)

precision_2 = precision_score(y_test, y_pred, average='macro')
recall_2 = recall_score(y_test, y_pred, average='macro')
f1_2 = f1_score(y_test, y_pred, average='macro')

print("\nМетрики для TfidfVectorizer на объединенных столбцах subject и body:")
print("Precision (macro avg):", precision_2)
print("Recall (macro avg):", recall_2)
print("F-score (macro avg):", f1_2)

X = data['body']
y = data['class']
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25, random_state=94)

tfidf_vect = TfidfVectorizer(ngram_range=(2, 3))
X_train_tfidf = tfidf_vect.fit_transform(X_train)
X_test_tfidf = tfidf_vect.transform(X_test)

rf_classifier = RandomForestClassifier(n_estimators=8, n_jobs=10, random_state=94)
rf_classifier.fit(X_train_tfidf, y_train)
y_pred = rf_classifier.predict(X_test_tfidf)

precision_3 = precision_score(y_test, y_pred, average='macro')
recall_3 = recall_score(y_test, y_pred, average='macro')
f1_3 = f1_score(y_test, y_pred, average='macro')
print("\nИспользование TfidfVectorizer() на колонке body с параметром ngram_range = (2, 3):")
print("Precision (macro avg):", round(precision_3,3))
print("Recall (macro avg):", round(recall_3,3))
print("F-score (macro avg):", round(f1_3,3))


# Текст для предсказания
text_to_predict = "Derivatives For Energy Professionals Two Full Days, April 9 - 10, 2002 Early Bird Special Deadline is March 11th Houston, TX For More Information Go To www.kaseco.com/classes/derivatives.htm Energy and Power Consumers and Producers? Are you a consumer that hesitated to buy forward in 2000 or bought too soon last year? Are you a producer who hedged too early in 2000 and did not hedge enough in early 2001? Did you take undue credit risk because you did not know how to estimate exposure? Are you comforting yourself with the fact that you did not hedge to make money? Let's face it - you did not hedge to lose money either. Kase's Derivatives for Energy Professionals will provide you with the indispensable strategies and tactics you need to avoid costly errors and equip you to make the important hedging decisions necessary for survival in today's treacherous marketplace. Content * The Derivatives Market * Buying and Selling Forward * Hedging Long and Short Positions * Trigger Deals, EFP's, Swaps * Hedging Spreads and Basis * Volatility and Options * Hedging with Options * Hedging with Exotics * Market Behavior in Favorable Price Environments * Market Behavior in Adverse Price Environments * Developing a Strategy to Match Risk Appetite * Selecting Appropriate Instruments * Developing Policies and Procedures In This Class You Will Learn About * How futures and OTC derivatives work * Hedging prices, basis, margins, and spreads * Math for volatility, VAR, Monte Carlos * Options, including puts, calls, floors, caps, synthetics, and more * Cost-reducing hedge strategies, such as three-way collars, option spreads * How the market behaves, including how long bull and bear markets typically last, how contango or backwardation effects hedging, the impact of removing hedges * What hedge maturities should be used * Considerations for custom strategies * Developing policies and procedures ------------------------------------------------------------------------ Registration Fees Include a Copy of Kase's video Effective Risk Management Early Bird Deadline is March 11 Early Bird Special: $895.00 Two or More Early Bird: $825.00 Regular: $995.00 Two or More: $895.00 Marriott West Loop, Houston $135 Per Night Kase Class Rate (713) 960-0111 Reservations Phone (713) 624-1517 Reservations Fax 1750 West Loop South Houston, TX 77027 For More Information Go To www.kaseco.com/classes/derivatives.htm This email has been sent to michelle.lokay@enron.com, by PowerMarketers.com. Visit our Subscription Center to edit your interests or unsubscribe. http://ccprod.roving.com/roving/d.jsp?p=oo&m=1000838503237&ea=michelle.lokay@enron.com View our privacy policy: http://ccprod.roving.com/roving/CCPrivacyPolicy.jsp Powered by Constant Contact(R) www.constantcontact.com"

# Векторизация текста
text_tfidf = tfidf_vect.transform([text_to_predict])

# Предсказание
predicted_class = rf_classifier.predict(text_tfidf)
predicted_prob = rf_classifier.predict_proba(text_tfidf)[0][predicted_class[0]]

print("\nМетка назначенного класса:", round(predicted_class[0],3))
print("Вероятность отнесения к назначенному классу:", round(predicted_prob,3))
