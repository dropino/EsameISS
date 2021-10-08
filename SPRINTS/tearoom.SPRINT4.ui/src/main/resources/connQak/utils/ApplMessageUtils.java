package connQak.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unibo.kactor.ApplMessage;

public class ApplMessageUtils {

	public static String extractApplMessagePayload(ApplMessage msg, int idx) {
		String result = null;

		String[] payloadArgs = extractApplMessagePayloadArgs(msg);

		if (payloadArgs == null || payloadArgs.length <= idx)
			return result;

		return payloadArgs[idx];
	}

	public static String[] extractApplMessagePayloadArgs(ApplMessage msg) {
		String result = null;

		Pattern pattern = Pattern.compile("\\((.*?)\\)");
		Matcher matcher = pattern.matcher(msg.msgContent());
		if (matcher.find()) {
			result = matcher.group(1);
		}

		String[] payloadArgs = result.split(",");

		return payloadArgs;
	}
}
