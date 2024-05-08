import re

from swiplserver import PrologMQI, create_posix_path
from methods import SiblingsPrinter, DatePrinter, FindKills, MonsterChecker

PATH = r'lab1.pl'

queries = [
    "Кого убил(а) Лаура?",
    "Является ли Бобби Хэкетт монстром?",
    "Выведи партнера Макс",
    "Выведи всех родственников Бобби Хэкетт"

]

patterns = {
    r'Кого убил\(а\) (.+)\?' : FindKills.FindKills(),
    r'Является ли (.+) монстром\?' : MonsterChecker.MonsterChecker(),
    r'Выведи партнера (.+)' : DatePrinter.DatePrinter(),
    r'Выведи всех родственников (.+)' : SiblingsPrinter.SiblingsPrinter()
}

with PrologMQI() as mqi:
    with mqi.create_thread() as prolog:
        path = create_posix_path(PATH)
        prolog.query(f'consult("lab1.pl")')
        while True:
            query = input('$ ')
            if query.lower() == 'stop':
                break
            for pattern in patterns:
                match = re.match(pattern, query, re.IGNORECASE)
                if match is None:
                    continue
                task = patterns[pattern]
                task.init_values(match.groups())
                task.execute(prolog)
                break
            else:
                print("Неверный ввод")





