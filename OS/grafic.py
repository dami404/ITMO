import matplotlib.pyplot as plt

cpu_user_intconversion = [
    0.0,
    0.25,
    0.50,
    0.25,
    0.0,
    0.0,
    0.50,
    0.25,
    0.0,
    0.50,
]

cpu_system_intconversion = [
    0.25,
    0.99,
    0.25,
    0.0,
    0.75,
    0.0,
    0.25,
    0.25,
    0.25,
    0.25,   
]

cpu_user_div64 = [
    0.25,
    0.25,
    0.0,
    0.50,
    0.0,
    0.74,
    0.0,
    0.25,
    0.25,
    0.50,
]

cpu_system_div64 = [
    0.25,
    0.25,
    0.25,
    0.25,
    0.50,
    0.50,
    0.25,
    0.50,
    0.50,
    0.25,   
]

cache_system_stream= [
    0.25,
    0.75,
    0.50,
    0.75,
    0.0,
    0.50,
    0.25,
    0.25,
    0.25,
    0.75,   
]

cache_user_stream= [
    0.25,
    0.0,
    0.25,
    0.0,
    0.0,
    0.0,
    0.50,
    0.0,
    0.50,
    0.0,   
]

cache_system_cache_ways= [
    0.25,
    0.0,
    0.75,
    0.0,
    0.0,
    0.25,
    0.50,
    0.50,
    0.50,
    0.50,   
]

cache_user_cache_ways= [
    0.25,
    0.0,
    0.0,
    0.50,
    1.51,
    0.25,
    0.0,
    0.50,
    0.0,
    0.50,   
]



def draw(user,system):
    xs =[x*1 for x in range(10)]
    plt.plot(xs,system,label='%user')
    plt.plot(xs,user,label='%system')
    plt.xlabel('Время (с)')
    plt.ylabel('Процент пользования')
    plt.legend(fontsize=14)
    plt.show()


# draw(cpu_user_intconversion,cpu_system_intconversion)
# draw(cpu_user_div64,cpu_system_div64)

draw(cache_user_stream,cache_system_stream)
draw(cache_user_cache_ways,cache_system_cache_ways)
