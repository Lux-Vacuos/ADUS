/*
 * This file is part of ADUS
 * 
 * Copyright (C) 2017-2019 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.adus.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;

import net.luxvacuos.adus.core.Device;
import net.luxvacuos.adus.core.ProjectVariables;

public class PerRunLog extends FileAppender {

	private static SimpleDateFormat timeStampFormat;

	static {
		timeStampFormat = new SimpleDateFormat("yyyy-MM-dd-h-mm-ss-a");
	}

	public PerRunLog() {
	}

	public PerRunLog(Layout layout, String filename, boolean append, boolean bufferedIO, int bufferSize)
			throws IOException {
		super(layout, filename, append, bufferedIO, bufferSize);
	}

	public PerRunLog(Layout layout, String filename, boolean append) throws IOException {
		super(layout, filename, append);
	}

	public PerRunLog(Layout layout, String filename) throws IOException {
		super(layout, filename);
	}

	@Override
	public void activateOptions() {
		if (fileName != null) {
			try {
				fileName = getNewLogFileName();
				setFile(fileName, fileAppend, bufferedIO, bufferSize);
			} catch (Exception e) {
				errorHandler.error("Error while activating log options", e, ErrorCode.FILE_OPEN_FAILURE);
			}
		}
	}

	private String getNewLogFileName() {
		if (fileName != null) {
			final String DOT = ".";
			final String HIPHEN = "-";
			final File logFile = new File(fileName);
			final String fileName = logFile.getName();
			String newFileName = "";

			final int dotIndex = fileName.indexOf(DOT);
			if (dotIndex != -1) {
				newFileName = fileName.substring(0, dotIndex) + HIPHEN + timeStampFormat.format(new Date()) + DOT
						+ fileName.substring(dotIndex + 1, dotIndex + 4);
			} else {
				newFileName = fileName + HIPHEN + timeStampFormat.format(new Date());
			}
			return Device.getPrefix() + ProjectVariables.CONFIG.getProject() + "/" + logFile.getParent() + File.separator + newFileName;
		}
		return null;
	}
}
