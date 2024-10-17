package ru.itmo.lab5.file;

import static ru.itmo.lab5.io.OutputManager.*;
import ru.itmo.lab5.exceptions.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



/**
 * Operates the main collection file for saving/loading.
 */
public class FileManager implements ReaderWriter{
    private InputStream inputStream;
    private String path;
    /**
     * Constructor just saves variable of all classes 
     * @param path to collection file
     */
    public FileManager(String path){
        this.path=path;

    }

    public void setPath(String path){
        this.path=path;
    }

    public FileManager(){
        path=null;
    }

    public String read(){
        String str="";
        try{
            if(path==null) throw new EmptyPathException();
            InputStreamReader reader= null;
            File file = new File(path);
            if(!file.exists()) throw new FileNotExistsException();
            if(!file.canRead()) throw new FileWrongPermissionsException("can not read the file");
            inputStream= new FileInputStream(file);
            reader = new InputStreamReader(inputStream);
            int currentSymbol;
            while(( currentSymbol=reader.read())!=-1){
                str+=((char)currentSymbol);
            }
            reader.close();
        }
        catch (FileException e){
            printErr(e.getMessage());
        }catch (IOException e){
            printErr(e.getMessage());

        }
        return str;
    }

    private void create(File file) throws CannotCreateFileException{
        try{
            file.createNewFile(); 
        } catch(IOException e){
            throw new CannotCreateFileException();
        }
    }
    public boolean write(String str){
        boolean res = true;
        try{
            if(path==null) throw new EmptyPathException();
            File file= new File(path);
            if(!file.exists()){
                print("file"+path+"does not exists, trying to create new file");
                create(file);
            };
            if (!file.canWrite()) throw new FileWrongPermissionsException("can not write the file");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            //print("successfully saved");
            writer.close();
        }catch (FileException e){
            printErr(e.getMessage());
            res=false;
        }catch (IOException e){
            res=false;
            printErr("can not access the file ");
        }
        return res;
    }
}
