package br.edu.ufcg.computacao.alumni.core.models;

import java.util.Arrays;

public class ParsedName {

	private String[] names;
	private String[] surnames;
	private String suffix;
	
	public ParsedName(String[] names, String[] surnames, String suffix) {
		this.names = (names == null ? new String[0] : names);
		this.surnames = (surnames == null ? new String[0] : surnames);
		this.suffix = suffix;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public String[] getSurnames() {
		return surnames;
	}

	public void setSurnames(String[] surnames) {
		this.surnames = surnames;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public boolean isComposed() {
		return names.length >= 2;
	}
	
	public void turnComposed() {
		this.names = new String[] { this.names[0], this.surnames[0] };
		
		this.surnames = new String[this.surnames.length - 1];
		for (int i = 1; i < this.surnames.length; i++) {
			this.surnames[i - 1] = this.surnames[i];
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(names);
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
		result = prime * result + Arrays.hashCode(surnames);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParsedName other = (ParsedName) obj;
		if (!Arrays.equals(names, other.names))
			return false;
		if (suffix == null) {
			if (other.suffix != null)
				return false;
		} else if (!suffix.equals(other.suffix))
			return false;
		if (!Arrays.equals(surnames, other.surnames))
			return false;
		return true;
	}
}
