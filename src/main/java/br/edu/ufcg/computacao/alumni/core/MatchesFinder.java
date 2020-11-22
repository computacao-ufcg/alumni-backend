package br.edu.ufcg.computacao.alumni.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.DateRange;
import br.edu.ufcg.computacao.alumni.core.models.Degree;
import br.edu.ufcg.computacao.alumni.core.models.Level;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinSchoolData;
import br.edu.ufcg.computacao.alumni.core.models.ParsedName;
import br.edu.ufcg.computacao.alumni.core.models.SchoolName;
import br.edu.ufcg.computacao.alumni.core.util.ScoreComparator;

public class MatchesFinder {
	private static final Logger LOGGER = Logger.getLogger(MatchesFinder.class);

	private static MatchesFinder instance;

	private MatchesFinder() {
	}

	public static synchronized MatchesFinder getInstance() {
		if (instance == null) {
			instance = new MatchesFinder();
		}
		return instance;
	}

	public Map<String, Collection<LinkedinAlumnusData>> findMatches(UfcgAlumnusData alumnus, SchoolName school) {
		Collection<LinkedinAlumnusData> linkedinProfilesList = LinkedinDataHolder.getInstance().getLinkedinAlumniData();
		Map<String, Collection<LinkedinAlumnusData>> selectedProfilesList = new TreeMap<>(new ScoreComparator()); // relaciona o score com uma lista
		
		String alumnusName = alumnus.getFullName().toUpperCase();

		linkedinProfilesList.forEach(linkedinProfile -> {
			int score = 0;

			LinkedinSchoolData[] linkedinSchoolData = linkedinProfile.getSchools();
			String linkedinAlumniFullName = linkedinProfile.getFullName().toUpperCase();

			score += getScoreFromName(alumnusName, linkedinAlumniFullName);
			LOGGER.debug(String.format("Comparing: %s com %s: %d", alumnusName, linkedinAlumniFullName, score));
			score += getScoreFromSchool(alumnus, linkedinSchoolData, school);

			String scoreString = String.format("%d", score);

			if (score >= 1) {
				if (!selectedProfilesList.containsKey(scoreString)) {
					selectedProfilesList.put(scoreString, new ArrayList<>());
				}
				selectedProfilesList.get(scoreString).add(linkedinProfile);
			}
		});

		return selectedProfilesList;
	}

	private String[] filterName(String name) {
		if (name == null) return new String[] {};

		Set<String> connectors = new HashSet<>(Arrays.asList(new String[] {"DE", "DA", "DO", "DOS", "DAS", "DI", "E"}));
		String[] rawName = name.split(" ");
		String[] splitedName = normalize(rawName);
		String[] splitedFilteredName = new String[splitedName.length];

		int count = 0;
		for (int i = 0; i < splitedName.length; i++) {
			String namePart = splitedName[i];
			if (!connectors.contains(namePart)) {
				splitedFilteredName[i] = namePart;
				count++;
			}
		}
		
		if (count == splitedFilteredName.length) return splitedFilteredName;
		
		String[] resizedSplitedFilteredName = new String[count];
		int index = 0;
		for (String filteredName : splitedFilteredName) {
			if (filteredName != null) {
				resizedSplitedFilteredName[index] = filteredName;
				index++;
			}
		}
		return resizedSplitedFilteredName;
	}

	private String[] normalize(String[] rawName) {
		String[] normalizedName = new String[rawName.length];
		for (int i = 0; i < rawName.length; i++) {
			normalizedName[i] = (rawName[i].trim()).toUpperCase();
		}
		return normalizedName;
	}

	private int compareNames(String[] namesToCompare, String[] names) {
		int score = 0;

		for (String name1 : namesToCompare) {
			for (String name2 : names) {
				if (name1 != null && name2 != null && name1.equals(name2)) {
					score += 10;
				}
			}
		}
		return score;
	}

	private ParsedName getParsedName(String name) {
		String[] names = new String[0];
		String[] surnames = new String [0];
		String suffix = null;
		int namePartsSize;
		int nameLength;
		int suffixLength = 0;

		Set<String> suffixes = new HashSet<>(Arrays.asList(new String[]{"JUNIOR", "JR", "NETO", "SOBRINHO", "JR.", "FILHO"}));

		String[] splitedName = filterName(name);
		namePartsSize = splitedName.length;

		if (namePartsSize == 0) {
			return new ParsedName(names, surnames, suffix);
		}

		if (suffixes.contains(splitedName[namePartsSize - 1])) {
			suffixLength = 1;
			suffix = splitedName[namePartsSize - 1];
			if (namePartsSize == 1) {
				return new ParsedName(new String[]{suffix}, surnames, suffix);
			}
		}

		if ((namePartsSize - suffixLength) >= 4) {
			names = new String[]{splitedName[0], splitedName[1]};
			nameLength = 2;
		} else {
			names = new String[]{splitedName[0]};
			nameLength = 1;
		}

		surnames = new String[(namePartsSize - nameLength - suffixLength)];
		for (int i = nameLength; i < (namePartsSize - suffixLength); i++) {
			surnames[i - nameLength] = splitedName[i];
		}

		return new ParsedName(names, surnames, suffix);
	}

	private int getScoreFromName(String alumniName, String linkedinName) {
		if (alumniName.equals(linkedinName)) return 200;

		ParsedName alumniParsedName = getParsedName(alumniName);
		ParsedName linkedinParsedName = getParsedName(linkedinName);

		int score = 0;
		
		if (alumniParsedName.isComposed()) {
			linkedinParsedName.turnComposed();
		}
		
		score += compareNames(alumniParsedName.getNames(), linkedinParsedName.getNames());
		score += compareNames(alumniParsedName.getSurnames(), linkedinParsedName.getSurnames());

		if (alumniParsedName.getSuffix() != null && linkedinParsedName.getSuffix() != null
				&& alumniParsedName.getSuffix().equals(linkedinParsedName.getSuffix())) score += 20;

		return score;
	}

	private Degree getDegreeData(Degree[] alumniGraus, CourseName curso, Level degreeLevel) {
		return Arrays.asList(alumniGraus)
				.stream()
				.filter(grau -> grau.getCourseName().equals(curso))
				.findAny()
				.orElse(null);
	}

	private int getMatchesBasedOnSchoolData(UfcgAlumnusData alumnus, LinkedinSchoolData schoolData, SchoolName school) {
		Degree alumnusDegreeData = getDegreeData(alumnus.getDegrees(), schoolData.getCourseName(), schoolData.getDegreeLevel());

		if (alumnusDegreeData == null) {
			return 0;
		}

		String schoolUrl = schoolData.getSchoolUrl();
		DateRange linkedinSchoolDateRange = schoolData.getDateRange();

		int score = 0;

		for (int i = 0; i < school.getNames().length; i++) {
			String name = school.getNames()[i];

			if (name.equals(schoolUrl)) {
				score += 10;
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
