package common.io;

import common.auth.User;
import common.connection.CommandMsg;
import common.data.*;
import common.exceptions.*;

import java.time.LocalDate;
import java.util.Scanner;

import static common.utils.DateConverter.parseLocalDate;

/**
 * basic implementation of InputManager
 */
public abstract class InputManagerImpl implements InputManager {
    private Scanner scanner;

    public InputManagerImpl(Scanner scanner) {
        this.scanner = scanner;

    }

    private String read() {
        return scanner.nextLine();
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner sc) {
        scanner = sc;
    }

    public String readName() throws EmptyStringException {
        String s = read();
        if (s.equals("")) {
            throw new EmptyStringException("[NameEmptyException] name cannot be empty");
        }
        return s;
    }

    public String readFullName() {
        String s = read();
        if (s.equals("")) {
            return null;
        }
        return s;
    }

    public float readXCoord() throws InvalidNumberException {
        float x;
        try {
            x = Float.parseFloat(read());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException("[XCoordFormatException] X must be number");
        }
        if (Float.isInfinite(x) || Float.isNaN(x)) throw new InvalidNumberException("[XCoordFormatException] X must be number");
        return x;
    }

    public Long readYCoord() throws InvalidNumberException {
        Long y;
        try {
            y = Long.parseLong(read());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException("[YCoordFormatException] y must be number");
        }
        if (y <= -123) throw new InvalidNumberException("[YCoordLimitException] y must be greater than -123");
        return y;
    }

    public Coordinates readCoords() throws InvalidNumberException {
        float x = readXCoord();
        Long y = readYCoord();
        Coordinates coord = new Coordinates(x, y);
        return coord;
    }

    public long readSalary() throws InvalidNumberException {
        Long s;
        try {
            s = Long.parseLong(read());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException("[SalaryFormatException] salary must be number");
        }

        if (s <= 0) throw new InvalidNumberException("[SalaryLimitException] salary must be greater than 0");

        return s;
    }

    public LocalDate readEndDate() throws InvalidDateFormatException {
        String buf = read();
        if (buf.equals("")) {
            return null;
        } else {
            try {
                return parseLocalDate(buf);
            }
            catch (InvalidDateFormatException e){
                throw new InvalidDateFormatException("[EndDateFormatException] wrong date format");
            }
        }
    }

    public Position readPosition() throws InvalidEnumException {
        String s = read();
        if (s.equals("")) {
            return null;
        } else {
            try {
                return Position.valueOf(s);
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumException("[PositionFormatException] wrong position");
            }
        }
    }

    public Status readStatus() throws InvalidDataException {
        String s = read();
        if(s==null||s.equals("")) throw new EmptyStringException("[StatusEmptyException] status cannot be empty");
        try {
            return Status.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException("[StatusFormatException] wrong status");
        }
    }

    public OrganizationType readOrganizationType() throws InvalidDataException {
        String s = read();
        if(s==null||s.equals("")) throw new EmptyStringException("[OrganizationTypeEmptyException] organization type cannot be empty");
        try {
            return OrganizationType.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException("[OrganizationTypeFormatException] wrong organization type");
        }
    }

    public Organization readOrganization() throws InvalidDataException {
        String fullName = readFullName();
        OrganizationType orgType = readOrganizationType();
        return new Organization(fullName, orgType);
    }

    public Worker readWorker() throws InvalidDataException {
        Worker worker = null;

        String name = readName();
        Coordinates coords = readCoords();
        long salary = readSalary();
        LocalDate date = readEndDate();
        Position pos = readPosition();
        Status stat = readStatus();
        Organization org = readOrganization();
        worker = new DefaultWorker(name, coords, salary, date, pos, stat, org);

        return worker;

    }

    public String readPassword() throws InvalidDataException {
        String s = read();
        if (s.equals("")) throw new EmptyStringException();
        return s;
    }

    public String readLogin() throws InvalidDataException {
        String s = read();
        if (s.equals("")) throw new EmptyStringException();
        return s;
    }

    public User readUser() throws InvalidDataException {
        return new User(readPassword(), readLogin());
    }

    public CommandMsg readCommand() throws InvalidDataException{
        String cmd = read();
        String arg = null;
        Worker worker = null;
        User user = null;
        if (cmd.contains(" ")) { //if command has argument
            String[] arr = cmd.split(" ", 2);
            cmd = arr[0];
            arg = arr[1];
        }
        if (cmd.equals("add") || cmd.equals("add_if_min") || cmd.equals("add_if_max") || cmd.equals("update")) {

            worker = readWorker();
        } else if (cmd.equals("login") || cmd.equals("register")) {
            user = readUser();

            return new CommandMsg(cmd, null, null, user);
        }
        return new CommandMsg(cmd, arg, worker);
    }
}
