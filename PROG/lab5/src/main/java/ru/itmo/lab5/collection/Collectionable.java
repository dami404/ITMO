package ru.itmo.lab5.collection;

import ru.itmo.lab5.data.Validateable;

/**
 * Интерфейс для хранимого объекта
 */
public interface Collectionable extends Comparable<Collectionable>, Validateable{

    public int getId();
    /**
     * устанавливает другое id. используется для замены объектов в коллекции
     * @param ID
     */
    public void setId(int ID);
    
    public long getNumberOfParticipants();
    
    public String getMBName();

    /**
     * сравнивает 2 объекта
     */
    public int compareTo(Collectionable mb);

    public boolean validate();
}
