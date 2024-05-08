from swiplserver import PrologThread

class SiblingsPrinter:
    def __init__(self,name:str='') -> None:
        self.place_name=name
    
    def execute(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res[0]["People"]) == 0:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return f"printAllSiblings('{self.place_name}', People)"

    def failure(self, res):
        print(f'У {self.place_name} не найдено родственников =^(')
    def success(self, res):
        no_dubls=[]
        for i in range(len(res)):
            if res[i]["People"] not in no_dubls and res[i]["People"]!=self.place_name:
                no_dubls.append(res[i]["People"])

        print(f'У {self.place_name} {len(no_dubls)} родственников, а именно: ', end="")
        for i in range(len(no_dubls)):
            if(i!=len(no_dubls)-1):
                print(no_dubls[i],end=', ')
            else:
                print(no_dubls[i],end='.')
        print("\n")

    def init_values(self, match):
        if len(match) > 1:
            print("Неверный ввод")
        else:
            self.place_name = match[0]