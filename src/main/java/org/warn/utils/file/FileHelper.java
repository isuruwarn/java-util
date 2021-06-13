package org.warn.utils.file;

public class FileHelper {
	
	private static final long KB = 1024;
	private static final long MB = 1048576;
	private static final long GB = 1073741824;
	
	public static String printFileSizeUserFriendly( long fileSize ) {
		if( fileSize < KB ) {
			return fileSize + " (bytes)";
		} else if( fileSize >= KB && fileSize < MB ) {
			return formatFileSize( fileSize, KB, " (KB)" );
		} else if( fileSize >= MB && fileSize < GB ) {
			return formatFileSize( fileSize, MB, " (MB)" );
		} else {
			return formatFileSize( fileSize, GB, " (GB)" );
		}
	}
	
	private static String formatFileSize( long fileSize, long unit, String unitStr ) {
		return String.format("%.2f%s", (float) fileSize / unit, unitStr);
	}

}
