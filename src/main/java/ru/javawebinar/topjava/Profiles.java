package ru.javawebinar.topjava;

public class Profiles {
    private static final String
            JDBC = "jdbc",
            JPA = "jpa",
            DATAJPA = "datajpa";

    private static final String REPOSITORY_IMPLEMENTATION = DATAJPA;

    private static final String
            POSTGRES_DB = "postgres",
            HSQL_DB = "hsqldb";

    //  Get DB profile depending of DB driver in classpath
    public static String getActiveDbProfile() {
        try {
            Class.forName("org.postgresql.Driver");
            return POSTGRES_DB;
        } catch (ClassNotFoundException ex) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                return HSQL_DB;
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Could not find DB driver");
            }
        }
    }

    public static String getRepositoryImplementation() {
        return REPOSITORY_IMPLEMENTATION;
    }
}
