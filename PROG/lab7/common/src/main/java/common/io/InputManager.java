package common.io;

import common.auth.User;
import common.connection.CommandMsg;
import common.data.*;
import common.exceptions.*;

import java.time.LocalDate;
import java.util.Scanner;

public interface InputManager {
    /**
     * reads name from input
     *
     * @return
     * @throws EmptyStringException
     */
    String readName() throws EmptyStringException;

    /**
     * reads fullName from input
     *
     * @return
     */
    String readFullName();

    /**
     * reads x from input
     *
     * @return
     * @throws InvalidNumberException
     */
    float readXCoord() throws InvalidNumberException;

    /**
     * reads y from input
     *
     * @return
     * @throws InvalidNumberException`
     */
    Long readYCoord() throws InvalidNumberException;

    /**
     * reads coordinates from input
     *
     * @return
     * @throws InvalidNumberException
     */
    Coordinates readCoords() throws InvalidNumberException;

    /**
     * reads salary from input
     *
     * @return
     * @throws InvalidNumberException
     */
    long readSalary() throws InvalidNumberException;

    /**
     * reads endDate from input
     *
     * @return
     * @throws InvalidDateFormatException
     */
    LocalDate readEndDate() throws InvalidDateFormatException;

    /**
     * reads position from input
     *
     * @return
     * @throws InvalidEnumException
     */
    Position readPosition() throws InvalidEnumException;

    /**
     * reads status from input
     *
     * @return
     * @throws InvalidEnumException
     */
    Status readStatus() throws InvalidEnumException;

    /**
     * reads organizationType from input
     *
     * @return
     * @throws InvalidEnumException
     */
    OrganizationType readOrganizationType() throws InvalidEnumException;

    /**
     * reads organization from input
     *
     * @return
     * @throws InvalidDataException
     */
    Organization readOrganization() throws InvalidDataException;

    /**
     * reads Worker from input
     *
     * @return
     * @throws InvalidDataException
     */
    Worker readWorker() throws InvalidDataException;

    /**
     * reads command-argument pair from input
     *
     * @return
     */
    CommandMsg readCommand();

    /**
     * gets input scanner
     *
     * @return
     */

    boolean hasNextLine();


    String readPassword() throws InvalidDataException;

    String readLogin() throws InvalidDataException;

    User readUser() throws InvalidDataException;

    Scanner getScanner();
}
