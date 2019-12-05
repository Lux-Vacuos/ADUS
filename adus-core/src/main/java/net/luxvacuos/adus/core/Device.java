package net.luxvacuos.adus.core;

public class Device {

	private static Platform platform;
	private static String prefix;

	public static String getPrefix() {
		if (prefix == null) {

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
		return prefix;
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

}
