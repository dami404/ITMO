package common.file;

import common.exceptions.*;

import java.io.*;

/**
 * Operates the main collection common.file for saving/loading.
 */
public class FileManager implements ReaderWriter {
    private String path;
    private File file;
    /**
     * Constructor, just save variable for all class.
     *
     * @param pth Path to collection common.file.
     */
    public FileManager(String pth) {
        path = pth;
    }
    public FileManager(File f) {
        file = f;
    }

    public void setPath(String pth) {
        path = pth;
    }
    public void setFile(File f){
        file = f;
    }

    public FileManager() {
        path = null;
    }

    public String read() throws FileException {
        if(file==null) {
            if (path == null) throw new EmptyPathException();
            if (path.startsWith("/dev/null") || path.startsWith("/dev/zero")) throw new ProhibitedPathException();
            InputStreamReader reader = null;

            File f = new File(path);
            return readFile(f);
        } else {
            return readFile(file);
        }
    }

    private String readFile(File f) throws FileException{
        String str = "";
        try {
            InputStreamReader reader = null;
            if (!f.exists()) throw new FileNotExistsException();
            if (!f.canRead()) throw new FileWrongPermissionsException();
            InputStream inputStream = new FileInputStream(f);
            reader = new InputStreamReader(inputStream);
            int currectSymbol;
            while ((currectSymbol = reader.read()) != -1) {
                str += ((char) currectSymbol);
            }
            reader.close();
        } catch (IOException e){
            throw new FileException();
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
            if (!file.canWrite()) throw new FileWrongPermissionsException();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            throw new FileException();
        }
    }
}