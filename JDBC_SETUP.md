# JDBC Practice Setup for OCP 17 Exam

This guide will help you set up PostgreSQL with Docker and run JDBC practice examples.

## Prerequisites

- Docker and Docker Compose installed
- Java 17 (already configured)
- Maven (IntelliJ IDEA has Maven bundled)

## Setup Instructions

### 1. Start PostgreSQL Database

Open a terminal in the project directory and run:

```bash
docker-compose up -d
```

This will:
- Start a PostgreSQL 16 container
- Create a database named `ocp_practice`
- Initialize tables: `employees`, `departments`, `projects`
- Insert sample data
- Expose PostgreSQL on port 5432

To verify the database is running:
```bash
docker-compose ps
```

To view logs:
```bash
docker-compose logs postgres
```

### 2. Configure IntelliJ IDEA for Maven

1. Open the project in IntelliJ IDEA
2. IntelliJ should automatically detect the `pom.xml` and prompt you to import the Maven project
3. If not, right-click on `pom.xml` → "Add as Maven Project"
4. Wait for Maven to download the PostgreSQL JDBC driver dependency

### 3. Verify Database Connection

You can connect to the database using:
- **Host**: localhost
- **Port**: 5432
- **Database**: ocp_practice
- **Username**: ocpuser
- **Password**: ocppass123

Using psql (if installed):
```bash
docker exec -it jdbc-practice-db psql -U ocpuser -d ocp_practice
```

Or use IntelliJ's Database tool:
1. View → Tool Windows → Database
2. Click "+" → Data Source → PostgreSQL
3. Enter the connection details above

## Running JDBC Examples

The examples are organized by topic and located in `src/jdbc/`:

### Example 1: Basic Connection
```bash
mvn compile exec:java -Dexec.mainClass="jdbc.Example01_BasicConnection"
```

Topics covered:
- Opening database connections
- Using try-with-resources
- Statement vs PreparedStatement
- Basic ResultSet iteration

### Example 2: CRUD Operations
```bash
mvn compile exec:java -Dexec.mainClass="jdbc.Example02_CRUDOperations"
```

Topics covered:
- INSERT with auto-generated keys
- SELECT with PreparedStatement
- UPDATE operations
- DELETE operations
- executeUpdate() return values

### Example 3: ResultSet Navigation
```bash
mvn compile exec:java -Dexec.mainClass="jdbc.Example03_ResultSetNavigation"
```

Topics covered:
- Forward-only vs scrollable ResultSets
- Navigation methods: first(), last(), absolute(), relative()
- Position checking: isFirst(), isLast(), isBeforeFirst()
- Updatable ResultSets

### Example 4: Transactions
```bash
mvn compile exec:java -Dexec.mainClass="jdbc.Example04_Transactions"
```

Topics covered:
- Auto-commit mode
- Manual transactions with commit() and rollback()
- Savepoints for partial rollbacks
- Transaction error handling

### Example 5: Batch Processing
```bash
mvn compile exec:java -Dexec.mainClass="jdbc.Example05_BatchProcessing"
```

Topics covered:
- addBatch() and executeBatch()
- Batch inserts with PreparedStatement
- Mixed batch operations
- BatchUpdateException handling

### Example 6: MetaData
```bash
mvn compile exec:java -Dexec.mainClass="jdbc.Example06_MetaData"
```

Topics covered:
- DatabaseMetaData for database information
- ResultSetMetaData for column information
- Getting table and column details
- Dynamic result processing

## Running from IntelliJ IDEA

1. Navigate to the example file (e.g., `Example01_BasicConnection.java`)
2. Right-click on the file or the main method
3. Select "Run 'Example01_BasicConnection.main()'"

## Database Management

### Stop the database:
```bash
docker-compose down
```

### Stop and remove all data:
```bash
docker-compose down -v
```

### Restart the database:
```bash
docker-compose restart
```

### Reset the database to initial state:
```bash
docker-compose down -v
docker-compose up -d
```

## OCP Exam Topics Covered

This setup covers key JDBC topics for the Java SE 17 Developer (1Z0-829) exam:

1. **Connecting to databases using JDBC URLs and DriverManager**
2. **Constructing and using RowSet objects**
3. **Performing CRUD operations (Create, Read, Update, Delete)**
4. **Processing query results using ResultSet**
5. **Using PreparedStatement and CallableStatement**
6. **Controlling transactions**
7. **Using batch updates**
8. **Working with database metadata**

## Additional Practice Ideas

- Modify the examples to use different SQL queries
- Create your own tables and practice with them
- Experiment with different ResultSet types and concurrency modes
- Practice error handling and exception scenarios
- Try using connection pooling libraries (HikariCP, C3P0)

## Troubleshooting

### Connection refused error
- Make sure Docker is running: `docker ps`
- Check if PostgreSQL container is up: `docker-compose ps`
- Verify port 5432 is not used by another application

### Class not found: org.postgresql.Driver
- Ensure Maven dependencies are downloaded
- In IntelliJ: Right-click on `pom.xml` → Maven → Reload Project

### Can't connect to database from Java
- Verify the database is accessible: `docker-compose logs postgres`
- Check connection string in `DatabaseConfig.java`
- Ensure no firewall is blocking port 5432

## Resources

- [PostgreSQL JDBC Driver Documentation](https://jdbc.postgresql.org/documentation/)
- [Java SE 17 JDBC API Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/module-summary.html)
- [OCP Java SE 17 Developer Exam Topics](https://education.oracle.com/java-se-17-developer/pexam_1Z0-829)
