package database;

import auth.UserManager;
import collection.WorkerDequeManager;
import common.auth.User;
import common.data.*;
import common.exceptions.*;
import common.utils.DateConverter;
import log.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class WorkerDatabaseManager extends WorkerDequeManager {
    //language=SQL
    private final static String INSERT_WORKER_QUERY = "INSERT INTO WORKERS (name, coordinates_x, coordinates_y, creation_date, salary, end_date, position, status, organization_full_name, organization_type, user_login,id)" +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,DEFAULT) RETURNING id; ";
    private final DatabaseHandler databaseHandler;
    private final UserManager userManager;

    public WorkerDatabaseManager(DatabaseHandler c, UserManager userManager) throws DatabaseException {
        super();
        databaseHandler = c;
        this.userManager = userManager;
        create();
    }

    private void create() throws DatabaseException {
        //language=SQL
        String create =
                "CREATE TABLE IF NOT EXISTS WORKERS (" +
                        "id SERIAL PRIMARY KEY CHECK ( id > 0 )," +
                        "name TEXT NOT NULL CHECK (name <> '')," +
                        "coordinates_x FLOAT NOT NULL ," +
                        "coordinates_y BIGINT NOT NULL CHECK (coordinates_y > -123 )," +
                        "creation_date TEXT NOT NULL," +
                        "salary BIGINT NOT NULL CHECK(salary > 0)," +
                        "end_date TEXT," +
                        "position TEXT," +
                        "status TEXT NOT NULL," +
                        "organization_full_name VARCHAR(1237) CHECK (organization_full_name <> '')," +
                        "organization_type TEXT NOT NULL," +
                        "user_login TEXT NOT NULL REFERENCES USERS(login)" +
                        ");";

        try (PreparedStatement createStatement = databaseHandler.getPreparedStatement(create)) {
            createStatement.execute();
        } catch (SQLException e) {
            throw new DatabaseException("cannot create worker database");
        }
    }

    @Override
    public int generateNextId() {
        try (PreparedStatement statement = databaseHandler.getPreparedStatement("SELECT nextval('id')")) {
            ResultSet r = statement.executeQuery();
            r.next();
            return r.getInt(1);
        } catch (SQLException e) {
            return 1;
        }
    }

    private void setWorker(PreparedStatement statement, Worker worker) throws SQLException {
        statement.setString(1, worker.getName());
        statement.setFloat(2, worker.getCoordinates().getX());
        statement.setLong(3, worker.getCoordinates().getY());
        statement.setString(4, DateConverter.dateToString(worker.getCreationDate()));
        statement.setLong(5, worker.getSalary());


        if (worker.getEndDate() == null) statement.setString(6, null);
        else statement.setString(6, DateConverter.dateToString(worker.getEndDate()));

        if (worker.getPosition() == null) statement.setString(7, null);
        else statement.setString(7, worker.getPosition().toString());

        statement.setString(8, worker.getStatus().toString());

        statement.setString(9, worker.getOrganization().getFullName());
        statement.setString(10, worker.getOrganization().getType().toString());

        statement.setString(11, worker.getUserLogin());

    }

    private Worker getWorker(ResultSet resultSet) throws SQLException, InvalidDataException {
        Coordinates coordinates = new Coordinates(resultSet.getFloat("coordinates_x"), resultSet.getLong("coordinates_y"));
        Integer id = resultSet.getInt("id");
        String name = resultSet.getString("name");

        Date creationDate = DateConverter.parseDate(resultSet.getString("creation_date"));
        long salary = resultSet.getLong("salary");

        String endDateStr = resultSet.getString("end_date");
        LocalDate endDate = null;
        if (endDateStr != null) endDate = DateConverter.parseLocalDate(endDateStr);

        String positionStr = resultSet.getString("position");
        Position position = null;
        if (positionStr != null) {
            try {
                position = Position.valueOf(positionStr);
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumException();
            }
        }
        Status status;
        OrganizationType type;
        try {
            status = Status.valueOf(resultSet.getString("status"));
            type = OrganizationType.valueOf(resultSet.getString("organization_type"));
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException();
        }
        String fullName = resultSet.getString("organization_full_name");
        Organization organization = new Organization(fullName, type);
        Worker worker = new Worker(name, coordinates, salary, endDate, position, status, organization);
        worker.setCreationDate(creationDate);
        worker.setId(id);
        worker.setUserLogin(resultSet.getString("user_login"));
        if (!userManager.isPresent(worker.getUserLogin())) throw new DatabaseException("no user found");
        return worker;
    }

    @Override
    public void add(Worker worker) {

        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        try (PreparedStatement statement = databaseHandler.getPreparedStatement(INSERT_WORKER_QUERY, true)) {
            setWorker(statement, worker);
            if (statement.executeUpdate() == 0) throw new DatabaseException();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (!resultSet.next()) throw new DatabaseException();
            worker.setId(resultSet.getInt(resultSet.findColumn("id")));

            databaseHandler.commit();
        } catch (SQLException | DatabaseException e) {
            databaseHandler.rollback();
            throw new CannotAddException();
        } finally {
            databaseHandler.setNormalMode();
        }
        super.addWithoutIdGeneration(worker);
    }

    @Override
    public void removeByID(Integer id) {
        //language=SQL
        String query = "DELETE FROM WORKERS WHERE id = ?;";
        try (PreparedStatement statement = databaseHandler.getPreparedStatement(query)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new CannotRemoveException(id);
        }
        super.removeByID(id);
    }

    @Override
    public void removeFirst() {
        removeByID(getCollection().getFirst().getId());
    }

    @Override
    public void updateByID(Integer id, Worker worker) {
        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        //language=SQL
        String sql = "UPDATE WORKERS SET " +
                "name=?," +
                "coordinates_x=?," +
                "coordinates_y=?," +
                "creation_date=?," +
                "salary=?," +
                "end_date=?," +
                "position=?," +
                "status=?," +
                "organization_full_name=?," +
                "organization_type=?," +
                "user_login=?" +
                "WHERE id=?";
        try (PreparedStatement statement = databaseHandler.getPreparedStatement(sql)) {
            setWorker(statement, worker);
            statement.setInt(12, id);
            statement.execute();
            databaseHandler.commit();
        } catch (SQLException e) {
            databaseHandler.rollback();
            throw new CannotUpdateException(id);
        } finally {
            databaseHandler.setNormalMode();
        }
        super.updateByID(id, worker);
    }

    @Override
    public void addIfMax(Worker worker) {
        //language=SQL
        String getMaxQuery = "SELECT MAX(salary) FROM WORKERS";

        if (getCollection().isEmpty()) {
            add(worker);
            return;
        }
        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        try (Statement getStatement = databaseHandler.getStatement();
             PreparedStatement insertStatement = databaseHandler.getPreparedStatement(INSERT_WORKER_QUERY)) {

            ResultSet resultSet = getStatement.executeQuery(getMaxQuery);
            if (!resultSet.next()) throw new CannotAddException();

            long maxSalary = resultSet.getLong(1);
            if (worker.getSalary() < maxSalary)
                throw new DatabaseException("[AddIfMaxException] unable to add, max salary is [" + maxSalary + "] current salary is [" + worker.getSalary()+"]");

            setWorker(insertStatement, worker);

            worker.setId(resultSet.getInt("id"));
            databaseHandler.commit();
        } catch (SQLException e) {
            databaseHandler.rollback();
            throw new CannotAddException();
        } finally {
            databaseHandler.setNormalMode();
        }
        super.addWithoutIdGeneration(worker);
    }

    @Override
    public void addIfMin(Worker worker) {
        //language=SQL
        String getMaxQuery = "SELECT MIN(salary) FROM WORKERS";

        if (getCollection().isEmpty()) {
            add(worker);
            return;
        }
        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        try (Statement getStatement = databaseHandler.getStatement();
             PreparedStatement insertStatement = databaseHandler.getPreparedStatement(INSERT_WORKER_QUERY)) {

            ResultSet resultSet = getStatement.executeQuery(getMaxQuery);
            if (!resultSet.next()) throw new CannotAddException();

            long minSalary = resultSet.getLong(1);
            if (worker.getSalary() > minSalary)
                throw new DatabaseException("[AddIfMinException] unable to add, min salary is [" + minSalary + "] current salary is [" + worker.getSalary()+"]");

            setWorker(insertStatement, worker);

            worker.setId(resultSet.getInt("id"));
            databaseHandler.commit();
        } catch (SQLException e) {
            databaseHandler.rollback();
            throw new DatabaseException("cannot add due to internal error");
        } finally {
            databaseHandler.setNormalMode();
        }
        super.addWithoutIdGeneration(worker);
    }

    public Collection<Worker> clear(User user) {
        databaseHandler.setCommitMode();
        databaseHandler.setSavepoint();
        Set<Worker> removed = new HashSet<>();

        try (PreparedStatement statement = databaseHandler.getPreparedStatement("DELETE FROM WORKERS WHERE user_login=? RETURNING id")) {
            statement.setString(1, user.getLogin());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                removed.add(getByID(id));
                removeByID(id);
            }
        } catch (SQLException | CollectionException e) {
            databaseHandler.rollback();
            deserializeCollection("");
            throw new DatabaseException("cannot clear database");
        } finally {
            databaseHandler.setNormalMode();
        }
        return  removed;
    }

    @Override
    public void deserializeCollection(String ignored) {
        if (!getCollection().isEmpty()) super.clear();
        //language=SQL
        String query = "SELECT * FROM WORKERS";
        try (PreparedStatement selectAllStatement = databaseHandler.getPreparedStatement(query)) {
            ResultSet resultSet = selectAllStatement.executeQuery();
            int damagedElements = 0;
            while (resultSet.next()) {
                try {
                    Worker worker = getWorker(resultSet);
                    if (!worker.validate()) throw new InvalidDataException("element is damaged");
                    super.addWithoutIdGeneration(worker);
                } catch (InvalidDataException | SQLException e) {
                    damagedElements += 1;
                }
            }
            if (super.getCollection().isEmpty()) throw new DatabaseException("nothing to load");
            if (damagedElements == 0) Log.logger.info("collection successfully loaded");
            else Log.logger.warn(damagedElements + " elements are damaged");
        } catch (SQLException e) {
            throw new DatabaseException("cannot load");
        }

    }
}
