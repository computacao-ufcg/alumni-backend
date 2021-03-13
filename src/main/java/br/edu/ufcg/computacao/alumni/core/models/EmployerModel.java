package br.edu.ufcg.computacao.alumni.core.models;

import java.util.HashSet;
import java.util.Set;

public class EmployerModel {

	private String name;
	private EmployerType type;
	private Set<String> ids;
	
	public EmployerModel(String name, EmployerType type) {
		this.name = name;
		this.type = type;
		this.ids = new HashSet<>();
	}

	public EmployerModel(String name, EmployerType type, Set<String> ids) {
		this(name, type);
		this.ids = ids;
	}
	
	public String getName() {
		return name;
	}
	
	public EmployerType getType() {
		return type;
	}

	public Set<String> getIds() {
		return ids;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(EmployerType type) {
		this.type = type;
	}

	public void setIds(Set<String> ids) {
		this.ids = ids;
	}

	public boolean addId(String id) {
		return this.ids.add(id);
	}

	public boolean addIds(Set<String> ids) {
		return this.ids.addAll(ids);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		EmployerModel other = (EmployerModel) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
