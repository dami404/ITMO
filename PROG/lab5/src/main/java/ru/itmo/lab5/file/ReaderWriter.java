package ru.itmo.lab5.file;

public interface ReaderWriter {
    /**
     * 
     * @param path
     */
    public void setPath(String path);
    /**
     * reads data
     * @return
     */
    public String read();
    /**
     * saves data
     * @param data
     * 
     */
    public boolean write(String data);
}
