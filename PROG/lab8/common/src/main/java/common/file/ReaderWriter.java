package common.file;

import common.exceptions.FileException;

public interface ReaderWriter {
    /**
     * sets path to file
     *
     * @param pth
     */
    void setPath(String pth);

    /**
     * reads data
     *
     * @return
     */
    String read() throws FileException;

    /**
     * saves data
     *
     * @param data
     */
    void write(String data) throws FileException;
}
