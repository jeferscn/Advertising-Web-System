package br.com.unicuritiba.projetopisistemaweb.localstorage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */
	private String location = "C:/Users/Jeferscn/Documents/workspace-spring-tool-suite-4-4.10.0.RELEASE/gs-uploading-files-main.old/complete/src/main/resources/static/anuncios-images";

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
