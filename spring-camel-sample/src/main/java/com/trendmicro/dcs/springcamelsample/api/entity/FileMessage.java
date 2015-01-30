package com.trendmicro.dcs.springcamelsample.api.entity;

import java.util.Map;

public class FileMessage extends Message {
	
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
