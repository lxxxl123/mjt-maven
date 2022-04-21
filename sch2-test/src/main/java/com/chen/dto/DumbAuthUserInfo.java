package com.chen.dto;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DumbAuthUserInfo implements UserInfo, UIKeyboardInteractive {

	private String password;

	private String passPhrase;

	public DumbAuthUserInfo(String password) {
		this.password = password;
	}

	@Override
	public boolean promptYesNo(String str) {
		return true;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getPassphrase() {
		return passPhrase;
	}

	@Override
	public boolean promptPassword(String message) {
		return true;
	}

	@Override
	public boolean promptPassphrase(String message) {
		return true;
	}

	@Override
	public void showMessage(String message) {

	}

	@Override
	public String[] promptKeyboardInteractive(String destination, String name,
			String instruction, String[] prompt, boolean[] echo) {
		String[] response = new String[prompt.length];

		for (int i = 0; i < prompt.length; i++) {
			log.debug("Keyboard Interactive prompt [" + prompt[i] + "]");

			if (prompt[i].contains("yes/no")) {
				response[i] = "yes";
				log.debug("Keyboard Interactive response [" + response[i] + "]");
				continue;
			}
			if (prompt[i].contains("assword:") || prompt[i].contains("密码")) {
				response[i] = password;
				log.debug("Keyboard Interactive response [" + response[i] + "]");
			}
		}

		return response;
	}

}
