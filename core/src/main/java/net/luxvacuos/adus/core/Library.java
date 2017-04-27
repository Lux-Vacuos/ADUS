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
import java.util.ArrayList;
import java.util.List;

/**
 * Library, virtual representation of a library.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class Library {

	private String name;
	private String version;
	private String domain;
	private List<Library> dependencies;

	/**
	 * 
	 * @param name
	 *            Library Name
	 * @param domain
	 *            Library Domain
	 * @param version
	 *            Library Version
	 */
	public Library(String name, String domain, String version) {
		this.name = name;
		this.version = version;
		this.domain = domain;
		dependencies = new ArrayList<>();
	}

	/**
	 * Download the library and the dependencies
	 */
	public void download() {
		String path = "/" + ProjectVariables.CONFIG.getProject() + "/" + ProjectVariables.CONFIG.getLibrariesPath()
				+ "/" + domain + "/" + name + "/" + version + "/" + name + "-" + version + ".jar";
		File lib = new File(ProjectVariables.PREFIX + ProjectVariables.CONFIG.getProject() + path);
		lib.getParentFile().mkdirs();
		if (!lib.exists())
			DownloadsHelper.download(lib.getPath(), path);
		for (Library library : dependencies) {
			library.download();
		}
	}

	public String getName() {
		return name;
	}

	public String getDomain() {
		return domain;
	}

	public String getVersion() {
		return version;
	}

	public List<Library> getDependencies() {
		return dependencies;
	}

	public String getClassPath() {
		StringBuilder builder = new StringBuilder();
		for (Library library : getDependencies()) {
			builder.append(ProjectVariables.PREFIX + ProjectVariables.CONFIG.getProject() + "/"
					+ ProjectVariables.CONFIG.getLibrariesPath() + "/" + library.getDomain() + "/" + library.getName()
					+ "/" + library.getVersion() + "/" + library.getName() + "-" + library.getVersion() + ".jar"
					+ ProjectVariables.SEPARATOR);
			builder.append(library.getClassPath());
		}
		return builder.toString();
	}

}
