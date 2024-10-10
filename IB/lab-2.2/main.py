# Шифрование RSA методом повторного шифрования

# N = 453819149023 
# e = 1011817 
# C = [442511634532]

N = 290716329017 
e = 497729 
C = [1135414239,
    169213008965,
    175441050863,
    109545918774,
    123669279758,
    149542889269,
    43068653151,
    32806195453,
    285151390718,
    137668394392,
    140567677417,
    176736386447,
    218957656245,
]


print("Входные данные:")
print(f'N = {N}')
print(f'e = {e}')
print(f'C = {C}\n')

i = 0
text = ''

print("Расшифровка шифроблоков:")

for c in C:
    j = 0
    y = pow(c,e,N)
    y_prev=0
    while  not y == c:
        j+=1
        y_prev=y
        y = pow(y_prev,e,N)
    print("- Порядок числа e в конечном поле Zф(N):")
    print(j+1)

    i+=1
    code = y_prev.to_bytes(4, byteorder='big')
    block = code.decode('cp1251') # другой не декодит
    text += block 
    if i>9:
        print(f'block{i} : {block}')

    else:
        print(f' block{i} : {block}')
print('\n')

print("Полностью расшифрованный текст:")
print(text)

