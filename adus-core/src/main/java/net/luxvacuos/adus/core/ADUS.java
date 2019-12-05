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

package net.luxvacuos.adus.core;

import java.io.InputStreamReader;

import com.google.gson.Gson;

import net.luxvacuos.adus.utils.Logger;

public class ADUS {

	private static Gson gson;

	public static void init() {
		Thread.currentThread().setName("ADUS");
		gson = new Gson();
		Config conf = gson.fromJson(
				new InputStreamReader(Config.class.getClassLoader().getResourceAsStream("project.json")), Config.class);
		ProjectVariables.CONFIG = conf;
		Logger.log("- Initializing ADUS");
		Logger.log("Project: " + conf.getProject());
		Logger.log("Host: " + conf.getHost());
		Logger.log("Base directory: " + Device.getPrefix());
		Logger.log("Config Path: " + conf.getProject() + "/" + conf.getConfigPath());
		Logger.log("Libraries Path: " + conf.getProject() + "/" + conf.getLibrariesPath());
		VersionsManager.update();
		VersionsManager.downloadAndRun();
	}

	public static void main(String[] args) {
		init();
	}

}
