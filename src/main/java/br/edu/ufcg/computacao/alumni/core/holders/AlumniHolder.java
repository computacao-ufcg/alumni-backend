package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.constants.CodigoCurso;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.alumni.core.models.Curso;
import br.edu.ufcg.computacao.alumni.core.models.Grau;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AlumniHolder {
    private Logger LOGGER = Logger.getLogger(AlumniHolder.class);

    private static AlumniHolder instance;
    private UfcgAlumnusData[] alumni;

    private AlumniHolder() throws Exception {
        this.loadAlumni(SystemConstants.DEFAULT_ALUMNI_INPUT_FILE_PATH);
    }

    public static AlumniHolder getInstance() throws Exception {
        synchronized (AlumniHolder.class) {
            if (instance == null) {
                instance = new AlumniHolder();
            }
            return instance;
        }
    }

    public void loadAlumni(String filePath) throws IOException {
        List<UfcgAlumnusData> alumniList = new LinkedList<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            String linkedinId = data[0];
            String name = data[1];
            String codigoCurso = data[2];
            String semestreIngresso = data[3];
            String semestreFormatura = data[4];

            Grau grau = null;
            switch (codigoCurso) {
                case CodigoCurso.GRAD_PROC_DADOS:
                    grau = new Grau(Curso.GRADUACAO_PROCESSAMENTO_DE_DADOS, semestreIngresso, semestreFormatura);
                    break;
                case CodigoCurso.GRAD_COMPUTACAO:
                    grau = new Grau(Curso.GRADUACAO_CIENCIA_DA_COMPUTACAO, semestreIngresso, semestreFormatura);
                    break;
                case CodigoCurso.MEST_INFORMATICA:
                    grau = new Grau(Curso.MESTRADO_EM_INFOMATICA, semestreIngresso, semestreFormatura);
                    break;
                case CodigoCurso.MEST_COMPUTACAO:
                    grau = new Grau(Curso.GRADUACAO_CIENCIA_DA_COMPUTACAO, semestreIngresso, semestreFormatura);
                    break;
                case CodigoCurso.DOUT_COMPUTACAO:
                    grau = new Grau(Curso.GRADUACAO_CIENCIA_DA_COMPUTACAO, semestreIngresso, semestreFormatura);
                    break;
                default:
                    LOGGER.error(String.format(Messages.INVALID_INPUT_S, row));
                    break;
            }
            Grau[] graus = new Grau[1];
            graus[0] = grau;
            UfcgAlumnusData alumnus = new UfcgAlumnusData(linkedinId, name, graus);
            alumniList.add(alumnus);
         }
        csvReader.close();
        this.alumni = new UfcgAlumnusData[alumniList.size()];
        for(int i = 0; i < alumniList.size(); i++) {
            this.alumni[i] = alumniList.get(i);
            LOGGER.info(String.format(Messages.LOADING_ALUMNI_D_S, i, this.alumni[i].getFullName()));
        }
    }

    public UfcgAlumnusData[] getAlumni() {
        return this.alumni;
    }
}
