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
import net.luxvacuos.adus.utils.Utils;

public class VersionsManager {

	private static VersionsManager versionsManager;

	public static VersionsManager getVersionsManager() {
		if (versionsManager == null)
			versionsManager = new VersionsManager();
		return versionsManager;
	}

	private File local = new File(ProjectVariables.PREFIX + ProjectVariables.CONFIG.getProject() + "/"
			+ ProjectVariables.CONFIG.getConfigPath() + "/branches.json");
	private Gson gson;
	private RemoteBranches remoteBranches;

	private VersionsManager() {
		gson = new Gson();
	}

	public void update() {
		try {
			local.getParentFile().mkdirs();
			DownloadsHelper.download(local.getPath(), "/" + ProjectVariables.CONFIG.getProject() + "/"
					+ ProjectVariables.CONFIG.getConfigPath() + "/branches.json");
			if (local.exists())
				remoteBranches = gson.fromJson(new FileReader(local), RemoteBranches.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadAndRun(String version, String branch, VersionKey key, List<String> args) {
		String filePath = ProjectVariables.CONFIG.getProject() + "/" + ProjectVariables.CONFIG.getConfigPath()
				+ "/branches/" + branch + "/" + version + "/" + key.name + "-" + key.version + ".json";
		File verf = new File(ProjectVariables.PREFIX + filePath);
		
		verf.getParentFile().mkdirs();
		if (verf.exists()) {
			if (key.md5 != null)
				if (!key.md5.equals("")) {
					try {
						if (!key.md5.equals(Utils.getMD5Checksum(verf))) {
							DownloadsHelper.download(verf.getPath(), "/" + filePath);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		} else
			DownloadsHelper.download(verf.getPath(), "/" + filePath);
		Version ver = null;
		try {
			ver = gson.fromJson(new FileReader(verf), Version.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e1) {
			e1.printStackTrace();
		}
		ver.download();
		ProcessBuilder pb;
		if (ADUS.getPlatform().equals(Platform.MACOSX)) {
			pb = new ProcessBuilder("java", "-XX:+UseG1GC", "-XstartOnFirstThread", "-Xmx1G", "-classpath",
					getClassPath(ver), ver.getMain());
		} else {
			pb = new ProcessBuilder("java", "-XX:+UseG1GC", "-Xmx1G", "-classpath", getClassPath(ver), ver.getMain());
		}
		pb.command().addAll(args);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void downloadAndRun(List<String> args) {
		RemoteBranch branch = remoteBranches.getBranches().get(0);

		String fileBranch = ProjectVariables.CONFIG.getProject() + "/" + ProjectVariables.CONFIG.getConfigPath()
				+ "/branches/" + branch.getBranch() + ".json";
		File fileBranchF = new File(ProjectVariables.PREFIX + fileBranch);
		fileBranchF.getParentFile().mkdirs();
		if (fileBranchF.exists()) {
			if (branch.getMd5() != null)
				if (!branch.getMd5().equals("")) {
					try {
						if (!branch.getMd5().equals(Utils.getMD5Checksum(fileBranchF))) {
							DownloadsHelper.download(fileBranchF.getPath(), "/" + fileBranch);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		} else
			DownloadsHelper.download(fileBranchF.getPath(), "/" + fileBranch);
		RemoteVersions remoteVersions = null;
		try {
			remoteVersions = gson.fromJson(new FileReader(fileBranchF), RemoteVersions.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}

		String key = (String) remoteVersions.getVersions().keySet().toArray()[0];
		List<VersionKey> versions = remoteVersions.getVersions(key);
		downloadAndRun(key, remoteVersions.getBranch(), versions.get(0), args);
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
			String file = ProjectVariables.PREFIX + ProjectVariables.CONFIG.getProject() + "/"
					+ ProjectVariables.CONFIG.getLibrariesPath() + "/" + library.getDomain() + "/" + library.getName()
					+ "/" + library.getVersion() + "/" + library.getName() + "-" + library.getVersion() + ".jar";
			if (count == size)
				builder.append(file);
			else
				builder.append(file + ProjectVariables.SEPARATOR);
		}
		return builder.toString();
	}

}
