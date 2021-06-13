package org.warn.utils.file;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FinderTest {

    @Test
    public void testWildCardIncludingDirectoryNames() throws IOException {
        Finder finder = new Finder("*" );
        Files.walkFileTree( Paths.get("/Users/isuru/Downloads/temp"), finder );
        List<Path> files = finder.getMatchedFiles();

        //files.stream().forEach( System.out::println );
        System.out.println( finder.getMatchedCount() );
        System.out.println( finder.getMatchedFilesSize() );
    }

    @Test
    public void testWildCardExcludingDirectoryNames() throws IOException {
        Finder finder = new Finder("*", false );
        Files.walkFileTree( Paths.get("/Users/isuru/Downloads/temp"), finder );
        List<Path> files = finder.getMatchedFiles();

        //files.stream().forEach( System.out::println );
        System.out.println( finder.getMatchedCount() );
        System.out.println( finder.getMatchedFilesSize() );
    }
}
