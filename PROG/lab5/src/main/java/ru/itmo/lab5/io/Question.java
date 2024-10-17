package ru.itmo.lab5.io;
import static ru.itmo.lab5.io.OutputManager.*;

import ru.itmo.lab5.exceptions.*;

/**
 * user input wrapper
 * @param <T>
 */
public class Question<T>{
    private Askable<T> askable;
    private T answer;
    public Question(String msg,Askable<T> askable){
        this.askable = askable;
        while (true){
            try{
                System.out.print(msg + " ");
                T ans = this.askable.ask();
                answer = ans;
                break;
            }
            catch(InvalidDataException e){
                printErr(e.getMessage()); 
            }
        }
    }
    public T getAnswer(){
        return answer;
    }
}
