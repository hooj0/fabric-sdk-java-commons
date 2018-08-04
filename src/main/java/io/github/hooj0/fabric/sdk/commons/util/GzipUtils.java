package io.github.hooj0.fabric.sdk.commons.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import io.github.hooj0.fabric.sdk.commons.FabricRootException;

import static java.lang.String.format;

public final class GzipUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private GzipUtils() {
	}

	/**
	 * Generate a targz inputstream from source folder.
	 * @param src
	 *            Source location
	 * @param pathPrefix
	 *            prefix to add to the all files found.
	 * @return return inputstream.
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static InputStream generateTarGzInputStream(File sourceDirectory, String pathPrefix) throws IOException {
		String sourcePath = sourceDirectory.getAbsolutePath();

		ByteArrayOutputStream bos = new ByteArrayOutputStream(500000);
		TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(new GzipCompressorOutputStream(new BufferedOutputStream(bos)));
		archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

		try {
			Collection<File> childrenFiles = org.apache.commons.io.FileUtils.listFiles(sourceDirectory, null, true);
			for (File childFile : childrenFiles) {
				String childPath = childFile.getAbsolutePath();
				
				String relativePath = childPath.substring((sourcePath.length() + 1), childPath.length());
				if (pathPrefix != null) {
					relativePath = org.hyperledger.fabric.sdk.helper.Utils.combinePaths(pathPrefix, relativePath);
				}
				relativePath = FilenameUtils.separatorsToUnix(relativePath);

				ArchiveEntry archiveEntry = new TarArchiveEntry(childFile, relativePath);
				FileInputStream fileInputStream = new FileInputStream(childFile);
				archiveOutputStream.putArchiveEntry(archiveEntry);

				try {
					IOUtils.copy(fileInputStream, archiveOutputStream);
				} finally {
					IOUtils.closeQuietly(fileInputStream);
					archiveOutputStream.closeArchiveEntry();
				}
			}
		} finally {
			IOUtils.closeQuietly(archiveOutputStream);
		}

		return new ByteArrayInputStream(bos.toByteArray());
	}

	public static File findFileSk(File directory) {
		File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));

		if (null == matches) {
			throw new FabricRootException(format("Matches returned null does '%s' directory exist?", directory.getAbsoluteFile()));
		}

		if (matches.length != 1) {
			throw new FabricRootException(format("Expected in '%s' only 1 sk file but found %d", directory.getAbsoluteFile(), matches.length));
		}

		return matches[0];
	}
}
