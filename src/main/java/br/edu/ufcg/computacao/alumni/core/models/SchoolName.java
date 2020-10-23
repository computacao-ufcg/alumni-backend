package br.edu.ufcg.computacao.alumni.core.models;

public class SchoolName {

	private String[] names;
	private DateRange[] dateRange;
	
	public SchoolName(String[] names, DateRange[] dateRanges) {
		this.names = names;
		this.dateRange = dateRanges;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public DateRange[] getDateRanges() {
		return dateRange;
	}

	public void setDateRanges(DateRange[] dateRange) {
		this.dateRange = dateRange;
	}
	
}
