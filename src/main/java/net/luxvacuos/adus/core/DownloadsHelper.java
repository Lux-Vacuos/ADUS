/*
 * This file is part of ADUS
 * 
 * Copyright (C) 2017 Lux Vacuos
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

package net.luxvacuos.adus.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.luxvacuos.adus.utils.Logger;

public class DownloadsHelper {
	
	private DownloadsHelper() {
	}
	
	public static boolean download(String local, String host) {
		try {
			Logger.log("Downloading: " + host);
			URL url = new URL(ProjectVariables.CONFIG.getHost() + host);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			FileOutputStream fos = new FileOutputStream(local);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] data = new byte[1024];
			int i = 0;
			while ((i = in.read(data, 0, 1024)) >= 0) {
				bout.write(data, 0, i);
			}
			bout.close();
			in.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("Download failed: " + host);
			return false;
		}
	}

}
