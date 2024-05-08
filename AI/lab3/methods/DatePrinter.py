from swiplserver import PrologThread


class DatePrinter:
    def __init__(self, name: str = ''):
        self.name = name

    def execute(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res[0]["Friend"]) == 0:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return f"printDating('{self.name}', Friend)"

    def init_values(self, match):
        if (len(match) > 1):
            print("Неверный ввод")
        else:
            self.name = match[0]

    def failure(self, res):
        print(f'{self.name} не имеет парня(девушку) :-(')

    def success(self, res):
        print(f'У {self.name} есть парень(девушка), а именно - {res[0]["Friend"]}', end=".")
        print("\n")