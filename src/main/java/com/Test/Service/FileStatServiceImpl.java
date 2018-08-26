package com.Test.Service;

import com.Test.Dao.FileStatDao;
import com.Test.Model.FileStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileStatServiceImpl implements FileStatService {

    @Autowired
    FileStatDao fileStatDao;

    @Override
    public List<FileStat> getAll() {
        return fileStatDao.getAll();
    }

    @Override
    public FileStat getByName(String name) {
        return fileStatDao.getByName(name);
    }

    @Override
    public void saveFileStat(File file) {
        fileStatDao.saveFileStat(calcFileStat(file));
    }

    private FileStat calcFileStat(File file){
        FileStat fileStat = null;
        try(Stream<String> linesStream = Files.lines(Paths.get(file.getPath()))){

            List<String> linesList = linesStream.filter(str -> str.length()>0).collect(Collectors.toList());

            int countLine = linesList.size();

            double sum = 0;
            for (String str: linesList) {
                sum += str.split(" +").length;
            }

            double avgWordInLines = sum/countLine;

            List<String> wordList = Arrays.asList(linesList.stream().collect(Collectors.joining(" ")).split(" +"));

            int maxWord = wordList.stream().max(Comparator.comparingInt(String::length)).get().length();
            int minWord = wordList.stream().min(Comparator.comparingInt(String::length)).get().length();

            fileStat = new FileStat(file.getName(),countLine,avgWordInLines,maxWord,minWord);

        }catch (Exception e){
            System.out.println("Exception in calcFileStat method");
        }

        return fileStat;
    }

}
