package ru.itmo.lab5.collection;
import java.util.List;
import java.util.Vector;



/**
 * interface for storing elements
 * @param <T> type of elements
 */
public interface CollectionManager<T> {
    /**
     * generates new unique ID for collection
     * @return
     */
    public int generateNextId();
    /**
     * sorts collection with id number
     */

    public void sort();
    /** 
     * @return collection
     */
    public Vector<T> getCollection();

    /**
     * adds new element
     * @param element
     */
    public void add(T element);

    /**
     * get information about collection
     * @return info
     */
    public String getInfo();

    /**
     * checks if collection contains element with particular id
     * @param ID
     * @return
     */
    public boolean checkID(Integer ID);

    /**
     * removes element by id
     * @param id
     */
    public void removeByID(Integer id);

    /**
     * updates element by id
     * @param id
     * @param newElement
     */
    public void updateByID(Integer id, T newElement);

    /**
     * Get size of collection
     * @return Size of collection
     */
    public int getSize();
   /**
     * clear collection
     */
    public void clear();
    /**
     * removes 1st element of collection
     */
    public void removeFirst();
    /**
     * remove last element in collection
     */
    public void removeLast();
    /**
     * 
     * @param id
     * Adds elements to the collection with entered ID
     */

    public void insertAtIndex (T a, String id);

    /**
     * print all elements which name starts with substring
     * @param start
     */
    public void filterStartsWithName(String start);
    /**
     * 
     * @param a
     * @return number of elements that contain a genres less than input
     */
    public Integer countLessThenGenre(String a);
    /**
     * 
     * @param a
     * @return sorted string of all slabels
     */
    public List<String> printFieldAscendingLabel();

    /**
     * convert collection to json
     * @param json
     * @return true if success, false if not
     */
    public boolean deserializeCollection(String json);

    /**
     * parse collection from json
     * @return
     */
    public String serializeCollection();

}
