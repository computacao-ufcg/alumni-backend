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
import br.edu.ufcg.computacao.alumni.core.models.Grau;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinSchoolData;

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
		Set<String> specialNames = new HashSet<>(Arrays.asList(new String[] {"DE", "DA", "O", "A", "E"}));
		String[] splitedName = name.split(" ");
		String[] splitedFilteredName = new String[splitedName.length];
		
		for (int i = 0; i < splitedName.length; i++) {
			String nome = splitedName[i].toUpperCase();
			if (!specialNames.contains(nome)) { 
				splitedFilteredName[i] = nome;
			}
		}
		return splitedFilteredName;
	}
	
	private int getNamesLength(String[] splitedName) {
		int cont = 0;
		for (String nome : splitedName) {
			cont += nome == null ? 0 : 1;
		}
		return cont;
	}
	
	private String getFirstName(String name) {
		Set<String> specialNames = new HashSet<>(Arrays.asList(new String[] {"JUNIOR", "JR", "NETO", "SOBRINHO"}));
		String[] splitedName = filterName(name);
		int cont = 0;
		
		for (String nome : splitedName) { 
			if (specialNames.contains(nome)) {
				cont++;
			}
		}
		
		if (getNamesLength(splitedName) - cont <= 4) {
			return (splitedName[0] + " " + splitedName[1]).trim();
		} else {
			return splitedName[0].trim();
		}
	}
	
	private String getLastName(String name) {
		List<String> names = Arrays.asList(filterName(name));
		List<String> firstNameList = Arrays.asList(getFirstName(name).split(" "));

		List<String> lastNameList = names.stream().filter(n -> !firstNameList.contains(n)).collect(Collectors.toList());
		String lastName = lastNameList.stream().reduce("", (a, b) -> a + " " + b).trim();
		
		return lastName;
	}

	private int getScoreFromName(String alumniName, String linkedinName) {
		if (alumniName.equals(linkedinName)) return 100;
		
		String alumniFirstName = getFirstName(alumniName);
		String alumniLastName = getLastName(alumniName);
		
		String linkedinFirstName = getFirstName(linkedinName);
		String linkedinLastName = getLastName(linkedinName);
		
		int score = 0;
		if (alumniFirstName.equals(linkedinFirstName)) score += 60;
		if (alumniLastName.equals(linkedinLastName)) score += 40;
		return score;
	}

	private int getScoreFromYear(String alumniYear, String linkedinYear) {
		if (linkedinYear == null) return 0;
		if (alumniYear.contains(linkedinYear)) return 1;
		return 0;
	}
	
	private Grau getGrauData(Grau[] alumniGraus, Curso curso) {
		return Arrays.asList(alumniGraus)
				.stream()
				.filter(grau -> grau.getCurso().equals(curso))
				.findAny()
				.orElse(null);
	}
	
	private int getMatchesFromCurso(UfcgAlumnusData alumni, LinkedinSchoolData schoolData, String schoolUrl) {
		Curso curso = getCurso(schoolData);
		Grau alumniGrauData = getGrauData(alumni.getGraus(), curso);
		if (alumniGrauData == null) {
			return 0;
		}
		
		int score = 1;
		score += getScoreFromYear(alumniGrauData.getSemestreIngresso(), schoolData.getDateRange().getStartYear());
		score += getScoreFromYear(alumniGrauData.getSemestreFormatura(), schoolData.getDateRange().getEndYear());
		score += getScoreFromSchoolData(schoolData, schoolUrl);
		
		return score;
	}
	
	private boolean isUfpb(int endYear, String schoolName) {
		Set<String> ufpbSinonimos = new HashSet<>(Arrays.asList(new String[] { "UFPB", "UNIVERSIDADE FEDERAL DA PARAÍBA", "UNIVERSIDADE FEDERAL DA PARAIBA" }));
		return (endYear >= 1987 && endYear <= 2002) && ufpbSinonimos.contains(schoolName);
	}
	
	private boolean isUfcg(String schoolName) {
		Set<String> ufcgSinonimos = new HashSet<>(Arrays.asList(new String[] { "UFCG", "UNIVERSIDADE FEDERAL DE CAMPINA GRANDE", "UNIVERSIDADE FEDERAL DE C. GRANDE - UFCG" }));
		return ufcgSinonimos.contains(schoolName);
	}
	
	private int getScoreFromSchoolData(LinkedinSchoolData linkedinSchoolData, String schoolUrl) {
		if (linkedinSchoolData.getSchoolUrl().equals(schoolUrl)) return 20;
		
		String linkedinSchoolName = linkedinSchoolData.getSchoolName().toUpperCase().trim();
		int endYear = Integer.parseInt(linkedinSchoolData.getDateRange().getEndYear().trim());
		
		if (isUfpb(endYear, linkedinSchoolName) || isUfcg(linkedinSchoolName)) {
			return 20;
		}
		return 0;
	}
	
	private int getScoreFromSchool(UfcgAlumnusData alumni, LinkedinSchoolData[] linkedinSchoolData, String schoolUrl) {
		int score = 0;
		for (LinkedinSchoolData linkedinSchool : linkedinSchoolData) {
			String linkedinSchoolUrl = linkedinSchool.getSchoolUrl();
			if (!schoolUrl.equals(linkedinSchoolUrl)) {
				score = 0;
				break;
			} else {
				score += getMatchesFromCurso(alumni, linkedinSchool, schoolUrl);
			}
		}
		return score;
	}

	public Collection<LinkedinAlumnusData> getMatches(UfcgAlumnusData alumni, String schoolUrl) throws Exception {
		Collection<LinkedinAlumnusData> linkedinProfilesList = LinkedinDataHolder.getInstance().getLinkedinAlumniData();
		Collection<LinkedinAlumnusData> selectedProfilesList = new LinkedList<>();

		String alumniName = alumni.getFullName().toUpperCase();

		linkedinProfilesList.forEach(linkedinProfile -> {
			int score = 0;

			LinkedinSchoolData[] linkedinSchoolData = linkedinProfile.getSchools();
			String linkedinAlumniFullName = linkedinProfile.getFullName().toUpperCase();

			score += getScoreFromName(alumniName, linkedinAlumniFullName);
			score += getScoreFromSchool(alumni, linkedinSchoolData, schoolUrl);
			
			if (score >= 1) {
				selectedProfilesList.add(linkedinProfile);
			}
		});
		
		return selectedProfilesList;
	}

}
