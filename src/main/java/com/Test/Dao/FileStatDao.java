package com.Test.Dao;

import com.Test.Model.FileStat;

import java.util.List;

public interface FileStatDao {

    List<FileStat> getAll();

    FileStat getByName(String name);

    void saveFileStat(FileStat file);
}
