from swiplserver import PrologThread


class FindKills:
    def __init__(self, name: str = ''):
        self.name = name

    def execute(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res[0]["Kills"]) == 0:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return f"findKills('{self.name}', Kills)"

    def init_values(self, match):
        if (len(match) > 1):
            print("Неверный ввод")
        else:
            self.name = match[0]

    def failure(self, res):
        print(f'{self.name} никого не убил(а) :-)')

    def success(self, res):
        print(f'{self.name} убила {len(res[0]["Kills"])} людей, а именно - ', end="")
        for i in range(len(res[0]["Kills"])):
            if (i != len(res[0]["Kills"]) - 1):
                print(res[0]["Kills"][i], end=", ")
            else:
                print(res[0]["Kills"][i], end=".")
        print("\n")