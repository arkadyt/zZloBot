package com.surveyo.zzlobot;

import android.os.Environment;

/**
 * TODO Comment on: SPLIT_STATES
 * */

final public class Constants {
	static final public String HTML_MARQUEE_MAIN_SCREEN = ""
			+ "<html>"
			+ "<body style=\""
			+ "padding: 0px;"
			+ "margin: 0px;"
			+ "background: #FFAE00;"
			+ "background: -webkit-linear-gradient(linear, 0 0, 0 bottom, from(#FFDE00), to(#FF8000));"
			+ "background: -webkit-linear-gradient(#FFDE00, #FF8000);"
			+ "background: -moz-linear-gradient(#FFDE00, #FF8000);"
			+ "background: -ms-linear-gradient(#FFDE00, #FF8000);"
			+ "background: -o-linear-gradient(#FFDE00, #FF8000);"
			+ "background: linear-gradient(#FFDE00, #FF8000);\">"
			+ "<marquee style=\"" + "font-family: consolas, monospace;\">"
			+ "Created by arcan770077f --- Ник в игре зЛые_СвИнъи"
			+ "</marquee>" + "</body>" + "</html>";
	static final public String HTML_MIME_TYPE = "text/html";
	static final public String HTML_ENCODING = "utf-8";
	static final public String PATH_TO_THE_HIROMACRO = Environment
			.getExternalStorageDirectory() + "/HiroMacro/Documents/";
	static final public int CLIP_TYPE_SUPER_HITS = 0,
			CLIP_TYPE_SHIELD_TURNS = 1, CLIP_TYPE_POTIONS = 2,
			CLIP_TYPE_BOT_NAME = 3, CLIP_TYPE_AFTER_FIGHT_ACTION = 4,
			CLIP_TYPE_BOT_PURPOSE = 5, CLIP_TYPE_DIALOGS = 6;
	static final public String SPREFSKEY_BOT_SLEEP_TIME_NORMAL = "INT_SLT_N",
			SPREFSKEY_BOT_SLEEP_TIME_TOURNAMENT = "INT_SLT_A";
}
