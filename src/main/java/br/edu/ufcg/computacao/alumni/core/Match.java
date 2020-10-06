package br.edu.ufcg.computacao.alumni.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.Curso;
import br.edu.ufcg.computacao.alumni.core.models.Grau;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinSchoolData;

public class Match {

	public Match() {
	}
	
	private Curso getCurso(LinkedinSchoolData linkedinSchoolData) {
		String[] fieldsComputacao = new String[] { "COMPUTER", "COMPUT" };
		String[] fieldsInformatica = new String[] { "INFORMATICA" };
		String[] fieldsProcDados = new String[] { "DADOS" };
		String[] degreesGraduacao = new String[] { "BS", "GRAD", "BACHELOR", "BC" };
		String[] degreesMestrado = new String[] { "MSC", "MASTER" };
		String[] degreesDoutorado = new String[] { "PH" };
		
		Set<String> fieldComputacaoSet = new HashSet<>(Arrays.asList(fieldsComputacao));
		Set<String> fieldInformaticaSet = new HashSet<>(Arrays.asList(fieldsInformatica));
		Set<String> fieldProcDadosSet = new HashSet<>(Arrays.asList(fieldsProcDados));
		Set<String> degreeGraduacaoSet = new HashSet<>(Arrays.asList(degreesGraduacao));
		Set<String> degreeMestradoSet = new HashSet<>(Arrays.asList(degreesMestrado));
		Set<String> degreeDoutoradoSet = new HashSet<>(Arrays.asList(degreesDoutorado));
		
		String field = linkedinSchoolData.getField().toUpperCase().trim();
		String degree = linkedinSchoolData.getDegree().toUpperCase().trim();

		if ("".equals(field) || "".equals(degree)) {
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
		if (alumniName.equals(linkedinName))
			return 1;
		return 0;
	}

	private int getScoreFromCurso(Curso alumniCurso, Curso linkedinCurso) {
		if (linkedinCurso == null)
			return 0;
		if (alumniCurso.equals(linkedinCurso))
			return 1;
		return 0;
	}

	private int getScoreFromSchoolUrl(String schoolUrl, String linkedinUrl) {
		if (linkedinUrl == null)
			return 0;
		if (schoolUrl.equals(linkedinUrl))
			return 1;
		return 0;
	}

	private int getScoreFromYear(String alumniYear, String linkedinYear) {
		if (linkedinYear == null)
			return 0;
		if (alumniYear.contains(linkedinYear))
			return 1;
		return 0;
	}
	
	public Grau getGrauData(Grau[] alumniGraus, Curso curso) {
		return Arrays.asList(alumniGraus)
				.stream()
				.filter(grau -> grau.getCurso().equals(curso))
				.findAny()
				.orElse(null);
	}
	
	private int getMatchesFromCurso(UfcgAlumnusData alumni, Curso curso, LinkedinSchoolData schoolData) {
		int score = 0;
		Grau alumniGrauData = getGrauData(alumni.getGraus(), curso);
		
		score += getScoreFromCurso(alumniGrauData.getCurso(), curso);
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
			}
		
			Curso linkedinCurso = getCurso(linkedinSchool);
			
			score += getScoreFromSchoolUrl(schoolUrl, linkedinSchoolUrl);
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
