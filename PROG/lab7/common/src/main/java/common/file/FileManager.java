package common.file;

import common.exceptions.*;
import common.exceptions.*;

import java.io.*;

/**
 * Operates the main collection common.file for saving/loading.
 */
public class FileManager implements ReaderWriter {
    private String path;

    /**
     * Constructor, just save variable for all class.
     *
     * @param pth Path to collection common.file.
     */
    public FileManager(String pth) {
        path = pth;
    }

    public void setPath(String pth) {
        path = pth;
    }

    public FileManager() {
        path = null;
    }

    public String read() throws FileException {
        String str = "";
        try {
            if (path == null) throw new EmptyPathException();
            if (path.startsWith("/dev/null") || path.startsWith("/dev/zero")) throw new ProhibitedPathException();
            InputStreamReader reader = null;

            File file = new File(path);
            if (!file.exists()) throw new FileNotExistsException();
            if (!file.canRead()) throw new FileWrongPermissionsException("cannot read file");
            InputStream inputStream = new FileInputStream(file);
            reader = new InputStreamReader(inputStream);
            int currectSymbol;
            while ((currectSymbol = reader.read()) != -1) {
                str += ((char) currectSymbol);
            }
            reader.close();
        } catch (IOException e) {
            throw new FileException("cannot read file");
        }
        return str;
    }

    private void create(File file) throws CannotCreateFileException {
        try {
            boolean success = file.createNewFile();
            if (!success) throw new CannotCreateFileException();
        } catch (IOException e) {
            throw new CannotCreateFileException();
        }
    }

    public void write(String str) throws FileException {
        try {
            if (path == null) throw new EmptyPathException();
            File file = new File(path);

            if (!file.exists()) {
                create(file);
            }
            if (!file.canWrite()) throw new FileWrongPermissionsException("cannot write file");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            throw new FileException("cannot access file");
        }
    }
}