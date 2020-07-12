package br.edu.ufcg.computacao.alumni;

import java.util.List;

public interface Alumni {

    /**
     *
     * @return List of members of Computação@UFCG alumni.
     * @throws Exception
     */
    public List<String> listMembers() throws Exception;
}
