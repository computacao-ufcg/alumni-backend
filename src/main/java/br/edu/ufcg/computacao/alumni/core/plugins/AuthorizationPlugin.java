package br.edu.ufcg.computacao.alumni.core.plugins;

import br.edu.ufcg.computacao.alumni.core.models.*;
import br.edu.ufcg.computacao.alumni.core.models.orders.Order;

public class AuthorizationPlugin<T extends AlumniOperation> {
    public boolean isAuthorized(SystemUser requester, RasOperation operation) throws Exception {
        Order order = operation.getOrder();
        ResourceType type = operation.getResourceType();
        if (order != null) {
            // Check whether requester owns order
            SystemUser orderOwner = order.getSystemUser();
            if (!orderOwner.equals(requester)) {
                throw new Exception();
            }
            // Check if requested type matches order type
            ResourceType orderType = order.getType();
            switch (orderType) {
                case NETWORK:
                    if (!type.equals(ResourceType.NETWORK) && !type.equals(ResourceType.SECURITY_RULE))
                        throw new Exception();
                    break;
                case PUBLIC_IP:
                    if (!type.equals(ResourceType.PUBLIC_IP) && !type.equals(ResourceType.SECURITY_RULE))
                        throw new Exception();
                    break;
                default:
                    if (!order.getType().equals(type))
                        throw new Exception();
                    break;
            }
        }
        return true;
    }
}
