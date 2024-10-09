
N = 81177745546021
e = 2711039
C = [61553353723258,
    11339642237403,
    55951185642146,
    38561524032018,
    34517298669793,
    33641624424571,
    78428225355946,
    50176820404544,
    68017840453091,
    5507834749606,
    26675763943141,
    47457759065088]


# N = 65815671868057
# e = 7423489
# C = [38932868535359]

print("Входные данные:")
print(f'N = {N}')
print(f'e = {e}')
print(f'C = {C}\n')
n = int(N**0.5) + 1

def isZNumber(n):
    if n % 1 ==0:
        return True

i = -1 # от -1, чтобы пункт (1) из методички тоже в цикл попал
D=0.1 # костыль, чтобы цикл шел, пока d не будет целым числом
while (not isZNumber(D)):
    i += 1
    t = n + i
    w = t**2 - N
    D = w**0.5

print("Промежуточные данные:")
print(f' n = {int(n)}')
print(f't{i} = {int(t)}')
print(f'w{i} = {int(w)}')
print(f' D = {int(D)}\n')


p = t + w**0.5
q = t - w**0.5
Phi = (p-1)*(q-1)
d = pow(e,-1,int(Phi))

print("Основные показатели:")
print(f'   p = {int(p)}')
print(f'   q = {int(q)}')
print(f'ф(N) = {int(Phi)}')
print(f'   d = {int(d)}\n')

print("Расшифровка шифроблоков:")
i = 0
text = ''
for c in C:
    i+=1
    M = pow(c,d,N)
    code = M.to_bytes(4, byteorder='big')
    block = code.decode('cp1251') # другой не декодит
    text += block 
    if i>9:
        print(f'block{i} : {block}')

    else:
        print(f' block{i} : {block}')
print('\n')

print("Полностью расшифрованный текст:")
print(text)

# N = p * q
# ф(N) = (p-1)*(q-1)
# e, ф(N) - взаимно простые
# d = 1(mod ф(N) )/e