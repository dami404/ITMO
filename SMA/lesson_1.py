import networkx as nx

# Задаем параметры графа

# вершины
num_nodes = 44
# ребра
num_edges = 428
seed = 68

# Генерируем граф
random_graph = nx.dense_gnm_random_graph(num_nodes, num_edges, seed=seed)
# Плотность графа
density = nx.density(random_graph)
print("Плотность графа:", round(density, 3))
# Длина кратчайшего пути от вершины 3 до вершины 38:
shortest_path_length = nx.shortest_path_length(random_graph, source=3, target=38)
print("Длина кратчайшего пути:", shortest_path_length)
# Количество вершин, входящих в клику наибольшего размера:
largest_clique_size = len(max(nx.find_cliques(random_graph), key=len))
print("Количество вершин в клике наибольшего размера:", largest_clique_size)
