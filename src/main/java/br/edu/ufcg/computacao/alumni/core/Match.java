package br.edu.ufcg.computacao.alumni.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.*;
import br.edu.ufcg.computacao.alumni.core.models.Degree;

public class Match {

	private static Match instance;

	private Match() {
	}

	public static synchronized Match getInstance() {
		if (instance == null) {
			instance = new Match();
		}
		return instance;
	}

	public Map<Integer, Collection<LinkedinAlumnusData>> getMatches(UfcgAlumnusData alumnus, SchoolName school) throws Exception {
		Collection<LinkedinAlumnusData> linkedinProfilesList = LinkedinDataHolder.getInstance().getLinkedinAlumniData();
		Map<Integer, Collection<LinkedinAlumnusData>> selectedProfilesList = new TreeMap<>(Collections.reverseOrder()); // relaciona o score com uma lista

		String alumniName = alumnus.getFullName().toUpperCase();

		linkedinProfilesList.forEach(linkedinProfile -> {
			int score = 0;

			LinkedinSchoolData[] linkedinSchoolData = linkedinProfile.getSchools();
			String linkedinAlumniFullName = linkedinProfile.getFullName().toUpperCase();

			score += getScoreFromName(alumniName, linkedinAlumniFullName);
			score += getScoreFromSchool(alumnus, linkedinSchoolData, school);

			if (score >= 1) {
				if (!selectedProfilesList.containsKey(score)) {
					selectedProfilesList.put(score, new ArrayList<>());
				}

				selectedProfilesList.get(score).add(linkedinProfile);
			}
		});

		return selectedProfilesList;
	}

	private CourseName getCourseName(LinkedinSchoolData linkedinSchoolData) {
		String field = linkedinSchoolData.getCourseName().toUpperCase().trim();

		return CourseName.COMPUTING_SCIENCE;
	}

	private Level getDegreeLevel(LinkedinSchoolData linkedinSchoolData) {
		String degree = linkedinSchoolData.getDegreeLevel().toUpperCase().trim();

		return Level.UNDERGRADUATE;
	}

	private String[] filterName(String name) {
		Set<String> connectors = new HashSet<>(Arrays.asList(new String[] {"DE", "DA", "DO", "DOS", "DAS", "E"}));
		String[] splitedName = name.split(" ");
		String[] splitedFilteredName = new String[splitedName.length];

		for (int i = 0; i < splitedName.length; i++) {
			String nome = splitedName[i].toUpperCase();
			if (!connectors.contains(nome)) {
				splitedFilteredName[i] = nome;
			}
		}
		return splitedFilteredName;
	}

	private int getNamesLength(String[] splitedName) {
		int cont = 0;
		for (String nome : splitedName) {
			cont += (nome == null) ? 0 : 1;
		}
		return cont;
	}

	private String[] getFirstName(String name) {
		Set<String> suffixes = new HashSet<>(Arrays.asList(new String[] {"JUNIOR", "JR", "NETO", "SOBRINHO", "JR.", "FILHO"}));
		String[] splitedName = filterName(name);
		int cont = 0;

		for (String nome : splitedName) {
			if (suffixes.contains(nome)) {
				cont++;
			}
		}

		if (getNamesLength(splitedName) - cont <= 4) {
			return new String[] { splitedName[0].trim(), splitedName[1].trim() };
		} else {
			return new String[] { splitedName[0].trim() };
		}
	}

	private String[] getLastName(String name) {
		List<String> names = Arrays.asList(filterName(name));
		List<String> firstNameList = Arrays.asList(getFirstName(name));

		List<String> lastNameList = names.stream().filter(n -> !firstNameList.contains(n)).collect(Collectors.toList());

		String[] lastNames = lastNameList.toArray(new String[0]);

		return lastNames;
	}

	private String getSuffix(String name) {
		Set<String> suffixes = new HashSet<>(Arrays.asList(new String[] {"JUNIOR", "JR", "NETO", "SOBRINHO", "JR.", "FILHO"}));
		String[] names = filterName(name);

		for (String nome : names) {
			if (suffixes.contains(nome)) {
				return nome;
			}
		}
		return null;
	}

	private int compareNames(String[] namesToCompare, String[] names) {
		int score = 0;

		for (String name1 : namesToCompare) {
			for (String name2 : names) {
				if (name1.equals(name2)) {
					score += 20;
				}
			}
		}
		return score;
	}

	private ParsedName getParsedName(String name) {
		String[] names = getFirstName(name);
		String[] surnames = getLastName(name);
		String suffix = getSuffix(name);

		return new ParsedName(names, surnames, suffix);
	}

	private int getScoreFromName(String alumniName, String linkedinName) {
		if (alumniName.equals(linkedinName)) return 100;

		ParsedName alumniParsedName = getParsedName(alumniName);
		ParsedName linkedinParsedName = getParsedName(linkedinName);

		int score = 0;
		score += compareNames(alumniParsedName.getNames(), linkedinParsedName.getNames());
		score += compareNames(alumniParsedName.getSurnames(), linkedinParsedName.getSurnames());

		if (alumniParsedName.getSuffix() != null && alumniParsedName.getSuffix().equals(linkedinParsedName.getSuffix())) score += 20;

		return score;
	}

	private Degree getDegreeData(Degree[] alumniGraus, CourseName curso, Level degreeLevel) {
		return Arrays.asList(alumniGraus)
				.stream()
				.filter(grau -> grau.getCourseName().equals(curso))
				.findAny()
				.orElse(null);
	}

	private int getMatchesBasedOnSchoolData(UfcgAlumnusData alumni, LinkedinSchoolData schoolData, SchoolName school) {
		CourseName courseName = getCourseName(schoolData);
		Level degreeLevel = getDegreeLevel(schoolData);
		Degree alumnusDegreeData = getDegreeData(alumni.getGraus(), courseName, degreeLevel);

		if (alumnusDegreeData == null) {
			return 0;
		}

		String schoolUrl = schoolData.getSchoolUrl();
		DateRange linkedinSchoolDateRange = schoolData.getDateRange();

		int score = 0;

		for (int i = 0; i < school.getNames().length; i++) {
			String name = school.getNames()[i];

			if (name.equals(schoolUrl)) {
				score += 20;
			} else {
				continue;
			}

			DateRange schoolDateRange = school.getDateRanges()[i];
			score += getScoreFromDateRange(linkedinSchoolDateRange, schoolDateRange);
		}

		return score;
	}

	private int getScoreFromDateRange(DateRange linkedinSchoolDateRange, DateRange schoolDateRange) {
		int score = 0;

		if (schoolDateRange.isCurrent()) {
			if (Integer.parseInt(linkedinSchoolDateRange.getEndYear()) >= 2002
					&& !(linkedinSchoolDateRange.getEndMonth().equals("Jan")
						|| linkedinSchoolDateRange.getEndMonth().equals("Fev")
						|| linkedinSchoolDateRange.getEndMonth().equals("Mar"))) {
				score += 20;
			}
		} else {
			if (Integer.parseInt(linkedinSchoolDateRange.getStartYear()) >= 1950 && Integer.parseInt(linkedinSchoolDateRange.getEndYear()) <= 2002
					&& (linkedinSchoolDateRange.getEndMonth().equals("Jan")
						|| linkedinSchoolDateRange.getEndMonth().equals("Fev")
						|| linkedinSchoolDateRange.getEndMonth().equals("Mar"))) {
				score += 20;
			}
		}

		return score;
	}

	private int getScoreFromSchool(UfcgAlumnusData alumni, LinkedinSchoolData[] linkedinSchoolData, SchoolName school) {
		int score = 0;

		for (LinkedinSchoolData linkedinSchool : linkedinSchoolData) {
			score += getMatchesBasedOnSchoolData(alumni, linkedinSchool, school);
		}

		return score;
	}
}
