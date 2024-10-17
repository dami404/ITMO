package common.io;

import common.auth.User;
import common.data.*;

import java.time.LocalDate;
import java.util.Scanner;

import static common.io.ConsoleOutputter.print;

public class ConsoleInputManager extends InputManagerImpl {

    public ConsoleInputManager() {
        super(new Scanner(System.in));
        getScanner().useDelimiter("\n");
    }

    @Override
    public String readName() {
        return new Question<String>("enter name:", super::readName).getAnswer();
    }

    @Override
    public String readFullName() {
        return new Question<String>("enter name:", super::readFullName).getAnswer();
    }

    @Override
    public float readXCoord() {
        return new Question<Float>("enter x:", super::readXCoord).getAnswer();
    }

    @Override
    public Long readYCoord() {
        return new Question<Long>("enter y:", super::readYCoord).getAnswer();
    }

    @Override
    public Coordinates readCoords() {
        print("enter coordinates");
        float x = readXCoord();
        Long y = readYCoord();
        Coordinates coord = new Coordinates(x, y);
        return coord;
    }

    @Override
    public long readSalary() {
        return new Question<Long>("enter salary:", super::readSalary).getAnswer();
    }

    @Override
    public LocalDate readEndDate() {
        return new Question<LocalDate>("enter endDate:", super::readEndDate).getAnswer();
    }

    @Override
    public Position readPosition() {
        return new Question<Position>("enter position(DIRECTOR, MANAGER, ENGINEER, BAKER):", super::readPosition).getAnswer();
    }

    @Override
    public Status readStatus() {
        return new Question<Status>("enter status(RECOMMENDED_FOR_PROMOTION, REGULAR, PROBATION):", super::readStatus).getAnswer();
    }

    @Override
    public OrganizationType readOrganizationType() {
        return new Question<OrganizationType>("enter organization type(PUBLIC, GOVERNMENT, PRIVATE_LIMITED_COMPANY, OPEN_JOINT_STOCK_COMPANY):", super::readOrganizationType).getAnswer();
    }

    @Override
    public Organization readOrganization() {
        print("enter organization");
        String fullName = readFullName();
        OrganizationType orgType = readOrganizationType();
        return new Organization(fullName, orgType);
    }

    @Override
    public String readLogin() {
        return new Question<String>("enter login:", super::readLogin).getAnswer();
    }

    @Override
    public String readPassword() {
        return new Question<String>("enter password:", super::readPassword).getAnswer();
    }

    @Override
    public User readUser() {
        String login = readLogin();
        String password = readPassword();
        return new User(login, password);
    }

}
