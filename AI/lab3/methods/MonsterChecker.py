from swiplserver import PrologThread


class MonsterChecker:
    def __init__(self, name: str = ''):
        self.name = name

    def execute(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res[0]["Monster"]) == 0:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return f"monsterChecker('{self.name}', Monster);findVictims('{self.name}', Victims)"


    def init_values(self, match):
        if (len(match) > 1):
            print("Неверный ввод")
        else:
            self.name = match[0]

    # def failure(self, res):
    #     print(f'{self.name} остался(ась) человеком :-)')

    def success(self, res):
        if len(res)==1:
            print(f'{self.name} остался(ась) человеком :-)')
        else:
            print(f'{self.name} превратился(ась) в оборотня. В этом виноват(а) {res[0]["Monster"]}', end=".\n")
            print(f'А сам(а) {self.name} покусал(а) - ', end="")
            
            for i in range(len(res[1]["Victims"])):
                if (i != len(res[1]["Victims"]) - 1):
                    print(res[1]["Victims"][i], end=", ")
                else:
                    print(res[1]["Victims"][i], end=".")
            print("\n")

        print(f'{self.name} остался(ась) человеком :-)')
