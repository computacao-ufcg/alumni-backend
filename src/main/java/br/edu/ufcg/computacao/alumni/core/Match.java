package br.edu.ufcg.computacao.alumni.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
	
	private final String COMPUTACAO = "computacao";
	private final String INFORMATICA = "informatica";
	private final String PROC_DADOS = "proc-dados";
	private final String GRADUACAO = "graduacao";
	private final String MESTRADO = "mestrado";
	private final String DOUTORADO = "doutorado";
	
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
		String[] values = props.getProperty(property).split("");
		
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

	private int getScoreFromName(String alumniName, String linkedinName) {
		if (alumniName.equals(linkedinName)) return 1;
		return 0;
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
	
	private int getMatchesFromCurso(UfcgAlumnusData alumni, Curso curso, LinkedinSchoolData schoolData) {
		Grau alumniGrauData = getGrauData(alumni.getGraus(), curso);
		if (alumniGrauData == null) {
			return 0;
		}
		
		int score = 1;
		score += getScoreFromYear(alumniGrauData.getSemestreIngresso(), schoolData.getDateRange().getStartYear());
		score += getScoreFromYear(alumniGrauData.getSemestreFormatura(), schoolData.getDateRange().getEndYear());
		
		return score;
	}
	
	private int getScoreFromSchoolData(UfcgAlumnusData alumni, LinkedinSchoolData[] linkedinSchoolData, String schoolUrl) {
		int score = 0;
		for (LinkedinSchoolData linkedinSchool : linkedinSchoolData) {
			String linkedinSchoolUrl = linkedinSchool.getSchoolUrl();
			if (!schoolUrl.equals(linkedinSchoolUrl)) {
				score = 0;
				break;
			} else {
				score += 1;
			}
			Curso linkedinCurso = getCurso(linkedinSchool);
			score += getMatchesFromCurso(alumni, linkedinCurso, linkedinSchool);
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
			score += getScoreFromSchoolData(alumni, linkedinSchoolData, schoolUrl);
			
			if (score >= 1) {
				selectedProfilesList.add(linkedinProfile);
			}
		});
		
		return selectedProfilesList;
	}

}
