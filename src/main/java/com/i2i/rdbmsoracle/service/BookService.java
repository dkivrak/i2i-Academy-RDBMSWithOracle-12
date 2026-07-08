package com.i2i.rdbmsoracle.service;

import com.i2i.rdbmsoracle.dto.BookDto;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final DataSource dataSource;

    public BookService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void importBooks(String rawData) {
        String sql = """
                DECLARE
                    v_xml CLOB;
                    v_json CLOB;
                BEGIN
                    v_xml := BOOK_OPERATIONS.RAW_TO_XML(?);
                    v_json := BOOK_OPERATIONS.RAW_TO_JSON(?);
                    BOOK_OPERATIONS.INSERT_BOOKS(v_xml, v_json);
                    COMMIT;
                END;
                """;

        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, rawData);
            statement.setString(2, rawData);
            statement.execute();

        } catch (Exception e) {
            throw new RuntimeException("Failed to import books: " + e.getMessage(), e);
        }
    }

    public List<BookDto> getAllBooks() {
        String sql = "BEGIN BOOK_OPERATIONS.GET_ALL_BOOKS(?); END;";

        List<BookDto> books = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.registerOutParameter(1, Types.REF_CURSOR);
            statement.execute();

            try (ResultSet resultSet = (ResultSet) statement.getObject(1)) {
                while (resultSet.next()) {
                    books.add(new BookDto(
                            resultSet.getLong("ID"),
                            resultSet.getString("TITLE"),
                            resultSet.getString("AUTHOR"),
                            resultSet.getString("PUBLISHER")
                    ));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch books: " + e.getMessage(), e);
        }

        return books;
    }
}