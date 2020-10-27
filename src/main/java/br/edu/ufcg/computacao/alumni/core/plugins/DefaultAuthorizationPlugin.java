package br.edu.ufcg.computacao.alumni.core.plugins;

import br.edu.ufcg.computacao.alumni.core.models.AlumniOperation;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.common.exceptions.UnauthorizedRequestException;

public class DefaultAuthorizationPlugin implements AuthorizationPlugin {

    @Override
    public boolean isAuthorized(SystemUser requester, AlumniOperation operation) throws UnauthorizedRequestException {
        return true;
    }
}
