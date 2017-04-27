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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.luxvacuos.adus.core.ADUS.Platform;

public class VersionsManager {

	private static VersionsManager versionsManager;

	public static VersionsManager getVersionsManager() {
		if (versionsManager == null)
			versionsManager = new VersionsManager();
		return versionsManager;
	}

	private File local = new File(ProjectVariables.PREFIX + ProjectVariables.CONFIG.getProject() + "/"
			+ ProjectVariables.CONFIG.getConfigPath() + "/versions.json");
	private Gson gson;
	private RemoteVersions remoteVersions;

	private VersionsManager() {
		gson = new Gson();
	}

	public void update() {
		try {
			local.getParentFile().mkdirs();
			DownloadsHelper.download(local.getPath(), "/" + ProjectVariables.CONFIG.getProject() + "/"
					+ ProjectVariables.CONFIG.getConfigPath() + "/versions.json");
			if (local.exists())
				remoteVersions = gson.fromJson(new FileReader(local), RemoteVersions.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadAndRun(String version, VersionKey key)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		File verf = new File(ProjectVariables.PREFIX + ProjectVariables.CONFIG.getProject() + "/"
				+ ProjectVariables.CONFIG.getConfigPath() + "/versions/" + version + "/" + key.name + "-" + key.version
				+ ".json");
		verf.getParentFile().mkdirs();
		if (!verf.exists())
			DownloadsHelper.download(verf.getPath(),
					"/" + ProjectVariables.CONFIG.getProject() + "/" + ProjectVariables.CONFIG.getConfigPath()
							+ "/versions/" + version + "/" + key.name + "-" + key.version + ".json");
		Version ver = gson.fromJson(new FileReader(verf), Version.class);
		ver.download();
		ProcessBuilder pb;
		if (ADUS.getPlatform().equals(Platform.MACOSX)) {
			pb = new ProcessBuilder("java", "-XX:+UseG1GC", "-XstartOnFirstThread", "-Xmx1G", "-classpath",
					getClassPath(ver), ver.getMain());
		} else {
			pb = new ProcessBuilder("java", "-XX:+UseG1GC", "-Xmx1G", "-classpath", getClassPath(ver), ver.getMain());
		}
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void downloadAndRun() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		String key = (String) remoteVersions.getVersions().keySet().toArray()[0];
		List<VersionKey> versions = remoteVersions.getVersions(key);
		downloadAndRun(key, versions.get(0));
	}

	private String getClassPath(Version ver) {
		StringBuilder builder = new StringBuilder();
		int size = ver.getLibs().size();
		int count = 0;
		builder.append(builder.append(ProjectVariables.PREFIX + ProjectVariables.CONFIG.getProject() + "/"
				+ ProjectVariables.CONFIG.getLibrariesPath() + "/" + ver.getDomain() + "/" + ver.getName() + "/"
				+ ver.getVersion() + "/" + ver.getName() + "-" + ver.getVersion() + ".jar"
				+ ProjectVariables.SEPARATOR));
		for (Library library : ver.getLibs()) {
			count++;
			builder.append(library.getClassPath());
			if (count == size)
				builder.append(ProjectVariables.PREFIX + ProjectVariables.CONFIG.getProject() + "/"
						+ ProjectVariables.CONFIG.getLibrariesPath() + "/" + library.getDomain() + "/"
						+ library.getName() + "/" + library.getVersion() + "/" + library.getName() + "-"
						+ library.getVersion() + ".jar");
			else
				builder.append(ProjectVariables.PREFIX + ProjectVariables.CONFIG.getProject() + "/"
						+ ProjectVariables.CONFIG.getLibrariesPath() + "/" + library.getDomain() + "/"
						+ library.getName() + "/" + library.getVersion() + "/" + library.getName() + "-"
						+ library.getVersion() + ".jar" + ProjectVariables.SEPARATOR);
		}
		return builder.toString();
	}

}
