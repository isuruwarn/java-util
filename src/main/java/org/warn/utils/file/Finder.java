package org.warn.utils.file;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Finder extends SimpleFileVisitor<Path> {

	private boolean countDirectories;
	private final PathMatcher matcher;
	private final List<Path> matchedFiles;
	private AtomicLong matchedFilesSize = new AtomicLong();

	public Finder( String pattern ) {
		 this( pattern, true );
	}

	public Finder( String pattern, boolean countDirectories ) {
		matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		this.countDirectories = countDirectories;
		matchedFiles = new ArrayList<Path>();
	}
	
	private void find(Path file) {
		Path name = file.getFileName();
		if( name != null && matcher.matches(name) ) {
			matchedFiles.add(file);
			matchedFilesSize.getAndAdd( file.toFile().length() );
		}
	}

	@Override
	public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) {
		find(file);
		return CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) {
		if( countDirectories )
			find(dir);
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed( Path file, IOException exc ) {
		System.err.println(exc);
		return CONTINUE;
	}

	public int getMatchedCount() {
		return matchedFiles.size();
	}

	public List<Path> getMatchedFiles() {
		return matchedFiles;
	}

	public long getMatchedFilesSizeInBytes() {
		return matchedFilesSize.get();
	}

	public String getMatchedFilesSize() {
		return FileHelper.printFileSizeUserFriendly( matchedFilesSize.get() );
	}
 }