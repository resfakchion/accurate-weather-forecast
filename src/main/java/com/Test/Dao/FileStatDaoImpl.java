package com.Test.Dao;

import com.Test.Mapper.FileStatMapper;
import com.Test.Model.FileStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FileStatDaoImpl implements FileStatDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<FileStat> getAll(){
        String sql = "SELECT * FROM fileStat";
        return jdbcTemplate.query(sql,new FileStatMapper());
    }

    @Override
    public FileStat getByName(String name){
        String sql = "SELECT * FROM fileStat WHERE file_name = ?";
        return jdbcTemplate.queryForObject(sql,new FileStatMapper(),name);
    }

    @Override
    public void saveFileStat(FileStat file){
        String sql = "INSERT INTO fileStat(" +
                "file_name," +
                "count_line," +
                "avg_words_in_line," +
                "max_word_length," +
                "min_word_length) " +
                "VALUES (?,?,?,?,?)";
        createTable();
        jdbcTemplate.update(sql,file.getName(),file.getCountLine(),file.getAvgWordInLines(),file.getMaxWord(),file.getMinWord());
    }

    private void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS fileStat(" +
                "  file_name varchar(50) primary key ," +
                "  count_line int," +
                "  avg_words_in_line double," +
                "  max_word_length int," +
                "  min_word_length int" +
                ")";
        jdbcTemplate.execute(sql);
    }

}
