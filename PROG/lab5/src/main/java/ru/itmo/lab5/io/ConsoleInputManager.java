package ru.itmo.lab5.io;

import java.util.Scanner;

import java.util.Date;

import ru.itmo.lab5.data.*;
import static ru.itmo.lab5.io.OutputManager.*;
public class ConsoleInputManager extends InputManagerImpl 
{
    public ConsoleInputManager(){
        super (new Scanner(System.in));
        getScanner().useDelimiter("\n");
    }

    @Override
    public  String readMusicBandName(){
        return new Question<String>("Enter the name of music band, please:", super::readMusicBandName).getAnswer();
    }
    @Override
    public int readXCoord(){
        return new Question<Integer>("Enter the X coordinate, please:",super::readXCoord).getAnswer();
    }
    @Override
    public double readYCoord(){
        return new Question<Double>("Enter the Y coordinate, please:",super::readYCoord).getAnswer(); 
    }
    @Override
    public Coordinates readCoords(){
        print("Entering Coordinates.");
        int x=readXCoord();
        double y=readYCoord();
        Coordinates coord= new Coordinates(x,y);
        return coord;
    }
    /**
    @Override

    public Date readCreationDate(){
        return new Question<Date>("Enter the date, please:",super::readCreationDate).getAnswer();
    }*/
    @Override
    public Long readNumberOfParticipants(){
        return new Question<Long>("Enter the number of participants, please",super::readNumberOfParticipants).getAnswer();
    }

    @Override
    public MusicGenre readGenre(){
        return new Question<MusicGenre>("Enter the music genre, please(PSYCHEDELIC_ROCK, RAP, POP, POST_PUNK)", super::readGenre).getAnswer();
    }

    @Override
    public String readLabelName(){
        return new Question<String>("Enter the name of label, please:",super::readLabelName).getAnswer();
    }
    @Override
    public Label readLabel(){
        String labelName=readLabelName();
        Label label= new Label(labelName);
        return  label;
    }

    @Override
    public MusicBand readMBand(){
        String MBname=readMusicBandName();
        Coordinates coords= readCoords();
        Date date= new Date();
        Long numberOfParticipants= readNumberOfParticipants();
        MusicGenre genre= readGenre();
        Label Lname= readLabel();
        return new MusicBand(MBname, coords, numberOfParticipants, date, genre, Lname);
    }
}
