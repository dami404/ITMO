from collections import Counter
from math import gcd

# Функция для нахождения обратного числа по модулю m
def mod_inverse(a, m):
    for i in range(m):
        if (a * i) % m == 1:
            return i
    raise ValueError(f"Не существует обратного элемента для {a} по модулю {m}")

# Шифрование
def affine_encrypt(text, a, b, m):
    encrypted_text = ""
    for char in text:
        if char.isalpha(): # Если символ является буквой
            is_upper = char.isupper()  # Проверяем, заглавная ли это буква
            char = char.lower() # Приводим символ к нижнему регистру для унификации
            P = ord(char) - ord('a')  # Определяем позицию символа в алфавите (от 0 до 25)
            C = (a * P + b) % m  # Применяем аффинное преобразование
            encrypted_char = chr(C + ord('a')) # Преобразуем результат обратно в символ
            if is_upper:
                encrypted_char = encrypted_char.upper() # Преобразуем результат в заглавную
            encrypted_text += encrypted_char
        else:
            encrypted_text += char
    return encrypted_text

# Дешифрование
def affine_decrypt(cipher_text, a, b, m):
    decrypted_text = ""
    a_inv = mod_inverse(a, m) # Находим обратное число для a по модулю m
    for char in cipher_text:
        if char.isalpha():
            is_upper = char.isupper()
            char = char.lower()
            C = ord(char) - ord('a')
            P = (a_inv * (C - b)) % m  # Применяем обратное аффинное преобразование
            decrypted_char = chr(P + ord('a'))
            if is_upper:
                decrypted_char = decrypted_char.upper()
            decrypted_text += decrypted_char
        else:
            decrypted_text += char
    return decrypted_text


# чтение файла
def read_file(filename):
    with open(filename, 'r', encoding='utf-8') as file:
        return file.read()
# запись в файл
def write_file(filename, data):
    with open(filename, 'w', encoding='utf-8') as file:
        file.write(data)



# частотный анализ
def frequency_analysis(text):
    text = ''.join([char.lower() for char in text if char.isalpha()])
    return Counter(text)


# проверка ключевых слов
def check_keywords(cipher_text, keywords):
    decrypted_words = cipher_text.split()
    found_keywords = [word for word in decrypted_words if word.lower() in keywords]
    return found_keywords



if __name__ == "__main__":
    # Задаем параметры
    a = 7  # должен быть взаимно прост с размером алфавита 
    b = 8  
    m = 26  # Размер латинского алфавита
        
    # Проверка взаимной простоты a и m
    if gcd(a, m) != 1:
        raise ValueError(f"Ключ a должен быть взаимно прост с размером алфавита m={m}")
    
    # Чтение исходного текста
    input_file = './input.txt'
    output_encrypted_file = 'encrypted.txt'
    output_decrypted_file = 'decrypted.txt'
    text = read_file(input_file)
    
    # Шифрование текста
    encrypted_text = affine_encrypt(text, a, b, m)
    write_file(output_encrypted_file, encrypted_text)
    print(f"Зашифрованный текст сохранен в {output_encrypted_file}")

    # Дешифрование текста
    decrypted_text = affine_decrypt(encrypted_text, a, b, m)
    write_file(output_decrypted_file, decrypted_text)
    print(f"Расшифрованный текст сохранен в {output_decrypted_file}")
    
    # Частотный анализ
    freq = frequency_analysis(encrypted_text)
    print("Частотный анализ зашифрованного текста:")
    for char, count in freq.most_common():
        print(f"{char}: {count}")

    # Проверка ключевых слов
    keywords = ['type', 'ci pher']  # Замените на список ключевых слов
    found_keywords = check_keywords(decrypted_text, keywords)
    if found_keywords:
        print("Найдены ключевые слова:", found_keywords)
    else:
        print("Ключевые слова не найдены.")
