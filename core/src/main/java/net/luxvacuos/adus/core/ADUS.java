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

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.luxvacuos.adus.utils.Logger;

public class ADUS {

	private static Platform platform;
	private static String prefix;
	private static Gson gson;

	static {
		if (getPlatform().equals(Platform.WINDOWS_32) || getPlatform().equals(Platform.WINDOWS_64))
			prefix = System.getenv("AppData");
		else if (getPlatform().equals(Platform.LINUX_32) || getPlatform().equals(Platform.LINUX_64))
			prefix = System.getProperty("user.home");
		else if (getPlatform().equals(Platform.MACOSX)) {
			prefix = System.getProperty("user.home");
			prefix += "/Library/Application Support";
		}
		prefix += "/.";
	}

	public enum Platform {
		WINDOWS_32, WINDOWS_64, MACOSX, LINUX_32, LINUX_64, UNKNOWN;
	}

	public static Platform getPlatform() {
		if (platform == null) {
			final String OS = System.getProperty("os.name").toLowerCase();
			final String ARCH = System.getProperty("os.arch").toLowerCase();

			boolean isWindows = OS.contains("windows");
			boolean isLinux = OS.contains("linux");
			boolean isMac = OS.contains("mac");
			boolean is64Bit = ARCH.equals("amd64") || ARCH.equals("x86_64");

			platform = Platform.UNKNOWN;

			if (isWindows)
				platform = is64Bit ? Platform.WINDOWS_64 : Platform.WINDOWS_32;
			if (isLinux)
				platform = is64Bit ? Platform.LINUX_64 : Platform.LINUX_32;
			if (isMac)
				platform = Platform.MACOSX;
		}

		return platform;
	}

	public static void exampleFiles() {
		Config config = new Config("https://s3.luxvacuos.net", "voxel", "config", "libraries");
		try (Writer writer = new FileWriter("project.json")) {
			gson.toJson(config, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		Thread.currentThread().setName("ADUS");
		gson = new Gson();
		ProjectVariables.PREFIX = prefix;
		Config conf = gson.fromJson(
				new InputStreamReader(Config.class.getClassLoader().getResourceAsStream("project.json")), Config.class);
		ProjectVariables.CONFIG = conf;
		Logger.log("- Initializing ADUS");
		Logger.log("Project: " + conf.getProject());
		Logger.log("Host: " + conf.getHost());
		Logger.log("Base directory: " + prefix);
		Logger.log("Config Path: " + conf.getProject() + "/" + conf.getConfigPath());
		Logger.log("Libraries Path: " + conf.getProject() + "/" + conf.getLibrariesPath());
		VersionsManager.getVersionsManager().update();
		try {
			VersionsManager.getVersionsManager().downloadAndRun();
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		init();
	}

}
