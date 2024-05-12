import pandas as pd
import random
from sklearn.cluster import KMeans
from sklearn.metrics import pairwise_distances_argmin_min

# Считываем данные из файла .csv
data = pd.read_csv("data/Soc_Net_Task_5_Test_File_0.csv")

# Определяем признаки для кластеризации (можно выбрать нужные признаки из списка)
features = ['user_location_country', 'user_location_region', 'user_location_city',
            'is_mobile', 'is_package', 'channel', 'srch_adults_cnt', 'srch_children_cnt',
            'srch_rm_cnt', 'srch_destination_id', 'srch_destination_type_id',
            'hotel_continent', 'hotel_country', 'hotel_market']

# Выполняем кластеризацию данных с помощью KMeans
kmeans = KMeans(n_clusters=100, random_state=42)
data['cluster'] = kmeans.fit_predict(data[features])

import numpy as np

def get_recommendations(query, data, k=5):
    # Reshape the query data to match the expected shape
    query_data = query[features].values.reshape(1, -1)
    # Predict the cluster for the query
    cluster_center = kmeans.cluster_centers_[kmeans.predict(query_data)]
    # Find the indices of the records in the cluster
    cluster_indices = data[data['cluster'] == query['cluster']].index
    # Convert indices to a list and shuffle it
    cluster_indices_list = cluster_indices.tolist()
    np.random.shuffle(cluster_indices_list)
    # Get k random recommendations from the cluster
    recommendations = data.loc[cluster_indices_list[:k]]
    return recommendations



# Применяем функцию для каждого поискового запроса и записываем результаты в файл
with open("data/recommendations.csv", "w") as f:
    f.write("user_id,recommended_hotels\n")
    for user_id, group in data.groupby('user_id'):
        recommendations = get_recommendations(group.iloc[0], data)
        f.write(f"{user_id},{'|'.join(recommendations['hotel_market'].astype(str))}\n")
