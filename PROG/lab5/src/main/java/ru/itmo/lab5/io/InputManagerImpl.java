package ru.itmo.lab5.io;

import java.util.Scanner;
import java.util.Date;

import ru.itmo.lab5.commands.CommandWrapper;

import static ru.itmo.lab5.utils.DateConverter.*;
import ru.itmo.lab5.data.*;
import ru.itmo.lab5.exceptions.*;

/**
 * basic implementation of InputManager
 */
public abstract class InputManagerImpl implements InputManager{
    private Scanner scanner;

    public InputManagerImpl(Scanner sc){
        this.scanner=sc;
        scanner.useDelimiter("\n");
    }

    public Scanner getScanner(){
        return scanner;
    }

    public void setScanner(Scanner scanner){
        this.scanner=scanner;

    }

    public String readMusicBandName () throws EmptyStringException{
        String s=scanner.nextLine().trim();
        if(s.equals("")){
            throw new EmptyStringException();
        }
        return s;
    }

    public int readXCoord() throws InvalidNumberException{
        int x;
        try{
            x=Integer.parseInt(scanner.nextLine());
        }
        catch(NumberFormatException e){
            throw new InvalidNumberException();
        }
        return x;

    }
      
    public double readYCoord() throws InvalidNumberException{
        double y;
        try{
            y=Double.parseDouble(scanner.nextLine());
        }
        catch(NumberFormatException e){
            throw new InvalidNumberException();
        }
        return y;
    }            
    
    public Coordinates readCoords() throws InvalidNumberException{
        int x=readXCoord();
        double y=readYCoord();
        Coordinates coord = new Coordinates(x, y);
        return coord;
    }
    
    public Date readCreationDate() throws InvalidDateFormatException{
        String date = scanner.nextLine().trim();
        if(date.equals("")){
            return null;
        }
        else{
            return parseDate(date);
        }
    }
    
    public Long readNumberOfParticipants() throws InvalidNumberException{
        long numberOfParticipants;
        try{
            numberOfParticipants=Long.parseLong(scanner.nextLine());
        }
        catch(NumberFormatException e) {
            throw new InvalidNumberException();
        }
        if(numberOfParticipants<=0){
            throw new InvalidNumberException("must be greater than 0");
        }
        return numberOfParticipants; 
    }
    
    public MusicGenre readGenre() throws InvalidEnumException{
        String s=scanner.nextLine().trim();
        try{
            return MusicGenre.valueOf(s);
        }
        catch(IllegalArgumentException e){
            throw new InvalidEnumException();
        } 
    }
    
    public String readLabelName(){
        String s=scanner.nextLine();
        if(s.equals("")){
            return null;
        }
        return s;
            
    }

    public Label readLabel(){
        String labelName=readLabelName();
        return new Label(labelName);
    }
    
    public MusicBand readMBand() throws InvalidDataException{
        MusicBand musicBand=null;
        String musicBandName= readMusicBandName();
        Coordinates coordinates= readCoords();
        Date date= new Date();
        Long numberOfParticipants= readNumberOfParticipants();
        MusicGenre musicGenre = readGenre();
        Label labelName= readLabel();
        musicBand=new MusicBand(musicBandName,coordinates,numberOfParticipants,date,musicGenre,labelName);
        return musicBand;
    }   
   
    public CommandWrapper readCommand(){
        String cmd=scanner.nextLine();
        if(cmd.contains(" ")){//if command has an argument
            String arr[] = cmd.split(" ",2);
            cmd=arr[0];
            String arg=arr[1];
            return new CommandWrapper(cmd,arg);
        }else{
            return new CommandWrapper(cmd);
        }
    }

}