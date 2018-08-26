package com.Test.Service;

import com.Test.Model.FileStat;

import java.io.File;
import java.util.List;

public interface FileStatService {

    List<FileStat> getAll();

    FileStat getByName(String name);

    void saveFileStat(File file);
}
