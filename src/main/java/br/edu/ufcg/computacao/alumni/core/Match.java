package br.edu.ufcg.computacao.alumni.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.holders.PropertiesHolder;
import br.edu.ufcg.computacao.alumni.core.models.Curso;
import br.edu.ufcg.computacao.alumni.core.models.DateRange;
import br.edu.ufcg.computacao.alumni.core.models.Grau;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinSchoolData;
import br.edu.ufcg.computacao.alumni.core.models.ParsedName;
import br.edu.ufcg.computacao.alumni.core.models.SchoolName;

public class Match {

	private PropertiesHolder props;

	private static Match instance;

	private static final String COMPUTACAO = "computacao";
	private static final String INFORMATICA = "informatica";
	private static final String PROC_DADOS = "proc-dados";
	private static final String GRADUACAO = "graduacao";
	private static final String MESTRADO = "mestrado";
	private static final String DOUTORADO = "doutorado";

	private Match() throws IOException {
		this.props = PropertiesHolder.getInstance();
	}

	public static synchronized Match getInstance() throws IOException {
		if (instance == null) {
			instance = new Match();
		}
		return instance;
	}

	private Set<String> getData(String property) {
		String[] values = props.getProperty(property).split(",");

		Set<String> set = new HashSet<>();
		set.addAll(Arrays.asList(values));

		return set;
	}

	private Curso getCurso(LinkedinSchoolData linkedinSchoolData) {
		Set<String> fieldComputacaoSet = getData(COMPUTACAO);
		Set<String> fieldInformaticaSet = getData(INFORMATICA);
		Set<String> fieldProcDadosSet = getData(PROC_DADOS);
		Set<String> degreeGraduacaoSet = getData(GRADUACAO);
		Set<String> degreeMestradoSet = getData(MESTRADO);
		Set<String> degreeDoutoradoSet = getData(DOUTORADO);

		String field = linkedinSchoolData.getField().toUpperCase().trim();
		String degree = linkedinSchoolData.getDegree().toUpperCase().trim();

		if (!(fieldComputacaoSet.contains(field) && fieldInformaticaSet.contains(field) && fieldProcDadosSet.contains(field))) {
			return null;
		}
		if (fieldComputacaoSet.contains(field) && degreeMestradoSet.contains(degree)) {
			return Curso.MESTRADO_EM_CIENCIA_DA_COMPUTACAO;
		}
		if (fieldComputacaoSet.contains(field) && degreeGraduacaoSet.contains(degree)) {
			return Curso.GRADUACAO_CIENCIA_DA_COMPUTACAO;
		}
		if (fieldComputacaoSet.contains(field) && degreeDoutoradoSet.contains(degree)) {
			return Curso.DOUTORADO_EM_CIENCIA_DA_COMPUTACAO;
		}
		if (fieldInformaticaSet.contains(field) && degreeMestradoSet.contains(degree)) {
			return Curso.MESTRADO_EM_INFOMATICA;
		}
		if (fieldProcDadosSet.contains(field) && degreeGraduacaoSet.contains(degree)) {
			return Curso.GRADUACAO_PROCESSAMENTO_DE_DADOS;
		}
		return null;
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

	private Grau getGrauData(Grau[] alumniGraus, Curso curso) {
		return Arrays.asList(alumniGraus)
				.stream()
				.filter(grau -> grau.getCurso().equals(curso))
				.findAny()
				.orElse(null);
	}

	private int getMatchesFromCurso(UfcgAlumnusData alumni, LinkedinSchoolData schoolData, SchoolName school) {
		Curso curso = getCurso(schoolData);
		Grau alumniGrauData = getGrauData(alumni.getGraus(), curso);

		if (alumniGrauData == null) {
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
			score += getMatchesFromCurso(alumni, linkedinSchool, school);
		}

		return score;
	}

	public Collection<LinkedinAlumnusData> getMatches(UfcgAlumnusData alumni, SchoolName school) throws Exception {
		LinkedinDataHolder linkedinHolder = LinkedinDataHolder.getInstance();
		linkedinHolder.loadLinkedinData();

		Collection<LinkedinAlumnusData> linkedinProfilesList = linkedinHolder.getLinkedinAlumniData();
		Collection<LinkedinAlumnusData> selectedProfilesList = new LinkedList<>();

		String alumniName = alumni.getFullName().toUpperCase();

		linkedinProfilesList.forEach(linkedinProfile -> {
			int score = 0;

			LinkedinSchoolData[] linkedinSchoolData = linkedinProfile.getSchools();
			String linkedinAlumniFullName = linkedinProfile.getFullName().toUpperCase();

			score += getScoreFromName(alumniName, linkedinAlumniFullName);
			score += getScoreFromSchool(alumni, linkedinSchoolData, school);

			if (score >= 1) {
				selectedProfilesList.add(linkedinProfile);
			}
		});

		return selectedProfilesList;
	}
}
