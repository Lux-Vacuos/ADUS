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

public class RemoteBranch {

	private String branch;
	private String md5;

	public RemoteBranch(String branch, String md5) {
		this.branch = branch;
		this.md5 = md5;
	}

	public String getBranch() {
		return branch;
	}

	public String getMd5() {
		return md5;
	}

}
