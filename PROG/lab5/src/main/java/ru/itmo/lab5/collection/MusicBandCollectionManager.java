package ru.itmo.lab5.collection;
import java.util.Vector;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import java.util.LinkedList;
import java.util.List;

import ru.itmo.lab5.json.*;

import ru.itmo.lab5.data.MusicBand;


import static ru.itmo.lab5.io.OutputManager.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;





public class MusicBandCollectionManager implements CollectionManager<MusicBand>{


    private Vector <MusicBand> collection;
    private java.util.Date date;
    private HashSet<Integer> uniqueIds;
    private int count;

    


    /**
     * Constructor, set start values
     */
    
    public MusicBandCollectionManager(){
        uniqueIds=new HashSet<>();
        collection= new Vector<>();
        date=new java.util.Date();
    }
    
    public int generateNextId(){
        if(collection.isEmpty()) return 1;
        else{
            Integer id= collection.lastElement().getId() +1;
            if (uniqueIds.contains(id)){
                while(uniqueIds.contains(id)) id+=1;
            }
            uniqueIds.add(id);
            return id;
        }
        
    }
    
    public void sort(){
        Collections.sort(collection);
    }
    
    public Vector<MusicBand> getCollection(){
        return collection;
    }
    
    public void add(MusicBand musicBand){
        musicBand.setId(generateNextId());
        collection.add(musicBand);
        print("You've added element:" +"\n" +  musicBand.toString());
    }
    
    public String getInfo(){
        return "Vector of MusicBand, size: " + Integer.toString(collection.size())+ ", initialization date: " + date.toString() ;

    }
    
    public boolean checkID(Integer id){
        for(MusicBand musicBand:collection){
            if(musicBand.getId()==id) return true;
        }
        return false;
    }
    
    public void removeByID(Integer id){
        for(MusicBand musicBand:collection){
            if(musicBand.getId()==id){
                collection.remove(musicBand);
                uniqueIds.remove(id);
                print("Element #"+Integer.toString(id) +" successfully deleted");
                return;
            }
        }
    }
    
    public void updateByID(Integer id,MusicBand newMusicBand){
        int idx=0;
        for(MusicBand musicBand: collection){
            if(musicBand.getId()==id){
                newMusicBand.setId(id);
                collection.set(idx,newMusicBand);
                print("element #"+Integer.toString(id)+" successfully updated");
               return;
            }
            idx+=1;
        }
    }
    

    public int getSize(){
        return collection.size();
    }
    
    public void clear(){
        collection.clear();
        uniqueIds.clear();
        print("Collection successfully cleared");
    }
    
    public Integer countLessThenGenre(String genre){
        
        for(MusicBand musicBand:collection){
            if (musicBand.getMusicGenre().toString().compareTo(genre.toString())>0) {
                count++;}
        }
        return count;
    }
    
    public List<String> printFieldAscendingLabel(){
        List<String> list = new ArrayList<>();
        for(MusicBand musicBand: collection){
            list.add(musicBand.getAllLabels().toString());
        }
        Collections.sort(list);
        return list;
    }


    

    public void removeFirst(){
        int id= collection.get(0).getId();
        collection.remove(0);
        uniqueIds.remove(id);
        print("element #"+Integer.toString(id)+" successfully deleted");

    }
    
    public void removeLast(){
        int id= collection.get(collection.size()-1).getId();
        collection.remove(collection.size()-1);
        uniqueIds.remove(id);
        print("element #"+Integer.toString(id)+" successfully deleted");
              
    
    }
    public void insertAtIndex(MusicBand musicBand,String a){
        int id = Integer.parseInt (a);
        
        {musicBand.setId(id);
            collection.add(musicBand);
            print("You've added element:" +"\n" +  musicBand.toString());}   
    }
     
     

    

    public void filterStartsWithName(String start){
        LinkedList<MusicBand> list=new LinkedList<>();
        for(MusicBand musicBand: collection){
            if(musicBand.getMBName().startsWith(start.trim())){
                list.add(musicBand);
            }
        }
        if(list.isEmpty()) print("none of elements have name which starts with " + start);
        else{
            print("Starts with: " + start);
            for(MusicBand musicBand: list){
                print(musicBand.toString());
            }
        }
    }

    public boolean deserializeCollection(String json){
        boolean success=true;
        try{
            if(json==null || json.equals("")){
                collection= new Vector<MusicBand>();
            }else{
                Type collectionType = new TypeToken<Vector<MusicBand>>(){}.getType();
                Gson gson =new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(collectionType, new CollectionDeserializer(uniqueIds))
                .create();
                collection=gson.fromJson(json.trim(),collectionType);
            }
        }catch (JsonParseException e){
            success= false;
            printErr("Wrong json data");
        }
        return success;
    }


    public String serializeCollection(){
        if(collection==null || collection.isEmpty()) return "";
        Gson  gson= new GsonBuilder()
        .registerTypeAdapter(Date.class, new DateSerializer())
        .setPrettyPrinting().create();
        String json= gson.toJson(collection);
        return json;

    }

    

    





}
