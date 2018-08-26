package com.Test.Mapper;

import com.Test.Model.FileStat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileStatMapper implements RowMapper<FileStat> {
    @Override
    public FileStat mapRow(ResultSet resultSet, int i) throws SQLException {

        FileStat fileStat = new FileStat();
        fileStat.setName(resultSet.getString("file_name"));
        fileStat.setCountLine(resultSet.getInt("count_line"));
        fileStat.setAvgWordInLines(resultSet.getDouble("avg_words_in_line"));
        fileStat.setMaxWord(resultSet.getInt("max_word_length"));
        fileStat.setMinWord(resultSet.getInt("min_word_length"));

        return fileStat;
    }
}
