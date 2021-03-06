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

public class Config {

	private String host = "";
	private String project = "";
	private String configPath = "";
	private String librariesPath = "";
	
	public Config(String host, String project, String configPath, String librariesPath) {
		this.host = host;
		this.project = project;
		this.configPath = configPath;
		this.librariesPath = librariesPath;
	}

	public String getProject() {
		return project;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getConfigPath() {
		return configPath;
	}
	
	public String getLibrariesPath() {
		return librariesPath;
	}
	
}
