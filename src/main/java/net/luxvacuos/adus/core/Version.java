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

import net.luxvacuos.adus.utils.Utils;

/**
 * Object for handling versions.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class Version {

	private String name;
	private String domain;
	private String version;
	private String type;
	private String main;
	private String md5;
	private List<Library> libs;

	/**
	 * 
	 * @param name
	 *            Project Name
	 * @param domain
	 *            Project Domain
	 * @param version
	 *            Project Version
	 * @param type
	 *            Type of Version
	 * @param main
	 *            Main class
	 */
	public Version(String name, String domain, String version, String type, String main, String md5) {
		this.name = name;
		this.domain = domain;
		this.version = version;
		this.type = type;
		this.main = main;
		this.md5 = md5;
		libs = new ArrayList<>();
	}

	/**
	 * Downloads the jar from the server in {@link Config#getHost()} and their
	 * respective libraries and dependencies.
	 */
	public void download() {
		String path = ProjectVariables.CONFIG.getProject() + "/" + ProjectVariables.CONFIG.getLibrariesPath() + "/"
				+ domain + "/" + name + "/" + version + "/" + name + "-" + version + ".jar";
		File jar = new File(ProjectVariables.PREFIX + path);
		jar.getParentFile().mkdirs();
		if (jar.exists()) {
			if (md5 != null)
				if (!md5.equals("")) {
					try {
						if (!md5.equals(Utils.getMD5Checksum(jar))) {
							DownloadsHelper.download(jar.getPath(), "/" + path);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		} else
			DownloadsHelper.download(jar.getPath(), "/" + path);
		for (Library library : libs) {
			library.download();
		}
	}

	public String getVersion() {
		return version;
	}

	public String getType() {
		return type;
	}

	public String getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public String getMain() {
		return main;
	}

	public String getMd5() {
		return md5;
	}

	public List<Library> getLibs() {
		return libs;
	}

}
