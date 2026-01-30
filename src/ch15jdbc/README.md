# Chapter 15: JDBC - OCP Java 17 Exam Prep

## Overview
This chapter covers all JDBC topics for the OCP Java 17 certification exam. Each file focuses on a specific aspect of JDBC with comprehensive documentation, examples, and exam tips.

## Files Created

### 1. `JDBCBasics.java`
**Topics:**
- CRUD operations and SQL keywords
- JDBC interfaces (Driver, Connection, PreparedStatement, CallableStatement, ResultSet)
- JDBC URL structure (jdbc:subprotocol:subname)
- Getting a Connection with DriverManager (all 3 signatures)
- Factory pattern
- SQLException handling
- Try-with-resources

**Run:** `java -cp src ch15jdbc.JDBCBasics`

### 2. `PreparedStatementExamples.java`
**Topics:**
- Creating PreparedStatement from Connection
- Execute methods: executeUpdate(), executeQuery(), execute()
- Which SQL works with which method (table)
- Return types for each method
- Binding variables (?) and 1-based indexing
- Setter methods: setInt, setString, setDouble, setBoolean, setLong, setNull, setObject
- Reusing PreparedStatement with different parameters
- Common SQLException causes

**Run:** `java -cp src ch15jdbc.PreparedStatementExamples`

### 3. `ResultSetExamples.java`
**Topics:**
- ResultSet cursor basics (starts BEFORE first row)
- Navigating with next()
- Loop patterns (while and if)
- Getter methods: getInt, getString, getDouble, getBoolean, getLong, getObject
- Accessing columns by index (1-based) vs by name
- Common SQLException causes with ResultSet

**Run:** `java -cp src ch15jdbc.ResultSetExamples`

### 4. `JDBCExamReference.java`
**Quick reference guide with:**
- CRUD to SQL mapping table
- JDBC URL format
- DriverManager signatures
- Execute method comparison tables
- PreparedStatement setters list
- ResultSet getters list
- Common SQLException causes
- Top 10 exam traps
- Exam day checklist
- What's NOT on the exam

**Note:** This is a reference guide with no executable code.

## Database Setup

### Start the PostgreSQL Database
```bash
docker-compose up -d
```

### Verify Database is Running
```bash
docker ps | grep jdbc-practice-db
```

### Stop the Database
```bash
docker-compose down
```

### Database Connection Details
- **URL:** `jdbc:postgresql://localhost:5432/ocp_practice`
- **Username:** `ocpuser`
- **Password:** `ocppass123`
- **Tables:** employees, departments, projects

## Running the Examples

### Compile all files:
```bash
javac src/ch15jdbc/*.java
```

### Run individual files:
```bash
# JDBC Basics
java -cp src ch15jdbc.JDBCBasics

# PreparedStatement Examples
java -cp src ch15jdbc.PreparedStatementExamples

# ResultSet Examples
java -cp src ch15jdbc.ResultSetExamples
```

### Or run all at once:
```bash
java -cp src ch15jdbc.JDBCBasics && \
java -cp src ch15jdbc.PreparedStatementExamples && \
java -cp src ch15jdbc.ResultSetExamples
```

## Key Exam Points

### Must Memorize:
1. **JDBC URL Format:** `jdbc:subprotocol:subname` (3 parts, always starts with "jdbc")
2. **Execute Methods:**
   - `executeUpdate()` → int (INSERT, UPDATE, DELETE)
   - `executeQuery()` → ResultSet (SELECT only)
   - `execute()` → boolean (any SQL)
3. **JDBC uses 1-based indexing** (NOT 0-based like arrays!)
4. **SQLException is CHECKED** - must handle or declare
5. **ResultSet cursor starts BEFORE first row** - must call next() first

### Common Exam Traps:
- Using index 0 for parameters or columns ❌
- `executeUpdate()` with SELECT ❌
- `executeQuery()` with INSERT/UPDATE/DELETE ❌
- Accessing ResultSet before calling next() ❌
- Forgetting SQLException is checked ❌

### Not on Exam:
- SQL syntax errors (won't be tested)
- Statement interface (only PreparedStatement and CallableStatement)
- DataSource (only DriverManager)
- Transactions, batch updates, scrollable ResultSets

## Study Approach

1. **Day 1-2:** Read all documentation comments in each file
2. **Day 3-4:** Run examples and observe output
3. **Day 5:** Review JDBCExamReference.java (all tables and traps)
4. **Day 6:** Test yourself on the Exam Day Checklist
5. **Day 7:** Run all examples again and review any weak areas

## Need Help?

If you encounter issues:
1. Ensure Docker is running: `docker ps`
2. Check database is accessible: `docker logs jdbc-practice-db`
3. Verify PostgreSQL JDBC driver is available (should be handled by Docker)

Good luck on your exam! 🍀
