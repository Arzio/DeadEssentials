package com.arzio.deadessentials.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.bukkit.command.CommandSender;

public class ThrowableHelper {
	
	public static String getStackTrace(Throwable throwable) {
		StringWriter writer = new StringWriter();
		PrintWriter print = new PrintWriter(writer);
		throwable.printStackTrace(print);
		
		try {
			writer.close();
			print.close();
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void printStackTrace(Throwable throwable, CommandSender sender) {
		sender.sendMessage(getStackTrace(throwable));
	}
}
