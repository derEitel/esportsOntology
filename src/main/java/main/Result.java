package main;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {
    private String competitions;
	private String teams;

	public String getCompetitions() {
		return competitions;
	}
	public void setCompetitions(String competitions) {
		this.competitions = competitions;
	}
	public String getTeams() {
		return teams;
	}
	public void setTeams(String teams) {
		this.teams = teams;
	}
}